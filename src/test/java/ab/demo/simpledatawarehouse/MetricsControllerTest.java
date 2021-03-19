package ab.demo.simpledatawarehouse;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ab.demo.simpledatawarehouse.controller.MetricsController;
import ab.demo.simpledatawarehouse.model.CampaignMetric;
import ab.demo.simpledatawarehouse.model.QueryParameters;
import ab.demo.simpledatawarehouse.service.MetricsService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(MetricsController.class)
public class MetricsControllerTest {

    private static final String CAMPAINGN = "cmpgn";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MetricsService service;

    @Test
    public void shouldReturnListFromService() throws Exception {

        when(service.getByQuery(any(QueryParameters.class))).thenReturn(getResultList());

        this.mockMvc.perform(get("/metrics/query"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(CAMPAINGN)));
    }

    private List<CampaignMetric> getResultList() {
        CampaignMetric campaignMetric = CampaignMetric.builder().campaign(CAMPAINGN).clicks(5L).build();
        return Arrays.asList(campaignMetric);
    }
}
