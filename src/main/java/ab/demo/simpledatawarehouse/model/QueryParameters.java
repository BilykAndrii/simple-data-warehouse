package ab.demo.simpledatawarehouse.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class QueryParameters {
    private List<String> dimensions;
    private List<String> metrics;
    private List<String> calculatedMetrics;
    private List<String> originalMetrics;
    private List<String> groupedBy;
    private List<String> dataSourceFilter;
    private List<String> campaignFilter;
    private LocalDate dateFrom;
    private LocalDate dateTo;
}
