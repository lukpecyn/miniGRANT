package pl.lukpecyn.minigrant.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import pl.lukpecyn.minigrant.controllers.SecurityController;

@Component
public class EmailService {
  
	@Value("${spring.mail.from.email}")
	public String from;

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    public JavaMailSender emailSender;
 
	public void sendSimpleEmail(String to, String subject, String content) {
		logger.info("Sending e-mail to " + to +"...");
		SimpleMailMessage message = new SimpleMailMessage(); 
        message.setTo(to); 
        message.setFrom(from);
        message.setSubject(subject); 
        message.setText(content);
        emailSender.send(message);
        logger.info("E-mail sent.");
	}
}