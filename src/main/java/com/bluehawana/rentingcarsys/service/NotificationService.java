package com.bluehawana.rentingcarsys.service;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Value("${mailjet.api.key}")
    private String apiKey;

    @Value("${mailjet.api.secret}")
    private String apiSecret;

    @Value("${mailjet.sender.email}")
    private String senderEmail;

    @Value("${mailjet.sender.name}")
    private String senderName;

    // This is the missing method that your code is trying to call
    public void sendEmail(String to, String toName, String subject, String textContent, String htmlContent) {
        try {
            MailjetClient client = new MailjetClient(apiKey, apiSecret);
            MailjetRequest request = new MailjetRequest(Emailv31.resource)
                    .property(Emailv31.MESSAGES, new JSONArray()
                            .put(new JSONObject()
                                    .put(Emailv31.Message.FROM, new JSONObject()
                                            .put("Email", senderEmail)
                                            .put("Name", senderName))
                                    .put(Emailv31.Message.TO, new JSONArray()
                                            .put(new JSONObject()
                                                    .put("Email", to)
                                                    .put("Name", toName)))
                                    .put(Emailv31.Message.SUBJECT, subject)
                                    .put(Emailv31.Message.TEXTPART, textContent)
                                    .put(Emailv31.Message.HTMLPART, htmlContent)));

            MailjetResponse response = client.post(request);

            if (response.getStatus() != 200) {
                log.error("Failed to send email, status code: {}", response.getStatus());
                throw new RuntimeException("Failed to send email: " + response.getData());
            }

            log.info("Email sent successfully to {}", to);
        } catch (MailjetException e) {
            log.error("Mailjet error sending email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email due to Mailjet error", e);
        } catch (Exception e) {
            log.error("Unexpected error sending email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    // Method to send booking confirmation email
    public void sendBookingConfirmation(String customerEmail, String customerName, Long bookingId,
                                        String startDate, String endDate, String totalPrice,
                                        String carModel) {
        String subject = "Booking Confirmation #" + bookingId;

        String textContent = "Dear " + customerName + ",\n\n" +
                "Your booking has been confirmed. Thank you for choosing our service!\n\n" +
                "Booking details:\n" +
                "Booking ID: " + bookingId + "\n" +
                "Car: " + carModel + "\n" +
                "Start Date: " + startDate + "\n" +
                "End Date: " + endDate + "\n" +
                "Total Price: $" + totalPrice + "\n\n" +
                "If you have any questions, please contact our support team.\n\n" +
                "Best regards,\n" +
                "Car Rental Team";

        String htmlContent = "<h3>Dear " + customerName + ",</h3>" +
                "<p>Your booking has been confirmed. Thank you for choosing our service!</p>" +
                "<h4>Booking details:</h4>" +
                "<ul>" +
                "<li><strong>Booking ID:</strong> " + bookingId + "</li>" +
                "<li><strong>Car:</strong> " + carModel + "</li>" +
                "<li><strong>Start Date:</strong> " + startDate + "</li>" +
                "<li><strong>End Date:</strong> " + endDate + "</li>" +
                "<li><strong>Total Price:</strong> $" + totalPrice + "</li>" +
                "</ul>" +
                "<p>If you have any questions, please contact our support team.</p>" +
                "<p>Best regards,<br>" +
                "Car Rental Team</p>";

        // This calls the sendEmail method above
        sendEmail(customerEmail, customerName, subject, textContent, htmlContent);
    }

    public void sendPaymentConfirmation(Long bookingId) {

    }
}



