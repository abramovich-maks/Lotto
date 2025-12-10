package com.lotto.infrastructure.usercrud;

import com.lotto.domain.loginandregister.UserConformer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping
public class RegisterWebController {

    private final UserConformer userConformer;

    @GetMapping("/confirm")
    public String confirm(@RequestParam String token) {
        boolean isConfirmed = userConformer.confirmUser(token);
        if (isConfirmed) {
            return "redirect:https://ec2-3-122-255-213.eu-central-1.compute.amazonaws.com/login";
        } else {
            return "redirect:https://ec2-3-122-255-213.eu-central-1.compute.amazonaws.com/register";
        }
    }
}