package ab.demo.simpledatawarehouse;


import ab.demo.simpledatawarehouse.model.QueryParameters;
import ab.demo.simpledatawarehouse.service.QueryParametersComposer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryParametersComposerTest {

    private static final String METRICS = "clicks";
    private static final String DATASRC = "dataSource";
    private static final String DATE_TO = "11/11/11";
    private static final LocalDate DATE = LocalDate.parse("2011-11-11");

    @Test
    public void shouldReturnComposedParameters() {

       QueryParameters params = QueryParametersComposer.compose(METRICS, DATASRC, null,
               null,null, DATE_TO, null);

       assertEquals(expectedParams(), params);

    }

    private QueryParameters expectedParams() {
        return QueryParameters.builder()
                .metrics(Arrays.asList(METRICS))
                .campaignFilter(null)
                .dimensions(Arrays.asList(DATASRC))
                .dataSourceFilter(null)
                .dateFrom(null)
                .originalMetrics(Arrays.asList(METRICS))
                .calculatedMetrics(null)
                .groupedBy(Arrays.asList(DATASRC))
                .dateTo(DATE).build();
    }
}
