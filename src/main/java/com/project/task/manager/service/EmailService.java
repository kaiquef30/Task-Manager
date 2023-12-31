package com.project.task.manager.service;


import com.project.task.manager.exception.EmailFailureException;
import com.project.task.manager.model.User;
import com.project.task.manager.model.VerificationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@CacheConfig(cacheNames = "emailCache")
public class EmailService {

    @Value("${email.from}")
    private String fromAddress;

    @Value("${app.frontend.url}")
    private String url;

    private final JavaMailSender javaMailSender;


    private SimpleMailMessage makeMailMessage() {
        var simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromAddress);
        return simpleMailMessage;
    }


    @Cacheable(key = "'verificationEmail_' + #verificationToken.token", unless = "#result == null")
    public void sendVerificationEmail(VerificationToken verificationToken) throws EmailFailureException {
        SimpleMailMessage message = makeMailMessage();
        message.setTo(verificationToken.getUser().getEmail());
        message.setSubject("Verify your email to active your account.");
        message.setText("Please follow the link below to verify your email to active your account.\n" +
                url + "/auth/verify?token=" + verificationToken.getToken());

        try {
            javaMailSender.send(message);
        } catch (MailException ex) {
            throw new EmailFailureException();
        }
    }

    @Cacheable(key = "'resetPasswordEmail_' + #user.id", unless = "#result == null")
    public void sendResetPasswordEmail(User user, String token) throws EmailFailureException{
        SimpleMailMessage message = makeMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Your password reset request link");
        message.setText("You requested a password reset on our website. Please " +
                "find the link to be able to reset your password.\n" +
                url + "/auth/reset?token=" + token);
        try {
            javaMailSender.send(message);
        } catch (MailException e){
            throw new EmailFailureException();
        }
    }

}
