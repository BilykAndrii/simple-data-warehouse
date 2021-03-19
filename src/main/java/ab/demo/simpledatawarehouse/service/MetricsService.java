package ab.demo.simpledatawarehouse.service;

import ab.demo.simpledatawarehouse.model.CampaignMetric;
import ab.demo.simpledatawarehouse.repo.CampaignMetricRepository;
import ab.demo.simpledatawarehouse.model.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.*;

import static ab.demo.simpledatawarehouse.service.QueryParametersComposer.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class MetricsService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CampaignMetricRepository repository;

    public List<CampaignMetric> getAll() {
        return (List<CampaignMetric>) repository.findAll();
    }

    public List<CampaignMetric> getByQuery(QueryParameters queryParameters) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CampaignMetric> query = criteriaBuilder.createQuery(CampaignMetric.class);
        Root<CampaignMetric> campaignMetrics = query.from(CampaignMetric.class);

        List<Selection<?>> columns = getColumns(queryParameters, campaignMetrics, criteriaBuilder);
        if (!isEmpty(columns)) {
            query.multiselect(columns);
        } else {
            query.select(campaignMetrics);
        }

        Predicate[] conditions = buildConditions(campaignMetrics, queryParameters, criteriaBuilder);
        query.where(conditions);

        List<Expression<?>> groupedByColumns = getGroupedBy(queryParameters, campaignMetrics);
        query.groupBy(groupedByColumns);

        List<CampaignMetric> fetchedMetrics = entityManager.createQuery(query).getResultList();

        enrichWithCalculatedMetrics(fetchedMetrics, queryParameters);

        filterUnusedMetrics(fetchedMetrics, queryParameters);

        return fetchedMetrics;
    }

    private void filterUnusedMetrics(List<CampaignMetric> fetchedMetrics, QueryParameters queryParameters) {
        if (isNull(queryParameters) || isEmpty(queryParameters.getOriginalMetrics())) {
            return;
        }

        boolean needClicks = queryParameters.getOriginalMetrics().contains(CLICKS);
        boolean needImpressions = queryParameters.getOriginalMetrics().contains(IMPRESSIONS);

        fetchedMetrics.forEach(campaignMetric -> {
            if (isNull(campaignMetric)) {
                return;
            }
            if (!needClicks) {
                campaignMetric.setClicks(null);
            }
            if (!needImpressions) {
                campaignMetric.setImpressions(null);
            }
        });
    }

    private void enrichWithCalculatedMetrics(List<CampaignMetric> fetchedMetrics, QueryParameters queryParameters) {
        if (isNull(queryParameters) || isEmpty(queryParameters.getCalculatedMetrics())) {
            return;
        }

        List<String> calculatedMetrics = queryParameters.getCalculatedMetrics();
        calculatedMetrics.forEach(calculatedMetric -> {
            if (!isNull(MetricsCalculator.getFormulas().get(calculatedMetric))) {
                fetchedMetrics.forEach(MetricsCalculator.getFormulas().get(calculatedMetric));
            }
        });
    }

    private List<Expression<?>> getGroupedBy(QueryParameters queryParameters,
                                             Root<CampaignMetric> campaignMetrics) {
        if (isNull(queryParameters)) {
            return Collections.emptyList();
        }
        List<Expression<?>> expressions = new ArrayList<>();

        List<String> groupedByColumns = queryParameters.getGroupedBy();
        if (!isEmpty(groupedByColumns)) {
            groupedByColumns.forEach(groupedByColumn -> expressions.add(campaignMetrics.get(groupedByColumn)));
        }

        return expressions;
    }

    private List<Selection<?>> getColumns(QueryParameters queryParameters, Root<CampaignMetric> campaignMetrics,
                                          CriteriaBuilder criteriaBuilder) {
        if (isNull(queryParameters)) {
            return Collections.emptyList();
        }
        List<Selection<?>> selections = new ArrayList<>();

        List<String> dimensions = queryParameters.getDimensions();

        if (dimensions.contains(DATA_SOURCE)) {
            selections.add(campaignMetrics.get(DATA_SOURCE));
        } else {
            selections.add(criteriaBuilder.nullLiteral(String.class));
        }

        if (dimensions.contains(CAMPAIGN)) {
            selections.add(campaignMetrics.get(CAMPAIGN));
        } else {
            selections.add(criteriaBuilder.nullLiteral(String.class));
        }

        if (dimensions.contains(DATE)) {
            selections.add(campaignMetrics.get(DATE));
        } else {
            selections.add(criteriaBuilder.nullLiteral(LocalDate.class));
        }

        addMetrics(queryParameters, selections, campaignMetrics, criteriaBuilder);

        return selections;
    }

    private void addMetrics(QueryParameters queryParameters, List<Selection<?>> selections,
                            Root<CampaignMetric> campaignMetrics, CriteriaBuilder criteriaBuilder) {
        if (isNull(queryParameters)) {
            return;
        }

        boolean isGropedBy = !isEmpty(queryParameters.getGroupedBy());

        List<String> metrics = queryParameters.getMetrics();

        if (metrics.contains(CLICKS)) {
            if (isGropedBy) {
                selections.add(criteriaBuilder.sum(campaignMetrics.get(CLICKS)));
            } else {
                selections.add(campaignMetrics.get(CLICKS));
            }
        } else {
            selections.add(criteriaBuilder.nullLiteral(Long.class));
        }

        if (metrics.contains(IMPRESSIONS)) {
            if (isGropedBy) {
                selections.add(criteriaBuilder.sum(campaignMetrics.get(IMPRESSIONS)));
            } else {
                selections.add(campaignMetrics.get(IMPRESSIONS));
            }
        } else {
            selections.add(criteriaBuilder.nullLiteral(Long.class));
        }
    }

    private Predicate[] buildConditions(Root<CampaignMetric> campaignMetrics, QueryParameters queryParameters,
                                        CriteriaBuilder criteriaBuilder) {
        List<Predicate> conditions = new ArrayList<>();
        addFilters(queryParameters, conditions, criteriaBuilder, campaignMetrics);

        return conditions.toArray(new Predicate[0]);
    }

    private void addFilters(QueryParameters queryParameters, List<Predicate> conditions,
                            CriteriaBuilder criteriaBuilder, Root<CampaignMetric> campaignMetrics) {

        if (isNull(queryParameters)) {
            return;
        }

        List<String> campaigns = queryParameters.getCampaignFilter();
        List<String> dataSources = queryParameters.getDataSourceFilter();

        addFilter(campaigns, CAMPAIGN, criteriaBuilder, campaignMetrics, conditions);
        addFilter(dataSources, DATA_SOURCE, criteriaBuilder, campaignMetrics, conditions);

        LocalDate dateFrom = queryParameters.getDateFrom();
        if (nonNull(dateFrom)) {
            conditions.add(criteriaBuilder.greaterThanOrEqualTo(campaignMetrics.get(DATE), dateFrom));
        }

        LocalDate dateTo = queryParameters.getDateTo();
        if (nonNull(dateTo)) {
            conditions.add(criteriaBuilder.lessThanOrEqualTo(campaignMetrics.get(DATE), dateTo));
        }
    }

    private void addFilter(List<String> filterValues, String filterName, CriteriaBuilder criteriaBuilder,
                           Root<CampaignMetric> campaignMetrics, List<Predicate> conditions) {

        if (isEmpty(filterValues)) {
            return;
        }

        if (filterValues.size() > 1) {
            CriteriaBuilder.In<String> in = criteriaBuilder.in(campaignMetrics.get(filterName));
            for (String filterValue : filterValues) {
                in.value(filterValue);
            }
            conditions.add(in);
        } else {
            conditions.add(criteriaBuilder.equal(campaignMetrics.get(filterName), filterValues.get(0)));
        }
    }
}
