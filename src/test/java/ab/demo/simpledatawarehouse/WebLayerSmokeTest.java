package ab.demo.simpledatawarehouse;

import ab.demo.simpledatawarehouse.service.FileService;
import ab.demo.simpledatawarehouse.service.MetricsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class WebLayerSmokeTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MetricsService metricsService;

    @MockBean
    private FileService fileService;

    @Test
    public void whenGetAllMetrics_thenStatus200() throws Exception {
        mvc.perform(get("/metrics/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenUploadNoFile_thenStatus400() throws Exception {
        mvc.perform(post("/upload"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}
