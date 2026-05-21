package cm.agrocam.digitrans.bi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${services.erp.url}")
    private String erpUrl;

    @Value("${services.crm.url}")
    private String crmUrl;

    @Value("${services.scm.url}")
    private String scmUrl;

    @Bean
    public RestClient erpRestClient() {
        return RestClient.builder()
                .baseUrl(erpUrl)
                .build();
    }

    @Bean
    public RestClient crmRestClient() {
        return RestClient.builder()
                .baseUrl(crmUrl)
                .build();
    }

    @Bean
    public RestClient scmRestClient() {
        return RestClient.builder()
                .baseUrl(scmUrl)
                .build();
    }
}
