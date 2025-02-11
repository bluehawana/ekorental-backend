package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.exception.ResourceNotFoundException;
import com.bluehawana.rentingcarsys.model.Booking;
import com.bluehawana.rentingcarsys.repository.BookingRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final JavaMailSender emailSender;
    private final BookingRepository bookingRepository;

    // Email templates remain the same...
    private static final String PAYMENT_CONFIRMATION_TEMPLATE = """
        Dear %s,
        
        Payment confirmed for booking #%d
        Amount: $%.2f
        Date: %s
        
        Thank you for your payment!
        
        Best regards,
        Your Car Rental Team
        """;

    private static final String BOOKING_CONFIRMATION_TEMPLATE = """
        Dear %s,
        
        Your booking has been confirmed!
        
        Booking Details:
        - Booking ID: %d
        - Car: %s
        - Start Date: %s
        - End Date: %s
        - Total Price: $%.2f
        
        Thank you for choosing our service!
        
        Best regards,
        Your Car Rental Team
        """;

    private static final String BOOKING_CANCELLATION_TEMPLATE = """
        Dear %s,
        
        Your booking #%d has been cancelled.
        
        If you have any questions, please contact our support team.
        
        Best regards,
        Your Car Rental Team
        """;

    private static final String BOOKING_COMPLETION_TEMPLATE = """
        Dear %s,
        
        Your booking #%d has been completed.
        We hope you enjoyed our service!
        
        Best regards,
        Your Car Rental Team
        """;

    public void sendBookingConfirmation(Booking booking) {
        try {
            String emailContent = createBookingConfirmationMessage(booking);
            sendEmail(booking.getUser().getEmail(), "Booking Confirmation - #" + booking.getId(), emailContent);
            log.info("Confirmation email sent for booking: {}", booking.getId());
        } catch (Exception e) {
            log.error("Failed to send confirmation email for booking: {}", booking.getId(), e);
        }
    }

    public void sendPaymentConfirmation(Long bookingId) {
        try {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

            String emailContent = createPaymentConfirmationMessage(booking);
            sendEmail(booking.getUser().getEmail(), "Payment Confirmation", emailContent);
            log.info("Payment confirmation email sent for booking: {}", bookingId);
        } catch (Exception e) {
            log.error("Failed to send payment confirmation email for booking: {}", bookingId, e);
        }
    }

    public void sendBookingCancellation(Booking booking) {
        try {
            String emailContent = createBookingCancellationMessage(booking);
            sendEmail(booking.getUser().getEmail(), "Booking Cancellation", emailContent);
            log.info("Cancellation email sent for booking: {}", booking.getId());
        } catch (Exception e) {
            log.error("Failed to send cancellation email for booking: {}", booking.getId(), e);
        }
    }

    public void sendBookingCompletionConfirmation(Booking booking) {
        try {
            String emailContent = createBookingCompletionMessage(booking);
            sendEmail(booking.getUser().getEmail(), "Booking Completed", emailContent);
            log.info("Completion email sent for booking: {}", booking.getId());
        } catch (Exception e) {
            log.error("Failed to send completion email for booking: {}", booking.getId(), e);
        }
    }

    private String createPaymentConfirmationMessage(Booking booking) {
        return String.format(PAYMENT_CONFIRMATION_TEMPLATE,
                booking.getUser().getName(),
                booking.getId(),
                booking.getTotalPrice(),
                booking.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE)
        );
    }

    private String createBookingConfirmationMessage(Booking booking) {
        return String.format(BOOKING_CONFIRMATION_TEMPLATE,
                booking.getUser().getName(),
                booking.getId(),
                booking.getCar().getModel(),
                booking.getStartTime().format(DateTimeFormatter.ISO_LOCAL_DATE),
                booking.getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE),
                booking.getTotalPrice()
        );
    }

    private String createBookingCancellationMessage(Booking booking) {
        return String.format(BOOKING_CANCELLATION_TEMPLATE,
                booking.getUser().getName(),
                booking.getId()
        );
    }

    private String createBookingCompletionMessage(Booking booking) {
        return String.format(BOOKING_COMPLETION_TEMPLATE,
                booking.getUser().getName(),
                booking.getId()
        );
    }

    private void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            emailSender.send(message);
        } catch (MessagingException e) {
            log.error("Failed to send email: {}", e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}