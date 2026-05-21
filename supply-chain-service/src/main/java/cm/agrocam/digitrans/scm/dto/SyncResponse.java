package cm.agrocam.digitrans.scm.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncResponse {
    private int processedCount;
    private int ignoredCount;
    private int errorCount;
    private List<String> details;
}
