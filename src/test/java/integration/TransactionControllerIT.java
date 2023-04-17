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

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Main.class})
@WebAppConfiguration
@TestPropertySource(locations="classpath:application-test.yaml")
class TransactionControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private TransactionRepository transactionRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void shouldReturn201_whenCreatingTransactionIsSuccessful() throws Exception {
        // given
        TransactionRequest request = TransactionRequest.builder()
                .userId("existingUserId")
                .amount(100.0)
                .build();

        // when // then
        mockMvc.perform(
                        post(ApiPaths.TRANSACTION_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void shouldReturn404_whenCreatingTransactionNotFoundUser() throws Exception {
        // given
        TransactionRequest request = TransactionRequest.builder()
                .userId("differentUserId")
                .amount(100.0)
                .build();

        // when // then
        mockMvc.perform(
                        post(ApiPaths.TRANSACTION_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldReturn200_whenUpdatingTransactionIsSuccessful() throws Exception {
        // given
        final String transactionId = UUID.randomUUID().toString();
        final String userId = "existingUserId";

        Transaction transactionToSave = Transaction.builder()
                .transactionId(transactionId)
                .userId(userId)
                .amount(100.0)
                .createdAt(ZonedDateTime.now())
                .build();
        transactionRepository.saveTransaction(transactionToSave);

        TransactionRequest request = TransactionRequest.builder()
                .userId(userId)
                .amount(110.0)
                .build();

        // when // then
        mockMvc.perform(
                        put(ApiPaths.TRANSACTION_PATH + "/" + transactionId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldReturn404_whenUpdatingTransactionNotFound() throws Exception {
        // given
        final String transactionId = UUID.randomUUID().toString();
        final String userId = "existingUserId";

        TransactionRequest request = TransactionRequest.builder()
                .userId(userId)
                .amount(110.0)
                .build();

        // when // then
        mockMvc.perform(
                        put(ApiPaths.TRANSACTION_PATH + "/" + transactionId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
