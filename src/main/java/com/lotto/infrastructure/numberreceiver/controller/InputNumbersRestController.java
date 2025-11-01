package com.lotto.infrastructure.numberreceiver.controller;

import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import com.lotto.domain.numberreceiver.dto.InputNumberResultDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@AllArgsConstructor
class InputNumbersRestController {

    private final NumberReceiverFacade numberReceiverFacade;

    @PostMapping("/inputNumbers")
    public ResponseEntity<InputNumberResultDto> inputNumbers(@RequestBody InputNumbersRequestDto dto) {
        Set<Integer> collect = new HashSet<>(dto.inputNumbers());
        InputNumberResultDto inputNumberResultDto = numberReceiverFacade.inputNumbers(collect);
        return ResponseEntity.ok(inputNumberResultDto);
    }
}
