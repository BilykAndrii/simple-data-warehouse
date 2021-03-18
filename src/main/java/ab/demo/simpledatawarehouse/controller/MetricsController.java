package ab.demo.simpledatawarehouse.controller;

import ab.demo.simpledatawarehouse.model.CampaignMetric;
import ab.demo.simpledatawarehouse.service.MetricsService;
import ab.demo.simpledatawarehouse.model.QueryParameters;
import ab.demo.simpledatawarehouse.service.QueryParametersComposer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    @Autowired
    private MetricsService metricsService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<CampaignMetric> getAllMetrics() {
        return metricsService.getAll();
    }

    @GetMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<CampaignMetric> getMetricsByQuery(@RequestParam(value = "metrics", required = false) String metrics,
                                                  @RequestParam(value = "groupedBy", required = false) String groupedBy,
                                                  @RequestParam(value = "dataSource", required = false) String dataSourceFilter,
                                                  @RequestParam(value = "campaign", required = false) String campaignFilter,
                                                  @RequestParam(value = "dateFrom", required = false) String dateFrom,
                                                  @RequestParam(value = "dateTo", required = false) String dateTo,
                                                  @RequestParam(value = "dateFormat", required = false) String dateformat) {

        QueryParameters queryParameters = QueryParametersComposer.compose(metrics, groupedBy, dataSourceFilter,
                                                  campaignFilter, dateFrom, dateTo, dateformat);
        return metricsService.getByQuery(queryParameters);
    }

}
