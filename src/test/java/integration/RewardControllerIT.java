package integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.infogain.Main;
import org.infogain.application.request.TransactionRequest;
import org.infogain.application.rest.ApiPaths;
import org.infogain.domain.transaction.model.Transaction;
import org.infogain.domain.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Main.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application-test.yaml")
class RewardControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void shouldReturn200_whenGettingRewardDetails() throws Exception {
        // given
        final String userId = "existingUserId";

        // when // then
        mockMvc.perform(
                        get(ApiPaths.REWARD_PATH + "/" + userId)
                )
                .andExpect(status().isOk())
                .andReturn();
    }
}
