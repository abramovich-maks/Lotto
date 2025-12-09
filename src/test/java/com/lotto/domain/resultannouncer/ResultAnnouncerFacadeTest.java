package com.lotto.domain.resultannouncer;

import com.lotto.domain.AdjustableClock;
import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import com.lotto.domain.numberreceiver.dto.TicketIsExistDto;
import com.lotto.domain.resultannouncer.dto.ResponseDto;
import com.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import com.lotto.domain.resultchecker.ResultCheckerFacade;
import com.lotto.domain.resultchecker.dto.TicketResultDto;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResultAnnouncerFacadeTest {

    ResultCheckerFacade resultCheckerFacade = mock(ResultCheckerFacade.class);
    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);

    ResultAnnouncerFacade resultAnnouncerFacade = ResultAnnouncerConfiguration.resultAnnouncerFacade(
            resultCheckerFacade,
            new InMemoryResultAnnouncerTestImpl(),
            numberReceiverFacade,
            Clock.systemUTC()
    );

    private static final ZoneId POLAND = ZoneId.of("Europe/Warsaw");
    ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.systemDefault());
    AdjustableClock adjustableClock = new AdjustableClock(zonedDateTime.toInstant(), POLAND);

    @Test
    public void should_return_response_when_hash_is_null_or_blank() {
        // given
        // when
        ResultAnnouncerResponseDto checkResultWhereNull = resultAnnouncerFacade.checkResult(null);
        ResultAnnouncerResponseDto checkResultWhereBlank = resultAnnouncerFacade.checkResult("");
        // then
        assertThat(checkResultWhereNull.message()).isEqualTo("Invalid hash");
        assertThat(checkResultWhereBlank.message()).isEqualTo("Invalid hash");
        assertThat(checkResultWhereBlank.responseDto()).isNull();
        assertThat(checkResultWhereNull.responseDto()).isNull();
    }

    @Test
    public void should_return_response_were_hash_does_not_exist_to_db_yet() {
        //given
        String hash = "hash-111";
        when(resultCheckerFacade.findByTicketId(hash)).thenReturn(null);
        //when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);
        //then
        ResultAnnouncerResponseDto result = new ResultAnnouncerResponseDto(null, "Hash does not exist");
        assertThat(resultAnnouncerResponseDto).isEqualTo(result);
    }

    @Test
    public void should_return_response_where_hash_has_already_saved_to_db() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2025, 10, 25, 12, 0);
        String hash = "hash-111";
        TicketResultDto resultDto = TicketResultDto.builder()
                .hash(hash)
                .drawDate(drawDate)
                .userNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .winningNumbers(Set.of(1, 2, 3, 4, 9, 10))
                .build();

        ResponseDto responseDto = ResponseDto.builder()
                .hash(hash)
                .drawDate(drawDate)
                .userNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .winningNumbers(Set.of(1, 2, 3, 4, 9, 10))
                .build();

        when(resultCheckerFacade.findByTicketId(hash)).thenReturn(resultDto);
        //when
        resultAnnouncerFacade.checkResult(hash);
        ResultAnnouncerResponseDto resultAnnouncerResponseDto2 = resultAnnouncerFacade.checkResult(hash);
        //then
        ResultAnnouncerResponseDto result = new ResultAnnouncerResponseDto(responseDto, "Result retrieved from cache");
        assertThat(resultAnnouncerResponseDto2).isEqualTo(result);
    }

    @Test
    public void should_return_response_where_ticket_has_matched_at_1_to_6_numbers() {
        // given
        LocalDateTime drawDate = LocalDateTime.of(2025, 10, 25, 12, 0);

        String hash0 = "hash-000";
        String hash1 = "hash-111";
        String hash2 = "hash-222";
        String hash3 = "hash-333";
        String hash4 = "hash-444";
        String hash5 = "hash-555";
        String hash6 = "hash-666";

        TicketResultDto resultDto0 = TicketResultDto.builder()
                .hash(hash0)
                .drawDate(drawDate)
                .userNumbers(Set.of(7, 8, 9, 10, 11, 12))
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .matches(0)
                .resultCategory("NO_MATCH")
                .build();

        TicketResultDto resultDto1 = TicketResultDto.builder()
                .hash(hash1)
                .drawDate(drawDate)
                .userNumbers(Set.of(6, 7, 8, 9, 10, 11))
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .matches(1)
                .resultCategory("ONE_MATCH")
                .build();

        TicketResultDto resultDto2 = TicketResultDto.builder()
                .hash(hash2)
                .drawDate(drawDate)
                .userNumbers(Set.of(5, 6, 7, 8, 9, 10))
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .matches(2)
                .resultCategory("TWO_MATCH")
                .build();

        TicketResultDto resultDto3 = TicketResultDto.builder()
                .hash(hash3)
                .drawDate(drawDate)
                .userNumbers(Set.of(4, 5, 6, 7, 8, 9))
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .matches(3)
                .resultCategory("THREE_MATCH")
                .build();

        TicketResultDto resultDto4 = TicketResultDto.builder()
                .hash(hash4)
                .drawDate(drawDate)
                .userNumbers(Set.of(3, 4, 5, 6, 7, 8))
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .matches(4)
                .resultCategory("FOUR_MATCH")
                .build();

        TicketResultDto resultDto5 = TicketResultDto.builder()
                .hash(hash5)
                .drawDate(drawDate)
                .userNumbers(Set.of(2, 3, 4, 5, 6, 7))
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .matches(5)
                .resultCategory("FIVE_MATCH")
                .build();

        TicketResultDto resultDto6 = TicketResultDto.builder()
                .hash(hash6)
                .drawDate(drawDate)
                .userNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .matches(6)
                .resultCategory("JACKPOT")
                .build();

        when(resultCheckerFacade.findByTicketId(hash0)).thenReturn(resultDto0);
        when(resultCheckerFacade.findByTicketId(hash1)).thenReturn(resultDto1);
        when(resultCheckerFacade.findByTicketId(hash2)).thenReturn(resultDto2);
        when(resultCheckerFacade.findByTicketId(hash3)).thenReturn(resultDto3);
        when(resultCheckerFacade.findByTicketId(hash4)).thenReturn(resultDto4);
        when(resultCheckerFacade.findByTicketId(hash5)).thenReturn(resultDto5);
        when(resultCheckerFacade.findByTicketId(hash6)).thenReturn(resultDto6);

        ResultAnnouncerResponseDto r0 = resultAnnouncerFacade.checkResult(hash0);
        ResultAnnouncerResponseDto r1 = resultAnnouncerFacade.checkResult(hash1);
        ResultAnnouncerResponseDto r2 = resultAnnouncerFacade.checkResult(hash2);
        ResultAnnouncerResponseDto r3 = resultAnnouncerFacade.checkResult(hash3);
        ResultAnnouncerResponseDto r4 = resultAnnouncerFacade.checkResult(hash4);
        ResultAnnouncerResponseDto r5 = resultAnnouncerFacade.checkResult(hash5);
        ResultAnnouncerResponseDto r6 = resultAnnouncerFacade.checkResult(hash6);

        // then
        assertThat(r0.responseDto().resultCategory()).isEqualTo("NO_MATCH");
        assertThat(r1.responseDto().resultCategory()).isEqualTo("ONE_MATCH");
        assertThat(r2.responseDto().resultCategory()).isEqualTo("TWO_MATCH");
        assertThat(r3.responseDto().resultCategory()).isEqualTo("THREE_MATCH");
        assertThat(r4.responseDto().resultCategory()).isEqualTo("FOUR_MATCH");
        assertThat(r5.responseDto().resultCategory()).isEqualTo("FIVE_MATCH");
        assertThat(r6.responseDto().resultCategory()).isEqualTo("JACKPOT");
    }

    @Test
    public void should_return_response_when_chek_ticket_after_drow_date() {
        // given
        LocalDateTime drawDate = LocalDateTime.now(adjustableClock).plusDays(1);
        String hash = "hash-111";

        TicketResultDto resultDto = TicketResultDto.builder()
                .hash(hash)
                .drawDate(drawDate)
                .build();

        TicketIsExistDto ticketIsExistDto = TicketIsExistDto.builder()
                .hash(resultDto.hash())
                .drawDate(resultDto.drawDate())
                .build();
        when(numberReceiverFacade.existsByHash(hash)).thenReturn(true);
        when(numberReceiverFacade.findByHash(hash)).thenReturn(ticketIsExistDto);
        // when
        ResultAnnouncerResponseDto resultIsAfterDrowDate = resultAnnouncerFacade.checkResult(hash);
        // then
        assertThat(resultIsAfterDrowDate.message()).isEqualTo("Ticket with hash hash-111 has not been drawn yet. Draw date is " + drawDate);
        assertThat(resultIsAfterDrowDate.responseDto()).isNull();
    }
}