package ab.demo.simpledatawarehouse.repo;

import ab.demo.simpledatawarehouse.model.CampaignMetric;
import org.springframework.data.repository.CrudRepository;

public interface CampaignMetricRepository extends CrudRepository<CampaignMetric, Long> {

}
