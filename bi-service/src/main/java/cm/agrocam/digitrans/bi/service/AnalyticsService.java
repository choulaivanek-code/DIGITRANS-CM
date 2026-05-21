package cm.agrocam.digitrans.bi.service;

import cm.agrocam.digitrans.bi.dto.*;
import cm.agrocam.digitrans.bi.dto.downstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsService.class);

    private final RestClient erpRestClient;
    private final RestClient crmRestClient;
    private final RestClient scmRestClient;

    public AnalyticsService(RestClient erpRestClient, RestClient crmRestClient, RestClient scmRestClient) {
        this.erpRestClient = erpRestClient;
        this.crmRestClient = crmRestClient;
        this.scmRestClient = scmRestClient;
    }

    /**
     * Aggregates restaurant orders, customer counts, average ticket size, and top restaurants per city.
     * Caches result for 5 minutes in Redis.
     */
    @Cacheable(value = "salesReport")
    public SalesReport getSalesReport() {
        log.info("Generating Sales Report... (Cache miss - fetching from CRM service)");
        try {
            // Fetch Orders
            ApiResponse<List<OrderResponse>> ordersResponse = crmRestClient.get()
                    .uri("/api/orders")
                    .header("X-User-Id", "system")
                    .header("X-User-Roles", "ROLE_MANAGER")
                    .retrieve()
                    .body(new ParameterizedTypeReference<ApiResponse<List<OrderResponse>>>() {});

            List<OrderResponse> orders = (ordersResponse != null && ordersResponse.isSuccess() && ordersResponse.getData() != null)
                    ? ordersResponse.getData()
                    : Collections.emptyList();

            if (orders.isEmpty()) {
                return SalesReport.builder()
                        .totalOrders(0L)
                        .totalRevenue(0.0)
                        .averageTicket(0.0)
                        .topRestaurantsByCity(Collections.emptyMap())
                        .build();
            }

            long totalOrders = orders.size();
            double totalRevenue = orders.stream()
                    .mapToDouble(OrderResponse::getTotalAmount)
                    .sum();
            double averageTicket = totalOrders > 0 ? totalRevenue / totalOrders : 0.0;

            // Group sales by restaurant
            Map<Long, List<OrderResponse>> ordersByRestaurant = orders.stream()
                    .filter(o -> o.getRestaurant() != null)
                    .collect(Collectors.groupingBy(o -> o.getRestaurant().getId()));

            List<RestaurantSalesDTO> restaurantSales = new ArrayList<>();
            for (Map.Entry<Long, List<OrderResponse>> entry : ordersByRestaurant.entrySet()) {
                Long restId = entry.getKey();
                List<OrderResponse> restOrders = entry.getValue();
                String restName = restOrders.get(0).getRestaurant().getName();
                String city = restOrders.get(0).getRestaurant().getCity();

                double sales = restOrders.stream().mapToDouble(OrderResponse::getTotalAmount).sum();
                long count = restOrders.size();

                restaurantSales.add(new RestaurantSalesDTO(restId, restName + " (" + city + ")", sales, count));
            }

            // Group by city and sort by sales descending
            Map<String, List<RestaurantSalesDTO>> topRestaurantsByCity = orders.stream()
                    .filter(o -> o.getRestaurant() != null)
                    .map(o -> o.getRestaurant().getCity())
                    .distinct()
                    .collect(Collectors.toMap(
                            city -> city,
                            city -> restaurantSales.stream()
                                    .filter(rs -> rs.getRestaurantName().contains("(" + city + ")"))
                                    .sorted(Comparator.comparingDouble(RestaurantSalesDTO::getSales).reversed())
                                    .collect(Collectors.toList())
                    ));

            return SalesReport.builder()
                    .totalOrders(totalOrders)
                    .totalRevenue(totalRevenue)
                    .averageTicket(averageTicket)
                    .topRestaurantsByCity(topRestaurantsByCity)
                    .build();

        } catch (RestClientException | NullPointerException e) {
            log.error("Failed to fetch data from crm-service. Graceful degradation fallback activated.", e);
            return SalesReport.builder()
                    .totalOrders(0L)
                    .totalRevenue(0.0)
                    .averageTicket(0.0)
                    .topRestaurantsByCity(Collections.emptyMap())
                    .build();
        }
    }

    /**
     * Aggregates warehouse stock levels, shipments in transit, and warehouse usage.
     * Caches result for 5 minutes in Redis.
     */
    @Cacheable(value = "supplyReport")
    public SupplyReport getSupplyReport() {
        log.info("Generating Supply Chain Report... (Cache miss - fetching from SCM service)");
        try {
            // Fetch Products
            ApiResponse<List<ProductResponse>> productsResponse = scmRestClient.get()
                    .uri("/api/products")
                    .header("X-User-Id", "system")
                    .header("X-User-Roles", "ROLE_MANAGER")
                    .retrieve()
                    .body(new ParameterizedTypeReference<ApiResponse<List<ProductResponse>>>() {});

            List<ProductResponse> products = (productsResponse != null && productsResponse.isSuccess() && productsResponse.getData() != null)
                    ? productsResponse.getData()
                    : Collections.emptyList();

            // Fetch Warehouses
            ApiResponse<List<WarehouseResponse>> warehousesResponse = scmRestClient.get()
                    .uri("/api/warehouses")
                    .header("X-User-Id", "system")
                    .header("X-User-Roles", "ROLE_MANAGER")
                    .retrieve()
                    .body(new ParameterizedTypeReference<ApiResponse<List<WarehouseResponse>>>() {});

            List<WarehouseResponse> warehouses = (warehousesResponse != null && warehousesResponse.isSuccess() && warehousesResponse.getData() != null)
                    ? warehousesResponse.getData()
                    : Collections.emptyList();

            // Fetch Shipments
            ApiResponse<List<ShipmentResponse>> shipmentsResponse = scmRestClient.get()
                    .uri("/api/shipments")
                    .header("X-User-Id", "system")
                    .header("X-User-Roles", "ROLE_MANAGER")
                    .retrieve()
                    .body(new ParameterizedTypeReference<ApiResponse<List<ShipmentResponse>>>() {});

            List<ShipmentResponse> shipments = (shipmentsResponse != null && shipmentsResponse.isSuccess() && shipmentsResponse.getData() != null)
                    ? shipmentsResponse.getData()
                    : Collections.emptyList();

            // 1. Calculate Stocks by Warehouse using SKU-based allocation rules
            Map<String, Integer> stocksByWarehouse = new HashMap<>();
            for (WarehouseResponse w : warehouses) {
                stocksByWarehouse.put(w.getName(), 0);
            }

            for (ProductResponse p : products) {
                String sku = p.getSku() != null ? p.getSku().toUpperCase() : "";
                String targetWarehouseName = "Entrepôt Portuaire Douala"; // Fallback default

                if (sku.contains("COCOA") || sku.contains("CHICK")) {
                    targetWarehouseName = warehouses.stream()
                            .filter(w -> w.getId() == 1L)
                            .map(WarehouseResponse::getName)
                            .findFirst()
                            .orElse("Entrepôt Portuaire Douala");
                } else if (sku.contains("COFFEE")) {
                    targetWarehouseName = warehouses.stream()
                            .filter(w -> w.getId() == 2L)
                            .map(WarehouseResponse::getName)
                            .findFirst()
                            .orElse("Entrepôt Central Yaoundé");
                } else if (sku.contains("FEED")) {
                    targetWarehouseName = warehouses.stream()
                            .filter(w -> w.getId() == 3L)
                            .map(WarehouseResponse::getName)
                            .findFirst()
                            .orElse("Entrepôt Régional Bafoussam");
                }

                stocksByWarehouse.put(targetWarehouseName, stocksByWarehouse.getOrDefault(targetWarehouseName, 0) + p.getStockQuantity());
            }

            // 2. Count Active Shipments (Status is SHIPPED or IN_TRANSIT, i.e., not DELIVERED)
            long activeShipmentsCount = shipments.stream()
                    .filter(s -> s.getStatus() != null && (s.getStatus().equalsIgnoreCase("SHIPPED") || s.getStatus().equalsIgnoreCase("IN_TRANSIT")))
                    .count();

            // 3. Calculate Delivery Success Rate
            long totalShipments = shipments.size();
            long deliveredShipments = shipments.stream()
                    .filter(s -> s.getStatus() != null && s.getStatus().equalsIgnoreCase("DELIVERED"))
                    .count();
            double successRate = totalShipments > 0 ? ((double) deliveredShipments / totalShipments) * 100.0 : 0.0;

            return SupplyReport.builder()
                    .stocksByWarehouse(stocksByWarehouse)
                    .activeShipmentsCount(activeShipmentsCount)
                    .deliverySuccessRate(successRate)
                    .build();

        } catch (RestClientException | NullPointerException e) {
            log.error("Failed to fetch data from supply-chain-service. Graceful degradation fallback activated.", e);
            return SupplyReport.builder()
                    .stocksByWarehouse(Collections.emptyMap())
                    .activeShipmentsCount(0L)
                    .deliverySuccessRate(0.0)
                    .build();
        }
    }

    /**
     * Aggregates employee statistics, payroll totals, and financial efficiency ratios.
     * Caches result for 5 minutes in Redis.
     */
    @Cacheable(value = "employeeReport")
    public EmployeeReport getEmployeeReport() {
        log.info("Generating Employee/Financial Report... (Cache miss - fetching from ERP service)");
        try {
            // Fetch Employees
            ApiResponse<List<EmployeeResponse>> employeesResponse = erpRestClient.get()
                    .uri("/api/employees")
                    .header("X-User-Id", "system")
                    .header("X-User-Roles", "ROLE_ADMIN")
                    .retrieve()
                    .body(new ParameterizedTypeReference<ApiResponse<List<EmployeeResponse>>>() {});

            List<EmployeeResponse> employees = (employeesResponse != null && employeesResponse.isSuccess() && employeesResponse.getData() != null)
                    ? employeesResponse.getData()
                    : Collections.emptyList();

            // Fetch Financials
            ApiResponse<List<FinancialResponse>> financialsResponse = erpRestClient.get()
                    .uri("/api/financials")
                    .header("X-User-Id", "system")
                    .header("X-User-Roles", "ROLE_ADMIN")
                    .retrieve()
                    .body(new ParameterizedTypeReference<ApiResponse<List<FinancialResponse>>>() {});

            List<FinancialResponse> financials = (financialsResponse != null && financialsResponse.isSuccess() && financialsResponse.getData() != null)
                    ? financialsResponse.getData()
                    : Collections.emptyList();

            // 1. Staff count by department
            Map<String, Long> staffCountByDepartment = employees.stream()
                    .filter(e -> e.getDepartment() != null)
                    .collect(Collectors.groupingBy(
                            e -> e.getDepartment().getName(),
                            Collectors.counting()
                    ));

            // 2. Total payroll
            double totalPayroll = employees.stream()
                    .mapToDouble(EmployeeResponse::getSalary)
                    .sum();

            // 3. Ratio charges/revenus financiers
            double totalRevenues = financials.stream()
                    .filter(f -> f.getType() != null && f.getType().equalsIgnoreCase("REVENUE"))
                    .mapToDouble(FinancialResponse::getAmount)
                    .sum();

            double totalExpenses = financials.stream()
                    .filter(f -> f.getType() != null && f.getType().equalsIgnoreCase("EXPENSE"))
                    .mapToDouble(FinancialResponse::getAmount)
                    .sum();

            double ratio = totalRevenues > 0 ? totalExpenses / totalRevenues : 0.0;

            return EmployeeReport.builder()
                    .staffCountByDepartment(staffCountByDepartment)
                    .totalPayroll(totalPayroll)
                    .expenseToRevenueRatio(ratio)
                    .build();

        } catch (RestClientException | NullPointerException e) {
            log.error("Failed to fetch data from erp-service. Graceful degradation fallback activated.", e);
            return EmployeeReport.builder()
                    .staffCountByDepartment(Collections.emptyMap())
                    .totalPayroll(0.0)
                    .expenseToRevenueRatio(0.0)
                    .build();
        }
    }
}
