package com.lotto.domain.loginandregister;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;

@Component
@AllArgsConstructor
@Log4j2
public class UserConformer {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final SpringTemplateEngine emailTemplateEngine;


    @Async
    public void sendConfirmationEmail(User user) {
        try {
            String to = user.getEmail();
            String name = user.getEmail();
            String token = user.getConfirmationToken();

//            String confirmUrl = "https://localhost:8080/confirm?token=" + token;
            String confirmUrl = "https://ec2-3-122-255-213.eu-central-1.compute.amazonaws.com:8000/confirm?token=" + token;

            Context ctx = new Context();
            ctx.setVariable("name", name);
            ctx.setVariable("confirmUrl", confirmUrl);

            String html = emailTemplateEngine.process("confirmation-email", ctx);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Confirm your Lotto registration");
            helper.setText(html, true);

            mailSender.send(mimeMessage);
        } catch (Exception ex) {
            log.error("Failed to send confirmation email to {}: {}", user.getEmail(), ex.getMessage(), ex);
        }
    }

    public boolean confirmUser(String confirmationToken) {
        User user = userRepository.findByConfirmationToken(confirmationToken)
                .orElseThrow(() -> new RuntimeException("User not found"));
        boolean changed = user.confirm();
        if (changed) {
            log.info("Confirm user {} by token: {}", user.getEmail(), confirmationToken);
            userRepository.save(user);
        }
        return changed;
    }
}

