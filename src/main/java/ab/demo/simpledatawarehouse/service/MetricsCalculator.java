package ab.demo.simpledatawarehouse.service;

import ab.demo.simpledatawarehouse.model.CampaignMetric;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static ab.demo.simpledatawarehouse.service.QueryParametersComposer.CLICK_THROUGH_RATE;
import static java.util.Objects.isNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MetricsCalculator {

    static final Map<String, Consumer<CampaignMetric>> formulas;
    static {
        formulas = new HashMap<>();
        formulas.put(CLICK_THROUGH_RATE, getClickThroughRateFormula());
    }

    private static Consumer<CampaignMetric> getClickThroughRateFormula() {
        return campaignMetric -> {
            if (isNull(campaignMetric)) {
                return;
            }
            double ctr = 0;
            if (campaignMetric.getImpressions() != 0) {
                ctr = (double) campaignMetric.getClicks() * 100 / campaignMetric.getImpressions();
            }
            campaignMetric.setClickThroughRate(String.format("%.2f%%", ctr));
        };
    }
}
