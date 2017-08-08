package com.codecool.services.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Slf4j
@Service
@EnableAutoConfiguration
@Transactional
public class EmailService {
    private final String fromAddress = "fromaddress@mukodik.com";
    private JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmailWithPDf(String address, String offerId, File pdf) throws MessagingException {
        log.debug("Sending email...");
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromAddress);
            helper.setTo(address);
            helper.setSubject("Árajánlat-A Napos oldal_" + offerId);
            helper.setText("<html><body>Tisztelt Érdeklődő!<br><br> Azért kapta ezt az e-mailt, mert árajánlatot kért a <a href='http://localhost:8080/'>www.naposoldal.hu</a> oldalon. Az ajánlatot a csatolmányban találja meg.<br> Amennyiben felkeltettük az érdeklődését,felveheti velünk a kapcsolatot az ajánlatban megadott elérhetőségeken. <br><br> Üdvözlettel, <br><br> A Napos Oldal csapat</body></html>", true);
            helper.addAttachment(pdf.getName(), pdf);
            javaMailSender.send(message);
            log.debug("Email sent...");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
