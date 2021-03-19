package ab.demo.simpledatawarehouse;

import static org.assertj.core.api.Assertions.assertThat;

import ab.demo.simpledatawarehouse.controller.FileController;
import ab.demo.simpledatawarehouse.controller.InfoController;
import ab.demo.simpledatawarehouse.controller.MetricsController;
import ab.demo.simpledatawarehouse.service.FileService;
import ab.demo.simpledatawarehouse.service.MetricsService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SimpleDataWarehouseApplicationTests {

	@Autowired
	private InfoController infoController;

	@Autowired
	private FileController fileController;

	@Autowired
	private FileService fileService;

	@Autowired
	private MetricsController metricsController;

	@Autowired
	private MetricsService metricsService;

	@Test
	public void contextLoads() {
		assertThat(fileController).isNotNull();
		assertThat(metricsController).isNotNull();
		assertThat(metricsService).isNotNull();
		assertThat(fileService).isNotNull();
	}
}
