package com.lotto.domain.numbergenerator;

import com.lotto.domain.drowdate.DrawDateFacade;
import com.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NumberGeneratorFacadeTest {

    private final WinningNumbersRepository winningNumbersRepository = new InMemoryWinningNumbersRepositoryTestImpl();
    DrawDateFacade drawDateFacade = mock(DrawDateFacade.class);

    NumberGeneratorFacade numberGeneratorFacade = new NumberGeneratorConfiguration().numberGeneratorFacadeForTest(
            new NumberGeneratorTestImp(),
            drawDateFacade,
            winningNumbersRepository
    );


    @Test
    void should_return_six_numbers() {
        // given
        LocalDateTime drawDate = LocalDateTime.of(2025, 10, 25, 12, 0);
        when(drawDateFacade.getNextDrawDate()).thenReturn(drawDate);
        // when
        WinningNumbersDto generatedNumbers = numberGeneratorFacade.generateWinningNumbers();
        // then
        assertThat(generatedNumbers.winningNumbers()).hasSize(6);
        assertThat(generatedNumbers.winningNumbers()).containsExactlyInAnyOrder(1, 2, 3, 4, 5, 6);
    }

    @Test
    void should_return_six_random_numbers() {
        // given
        LocalDateTime drawDate = LocalDateTime.of(2025, 10, 25, 12, 0);
        when(drawDateFacade.getNextDrawDate()).thenReturn(drawDate);
        // when
        NumberGeneratorFacade numberGeneratorFacade = new NumberGeneratorConfiguration().numberGeneratorFacadeForTest(
                new NumberGeneratorTestImp(),
                drawDateFacade,
                winningNumbersRepository
        );
        WinningNumbersDto generatedNumbers = numberGeneratorFacade.generateWinningNumbers();
        // then
        assertThat(generatedNumbers.winningNumbers()).hasSize(6);
    }


    @Test
    public void should_throw_an_exception_when_number_not_in_range() {
        //given
        Set<Integer> numbersOutOfRange = Set.of(1, 2, 3, 4, 5, 100);
        RandomNumberGenerable generator = new NumberGeneratorTestImp(numbersOutOfRange);
        InMemoryWinningNumbersRepositoryTestImpl repositoryTest = new InMemoryWinningNumbersRepositoryTestImpl();
        //when
        //then
        NumberGeneratorFacade numberGeneratorFacade = new NumberGeneratorConfiguration().numberGeneratorFacadeForTest(
                generator,
                drawDateFacade,
                repositoryTest
        );
        assertThrows(IllegalStateException.class, numberGeneratorFacade::generateWinningNumbers, "Number out of range!");
    }

    @Test
    public void should_return_win_number_when_number_in_range() {
        // given
        LocalDateTime drawDate = LocalDateTime.of(2025, 10, 25, 12, 0);
        when(drawDateFacade.getNextDrawDate()).thenReturn(drawDate);
        // when
        WinningNumbersDto generatedNumbers = numberGeneratorFacade.generateWinningNumbers();
        //then
        assertThat(generatedNumbers.winningNumbers())
                .containsExactlyInAnyOrder(1, 2, 3, 4, 5, 6);
    }

    @Test
    public void should_return_win_number_by_date() {
        // given
        LocalDateTime dateTime = LocalDateTime.of(2025, 10, 25, 12, 0);
        InMemoryWinningNumbersRepositoryTestImpl repo = new InMemoryWinningNumbersRepositoryTestImpl();
        WinningNumbers winningNumbers = new WinningNumbers(null, Set.of(10, 20, 30, 40, 50, 60), dateTime);
        repo.save(winningNumbers);

        RandomNumberGenerable generator = new NumberGeneratorTestImp();
        NumberGeneratorFacade numberGeneratorFacade = new NumberGeneratorConfiguration().numberGeneratorFacadeForTest(
                generator,
                drawDateFacade,
                repo
        );
        // when
        WinningNumbersDto dto = numberGeneratorFacade.retrieveWinningNumberByDate(dateTime);
        // then
        assertThat(dto.date()).isEqualTo(dateTime);
        assertThat(dto.winningNumbers()).containsExactlyInAnyOrder(10, 20, 30, 40, 50, 60);
    }

    @Test
    void should_throw_an_exception_when_by_data_not_winners_numbers() {
        // given
        LocalDateTime dateTime = LocalDateTime.of(2025, 10, 25, 12, 0);

        InMemoryWinningNumbersRepositoryTestImpl repo = new InMemoryWinningNumbersRepositoryTestImpl();

        RandomNumberGenerable generator = new NumberGeneratorTestImp(Set.of(1, 2, 3, 4, 5, 6));
        // when / then
        NumberGeneratorFacade numberGeneratorFacade = new NumberGeneratorConfiguration().numberGeneratorFacadeForTest(
                generator,
                drawDateFacade,
                repo
        );
        assertThrows(WinningNumbersNotFoundException.class, () -> numberGeneratorFacade.retrieveWinningNumberByDate(dateTime));
    }
}