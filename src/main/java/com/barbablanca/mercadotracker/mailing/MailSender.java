package com.barbablanca.mercadotracker.mailing;

import com.barbablanca.mercadotracker.products.ProductEntity;
import com.barbablanca.mercadotracker.users.UserEntity;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MailSender {

    Logger log = LoggerFactory.getLogger(MailSender.class);

    private final PendingVerificationsRepository pendingVerificationsRepository;
    private final SendGrid sendGrid;

    public MailSender(PendingVerificationsRepository pendingVerificationsRepository) {
        this.pendingVerificationsRepository = pendingVerificationsRepository;
        this.sendGrid = new SendGrid(System.getenv("SENDGRID_API_KEY"));
    }

    public void sendVerificationMail(String to, Integer userId) throws IOException {
        Mail mail = new Mail();
        Personalization toPersonalization = new Personalization();

        Email from = new Email("mercado.tracker@noresponder.com");
        Email _to = new Email(to);

        mail.setFrom(from);
        toPersonalization.addTo(_to);

        PendingVerificationEntity pendingVerification = new PendingVerificationEntity(userId);
        pendingVerification = pendingVerificationsRepository.save(pendingVerification);

        toPersonalization.addDynamicTemplateData("verificationUrl",
                "https://mercadotracker.herokuapp.com/verify/"+ pendingVerification.getToken());

        mail.addPersonalization(toPersonalization);

        mail.setTemplateId("d-96931d16786a4639aeca9e231d299821");

        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sendGrid.api(request);

        log.info("Sended email response status: "+ response.getStatusCode());
        log.info(response.getBody());
    }

    public void sendNotification(UserEntity to, ProductEntity product) throws IOException {
        if (!to.getVerified()) {
            log.info("El usuario"+ to.getId() +" no está verificado, no se enviará la notificacion");
            return;
        }

        Mail mail = new Mail();
        Personalization toPersonalization = new Personalization();

        Email from = new Email("mercado.tracker@noresponder.com");
        Email _to = new Email(to.getEmail());

        mail.setFrom(from);
        toPersonalization.addTo(_to);

        String title;
        if (product.getCurPrice().getCurrency()
                .equals(product.getPrevPrice().getCurrency())) {
            if (product.getCurPrice().getAmount() > product.getPrevPrice().getAmount()) {
                title = "Tu producto subió de precio";
            }
            else {
                title = "Tu producto bajó de precio";
            }
        }
        else {
            title = "La moneda de tu producto cambio";
        }

        toPersonalization.addDynamicTemplateData("title", title);
        toPersonalization.addDynamicTemplateData("productUrl", product.getUrl());
        toPersonalization.addDynamicTemplateData("productName", product.getName());
        toPersonalization.addDynamicTemplateData("productImgUrl", product.getImgUrl());
        toPersonalization.addDynamicTemplateData("productPrevPrice", product.getPrevPrice().getAmount());
        toPersonalization.addDynamicTemplateData("productPrevCurrency", product.getPrevPrice().getCurrency());
        toPersonalization.addDynamicTemplateData("productCurPrice", product.getCurPrice().getAmount());
        toPersonalization.addDynamicTemplateData("productCurCurrency", product.getCurPrice().getCurrency());

        mail.addPersonalization(toPersonalization);

        mail.setTemplateId("d-631d60b9519b4a049b5735f7d2c569cf");

        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sendGrid.api(request);

        log.info("Send email response status: "+ response.getStatusCode());
        log.info(response.getBody());
    }


    public void sendPasswordResetCode(UserEntity to, Integer code) throws IOException {
        if (!to.getVerified()) {
            log.info("El usuario"+ to.getId() +" no está verificado, no se enviará la noctificacion");
            return;
        }

        Mail mail = new Mail();
        Personalization toPersonalization = new Personalization();

        Email from = new Email("mercado.tracker@noresponder.com");
        Email _to = new Email(to.getEmail());

        mail.setFrom(from);
        toPersonalization.addTo(_to);

        toPersonalization.addDynamicTemplateData("code", code);

        mail.addPersonalization(toPersonalization);
        mail.setTemplateId("d-204502397b23415d8438427b8ec7a54a");

        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sendGrid.api(request);

        log.info("Send email response status: "+ response.getStatusCode());
        log.info(response.getBody());
    }
}
