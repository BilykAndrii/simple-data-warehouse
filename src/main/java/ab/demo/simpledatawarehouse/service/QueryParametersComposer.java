package ab.demo.simpledatawarehouse.service;

import ab.demo.simpledatawarehouse.model.QueryParameters;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

/*
 All unknown params are filtered out
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryParametersComposer {

    public static final String CLICKS = "clicks";
    public static final String IMPRESSIONS = "impressions";
    public static final String DATA_SOURCE = "dataSource";
    public static final String CAMPAIGN = "campaign";
    public static final String DATE = "date";
    public static final String CLICK_THROUGH_RATE = "ctr";

    private static final List<String> DEFAULT_METRICS = Arrays.asList(CLICKS, IMPRESSIONS);
    private static final List<String> CALCULATED_METRICS = Arrays.asList(CLICK_THROUGH_RATE);

    private static final String DEFAULT_DATE_FORMAT = "MM/dd/yy";
    private static final List<String> DIMENSIONS = Arrays.asList(DATA_SOURCE, CAMPAIGN, DATE);

    public static QueryParameters compose(String metricsParam, String groupedByParam, String dataSourceFilterParam,
                                          String campaignFilterParam, String dateFromParam, String dateToParam,
                                          String dateFormatParam) {

        List<String> originalMetrics = extractSimpleParam(metricsParam);
        List<String> baseMetrics = filterFields(originalMetrics, DEFAULT_METRICS);
        List<String> calculatedMetrics = filterFields(originalMetrics, CALCULATED_METRICS);

        List<String> groupedBy = extractSimpleParam(groupedByParam);
        groupedBy = filterFields(groupedBy, DIMENSIONS);

        List<String> dataSourceFilter = extractSimpleParam(dataSourceFilterParam);
        List<String> campaignFilter = extractSimpleParam(campaignFilterParam);

        LocalDate dateFrom = extractDate(dateFromParam, dateFormatParam);
        LocalDate dateTo = extractDate(dateToParam, dateFormatParam);

        QueryParameters.QueryParametersBuilder builder = QueryParameters.builder();

        if (!isEmpty(groupedBy)) {
            builder.groupedBy(groupedBy);
            builder.dimensions(groupedBy);
        } else {
            builder.dimensions(DIMENSIONS);
        }

        if (!isEmpty(originalMetrics)) {
            builder.originalMetrics(originalMetrics);
        }

        if (isEmpty(baseMetrics) || !isEmpty(calculatedMetrics)) {
            builder.metrics(DEFAULT_METRICS);
        } else {
            builder.metrics(baseMetrics);
        }

        if (!isEmpty(calculatedMetrics)) {
            builder.calculatedMetrics(calculatedMetrics);
        }

        if (!isEmpty(dataSourceFilter)) {
            builder.dataSourceFilter(dataSourceFilter);
        }

        if (!isEmpty(campaignFilter)) {
            builder.campaignFilter(campaignFilter);
        }

        if (nonNull(dateFrom)) {
            builder.dateFrom(dateFrom);
        }

        if (nonNull(dateTo)) {
            builder.dateTo(dateTo);
        }

        return builder.build();
    }

    private static List<String> filterFields(List<String> fields, List<String> defaultFields) {
        if (isEmpty(fields) || isEmpty(defaultFields)) {
            return Collections.emptyList();
        }

        return fields.stream().filter(defaultFields::contains).collect(Collectors.toList());
    }

    private static LocalDate extractDate(String date, String dateFormat) {
        if (isBlank(date)) {
            return null;
        }
        if (isBlank(dateFormat)) {
            dateFormat = DEFAULT_DATE_FORMAT;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        return LocalDate.parse(date, formatter);
    }

    private static List<String> extractSimpleParam(String param) {
        if (isBlank(param)) {
            return Collections.emptyList();
        }
        return Arrays.asList(param.split(","));
    }
}
