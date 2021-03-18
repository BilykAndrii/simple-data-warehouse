package ab.demo.simpledatawarehouse.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)

@Entity
@IdClass(CampaignMetricsKey.class)
public class CampaignMetric implements Serializable {

    @Id
    private String dataSource;
    @Id
    private String campaign;
    @Id
    private LocalDate date;
    private Long clicks;
    private Long impressions;

    @Transient
    private String clickThroughRate;

    public CampaignMetric(String dataSource, String campaign, LocalDate date, Long clicks, Long impressions) {
        this.dataSource = dataSource;
        this.campaign = campaign;
        this.date = date;
        this.clicks = clicks;
        this.impressions = impressions;
    }
}
