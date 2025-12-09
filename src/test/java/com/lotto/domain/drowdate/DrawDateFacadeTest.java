package com.lotto.domain.drowdate;

import com.lotto.domain.AdjustableClock;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DrawDateFacadeTest {

    private static final ZoneId POLAND = ZoneId.of("Europe/Warsaw");
    ZonedDateTime zonedDateTime = ZonedDateTime.of(2025, 10, 23, 12, 0, 0, 0, POLAND);
    AdjustableClock clock = new AdjustableClock(zonedDateTime.toInstant(), POLAND);
    DrawDateFacade drawDateFacade = new DrawDateConfiguration().drawDateFacadeForTest(clock);

    @Test
    public void should_return_correct_draw_date() {
        //given
        //when
        LocalDateTime localDateTime = drawDateFacade.getNextDrawDate();
        //then
        LocalDateTime expectedDate = LocalDateTime.of(2025, 10, 25, 12, 0, 0);
        assertThat(localDateTime).isEqualTo(expectedDate);
    }

    @Test
    public void should_return_next_Saturday_when_date_is_Saturday_noon() {
        //given
        clock.setClockToLocalDateTime(LocalDateTime.of(2025, 10, 25, 12, 00));
        //when
        LocalDateTime localDateTime = drawDateFacade.getNextDrawDate();
        //then
        LocalDateTime expectedDate = LocalDateTime.of(2025, 11, 01, 12, 0, 0);
        assertThat(localDateTime).isEqualTo(expectedDate);
    }

    @Test
    public void should_return_next_Saturday_when_date_is_Saturday_afternoon() {
        //given
        clock.setClockToLocalDateTime(LocalDateTime.of(2025, 10, 25, 16, 00));
        //when
        LocalDateTime localDateTime = drawDateFacade.getNextDrawDate();
        //then
        LocalDateTime expectedDate = LocalDateTime.of(2025, 11, 01, 12, 0, 0);
        assertThat(localDateTime).isEqualTo(expectedDate);
    }
}