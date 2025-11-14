package com.lotto.feature;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.lotto.BaseIntegrationTest;
import com.lotto.IntegrationTestData;
import com.lotto.domain.numbergenerator.NumberGeneratorFacade;
import com.lotto.domain.numbergenerator.WinningNumbersNotFoundException;
import com.lotto.domain.numberreceiver.dto.InputNumberResultDto;
import com.lotto.domain.resultannouncer.TicketListByUser;
import com.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import com.lotto.domain.resultchecker.ResultCheckerFacade;
import com.lotto.domain.resultchecker.TicketNotFoundException;
import com.lotto.infrastructure.loginandregister.controller.dto.JwtResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserPlayedLottoAndWonIntegrationTest extends BaseIntegrationTest implements IntegrationTestData {

    @Autowired
    NumberGeneratorFacade numberGeneratorFacade;

    @Autowired
    ResultCheckerFacade resultCheckerFacade;

    @Test
    public void should_user_win_and_system_should_generate_winners() throws Exception {
        // step 1: external service returns 6 random numbers (1,2,3,4,5,6)
        // given
        wireMockServer.stubFor(WireMock.get("/api/v1.0/random?min=1&max=99&count=25")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [1, 2, 3, 4, 5, 6, 82, 82, 83, 83, 86, 57, 10, 81, 53, 93, 50, 54, 31, 88, 15, 43, 79, 32, 43]
                                """.trim()
                        )));


        // step 2: użytkownik próbuje uzyskać token JWT, wysyłając POST /token z mail=maksim@mail.com i password=12345 and system returned UNAUTHORIZED(401)
        // given && when && then
        mockMvc.perform(post("/token").content(requestBodyLogin())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Bad credentials"))
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"));


        // step 3: użytkownik wysyła POST /register z mail=maksim@mail.com i password=12345;  system rejestruje użytkownika i zwraca CREATED (201)
        // given when && then
        mockMvc.perform(post("/register").content(requestBodyRegister())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("maksim@mail.com"))
                .andExpect(jsonPath("$.message").value("Success. User created."));


        // step 4: użytkownik ponownie próbuje uzyskać token JWT, wysyłając POST /token z mail=maksim@mail.com i password=12345;  system zwraca OK (200) oraz jwtToken="AAAA.BBBB.CCC"
        // given && when && then
        MvcResult mvcResultToken = mockMvc.perform(post("/token").content(requestBodyLogin())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String jsonWithToken = mvcResultToken.getResponse().getContentAsString();
        JwtResponseDto jwtResponseDto = objectMapper.readValue(jsonWithToken, JwtResponseDto.class);
        String token = jwtResponseDto.token();
        assertAll(
                () -> assertThat(jwtResponseDto.login()).isEqualTo("maksim@mail.com"),
                () -> {
                    assertThat(token).matches(Pattern.compile("^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$"));
                }
        );


        // step 5: system fetched winning numbers for draw date: 01.11.2025 12:00
        // given
        LocalDateTime drawDate = LocalDateTime.of(2025, 11, 1, 12, 0, 0);
        // when && then
        await()
                .atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> {
                            try {
                                return !numberGeneratorFacade.retrieveWinningNumberByDate(drawDate).winningNumbers().isEmpty();
                            } catch (WinningNumbersNotFoundException e) {
                                return false;
                            }
                        }
                );


        // step 6: user made POST /inputNumbers with 6 numbers (1, 2, 3, 4, 5, 6) at 29-10-2025 10:00 and system returned OK(200) with message: “success” and Ticket (DrawDate: 01.11.2025 12:00 (Saturday), TicketId: sampleTicketId)
        // given
        // when
        ResultActions perform = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                        "inputNumbers" : [1,2,3,4,5,6]
                        }       
                        """.trim()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token));
        // then
        MvcResult mvcResult = perform.andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        InputNumberResultDto numberReceiverResponseDto = objectMapper.readValue(json, InputNumberResultDto.class);
        String hash = numberReceiverResponseDto.ticketDto().hash();
        assertAll(
                () -> assertThat(numberReceiverResponseDto.ticketDto().drawDate()).isEqualTo(drawDate),
                () -> assertThat(hash).isNotNull(),
                () -> assertThat(numberReceiverResponseDto.message()).isEqualTo("success")
        );


        // step 7: user made GET /results/notExistingId and system returned 404(NOT_FOUND) and body with (message: Ticket with id: notExistingId not found and status NOT_FOUND)
        // given
        // when
        ResultActions performGetResultWithNotExistingId = mockMvc.perform(get("/result/" + "notExistingId")
                .header("Authorization", "Bearer " + token));
        // then
        performGetResultWithNotExistingId.andExpect(status().isNotFound())
                .andExpect(content().json(
                        """
                                {
                                "message" : "Ticket with id: notExistingId not found" ,
                                 "status": "NOT_FOUND"
                                 }
                                """.trim()

                ));


        // step 8: 3 days and 115 minutes passed, and it is 5 minute before draw (01.11.2025 11:55)
        clock.plusDaysAndMinutes(3, 115);


        // step 9: system generated result for TicketId: sampleTicketId with draw date 01.11.2025 12:00, and saved it with 6 hits
        await()
                .atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> {
                            try {
                                return !resultCheckerFacade.findByTicketId(hash).userNumbers().isEmpty();
                            } catch (TicketNotFoundException e) {
                                return false;
                            }
                        }
                );


        // step 10: 6 minutes passed and it is 1 minute after the draw (01.11.2025 12:01)
        clock.plusMinutes(6);


        // step 11: użytkownik wysyła GET /offers bez tokena JWT ; system zwraca UNAUTHORIZED (401)
        // given && when
        ResultActions performGetResultsWithoutToken = mockMvc.perform(get("/result/" + hash));
        // then
        performGetResultsWithoutToken.andExpect(status().isForbidden());


        // step 12: user made GET /results/sampleTicketId and system returned 200 (OK)
        ResultActions performGetResultsWithExistingId = mockMvc.perform(get("/result/" + hash)
                .header("Authorization", "Bearer " + token));
        // then
        MvcResult mvcResultGetMetod = performGetResultsWithExistingId.andExpect(status().isOk()).andReturn();
        String jsonGet = mvcResultGetMetod.getResponse().getContentAsString();
        ResultAnnouncerResponseDto finalResult = objectMapper.readValue(jsonGet, ResultAnnouncerResponseDto.class);
        assertAll(
                () -> assertThat(finalResult.message()).isEqualTo("Result available"),
                () -> assertThat(finalResult.responseDto().hash()).isEqualTo(hash),
                () -> assertThat(finalResult.responseDto().resultCategory()).isEqualTo("JACKPOT")
        );

        // step 13: user made GET /results and system returned 200 (OK)
        // given && when
        ResultActions performGetResults = mockMvc.perform(get("/result")
                .header("Authorization", "Bearer " + token));
        // then
        MvcResult mvcResultGetAllTickets = performGetResults.andExpect(status().isOk()).andReturn();
        String jsonGetAllTickets = mvcResultGetAllTickets.getResponse().getContentAsString();
        List<TicketListByUser> userTickets = objectMapper.readValue(jsonGetAllTickets, new TypeReference<>() {
        });
        TicketListByUser ticket = userTickets.get(0);
        assertAll(
                () -> assertThat(userTickets).hasSize(1),
                () -> assertThat(ticket.hash()).isNotNull(),
                () -> assertThat(ticket.numbers()).isEqualTo(Set.of(1, 2, 3, 4, 5, 6)),
                () -> assertThat(ticket.drawDate()).isEqualTo(drawDate)
        );
    }
}
