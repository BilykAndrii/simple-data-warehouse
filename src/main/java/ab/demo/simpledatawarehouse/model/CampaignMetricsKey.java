package ab.demo.simpledatawarehouse.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class CampaignMetricsKey implements Serializable {
    private String dataSource;
    private String campaign;
    private LocalDate date;
}
