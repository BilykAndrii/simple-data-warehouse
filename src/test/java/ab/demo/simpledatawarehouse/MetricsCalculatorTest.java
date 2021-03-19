package ab.demo.simpledatawarehouse;

import ab.demo.simpledatawarehouse.model.CampaignMetric;
import ab.demo.simpledatawarehouse.service.MetricsCalculator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MetricsCalculatorTest {

    private final static String SOURCE = "somesrc";
    private final static String CTR = "ctr";

    @Test
    public void shouldCalculateCTR() {
        CampaignMetric campaignMetricOne
                = CampaignMetric.builder().dataSource(SOURCE).clicks(5L).impressions(10L).build();
        CampaignMetric campaignMetricTwo
                = CampaignMetric.builder().dataSource(SOURCE).clicks(0L).impressions(10L).build();

        List<CampaignMetric> campaignMetrics = Arrays.asList(campaignMetricOne, campaignMetricTwo);
        campaignMetrics.forEach(MetricsCalculator.getFormulas().get(CTR));

        assertEquals("50,00%", campaignMetrics.get(0).getClickThroughRate());
        assertEquals("0,00%", campaignMetrics.get(1).getClickThroughRate());

    }

}
