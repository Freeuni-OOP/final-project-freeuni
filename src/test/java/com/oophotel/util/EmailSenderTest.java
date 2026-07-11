package com.oophotel.util;

import jakarta.mail.Message;
import jakarta.mail.Transport;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

class EmailSenderTest {

    @Test
    void sendPassesSubjectAndBodyIntoMessage() throws Exception {
        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);

        try (MockedStatic<Transport> transport = mockStatic(Transport.class)) {
            EmailSender.send("Hello", "Body text");

            transport.verify(() -> Transport.send(captor.capture()));
        }

        Message sent = captor.getValue();
        assertEquals("Hello", sent.getSubject());
        assertTrue(sent.getContent().toString().contains("Body text"));
        assertEquals("sender@example.com", sent.getFrom()[0].toString());
        assertEquals("recipient@example.com", sent.getAllRecipients()[0].toString());
    }
}
