package ab.demo.simpledatawarehouse;


import ab.demo.simpledatawarehouse.controller.MetricsController;
import ab.demo.simpledatawarehouse.model.CampaignMetric;
import ab.demo.simpledatawarehouse.model.QueryParameters;
import ab.demo.simpledatawarehouse.service.MetricsService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class MetricsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private MetricsController controller;

    @Test
    public void whenGetAllMetrics_thenStatus200() throws Exception {
        mvc.perform(get("/metrics/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void givenQuery_whenGetMetrics_thenStatus200() throws Exception {

        when(controller.getMetricsByQuery(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString()))
                .thenReturn(getResultList());

        mvc.perform(get("/metrics/query"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("cmpgn")));
    }

    private List<CampaignMetric> getResultList() {
        CampaignMetric campaignMetric = new CampaignMetric();
        campaignMetric.setCampaign("cmpgn");
        campaignMetric.setClicks(5L);
        return Arrays.asList(campaignMetric);
    }
}
