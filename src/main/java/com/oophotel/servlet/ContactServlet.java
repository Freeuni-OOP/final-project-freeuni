package com.oophotel.servlet;

import com.oophotel.util.EmailSender;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/api/contact")
public class ContactServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");

        if (isBlank(name) || isBlank(email) || isBlank(subject) || isBlank(message)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("{\"error\":\"Missing required fields\"}");
            return;
        }

        if (!email.contains("@")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("{\"error\":\"Invalid email address\"}");
            return;
        }

        if (isBlank(phone)) {
            phone = "—";
        }

        String emailSubject = "Contact Form Message: " + subject;

        String body = "Name: " + name + "\n";
        body += "Email: " + email + "\n";
        body += "Phone: " + phone + "\n\n";
        body += "Message: \n" + message;

        try {
            EmailSender.send(emailSubject, body);

            response.getWriter().print("{\"success\":true}");
        } catch (MessagingException e) {
            e.printStackTrace();

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            response.getWriter().print("{\"error\":\"Failed to send email\"}");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}