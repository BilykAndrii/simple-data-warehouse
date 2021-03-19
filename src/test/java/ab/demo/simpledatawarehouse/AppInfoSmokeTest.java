package ab.demo.simpledatawarehouse;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AppInfoSmokeTest {

    private static final String APP_NAME = "simple-data-warehouse";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void infoShouldContainAppName() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api/info",
                String.class)).contains(APP_NAME);
    }
}
