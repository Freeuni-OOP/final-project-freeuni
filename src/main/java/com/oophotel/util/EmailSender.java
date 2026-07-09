package com.oophotel.util;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailSender {

    public static void send(String subject, String body) throws MessagingException {
        Properties config = new Properties();

        InputStream input =
                EmailSender.class.getClassLoader().getResourceAsStream("mail.properties");

        if(input == null){
            throw new RuntimeException("mail.properties file not found");
        }

        try{
            config.load(input);
            input.close();
        }catch(IOException e){
            throw new RuntimeException("Unable to load mail configuration.", e);
        }

        String username = config.getProperty("mail.username");
        String password = config.getProperty("mail.password");
        String recipient = config.getProperty("mail.to");

        String host = config.getProperty("mail.smtp.host");
        String port = config.getProperty("mail.smtp.port");

        Properties mailProperties = new Properties();

        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.starttls.enable", "true");
        mailProperties.put("mail.smtp.host", host);
        mailProperties.put("mail.smtp.port", port);


        Session session = Session.getInstance(mailProperties,
                new Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));

        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}