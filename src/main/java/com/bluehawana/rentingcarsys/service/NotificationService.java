package com.bluehawana.rentingcarsys.service;

import com.bluehawana.rentingcarsys.exception.ResourceNotFoundException;
import com.bluehawana.rentingcarsys.model.Booking;
import com.bluehawana.rentingcarsys.model.Payment;
import com.bluehawana.rentingcarsys.model.User;
import com.bluehawana.rentingcarsys.repository.BookingRepository;
import com.bluehawana.rentingcarsys.repository.UserRepository;
import com.bluehawana.rentingcarsys.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public void sendEmailConfirmation(Long bookingId) {
        String subject = "Booking Confirmation";
        String message = "Thank you for booking with us! Your booking ID is " + bookingId;

        String recipientEmail = findUserEmailByBookingId(bookingId);

        sendEmail(recipientEmail, subject, message);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendPaymentConfirmation(Long bookingId) {
        String subject = "Payment Confirmation";
        String message = "Thank you for your payment! Your booking ID is " + bookingId;

        String recipientEmail = findUserEmailByBookingId(bookingId);

        sendEmail(recipientEmail, subject, message);
    }

    private String findUserEmailByBookingId(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        User user = userRepository.findById(booking.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getEmail();
    }

    public void sendBookingConfirmation(Booking booking) {
        String subject = "Booking Confirmation";
        String message = "Thank you for booking with us! Your booking ID is " + booking.getId();

        String recipientEmail = findUserEmailByBookingId(booking.getId());

        sendEmail(recipientEmail, subject, message);
    }

    public void sendBookingCompletionConfirmation(Booking booking) {
        String subject = "Booking Completion Confirmation";
        String message = "Thank you for booking with us! Your booking ID is " + booking.getId();

        String recipientEmail = findUserEmailByBookingId(booking.getId());

        sendEmail(recipientEmail, subject, message);
    }

    public void sendBookingCancellation(Booking booking) {
        String subject = "Booking Cancellation";
        String message = "Your booking with ID " + booking.getId() + " has been cancelled";

        String recipientEmail = findUserEmailByBookingId(booking.getId());

        sendEmail(recipientEmail, subject, message);
    }

    public void sendRefundConfirmation(Payment payment) {
        String subject = "Refund Confirmation";
        String message = "Your refund for payment ID " + payment.getId() + " has been processed";

        String recipientEmail = findUserEmailByPaymentId(String.valueOf(payment.getId()));

        sendEmail(recipientEmail, subject, message);
    }

    private String findUserEmailByPaymentId(String id) {
        Payment payment = (Payment) PaymentRepository.findByPaymentIntentId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        User user = userRepository.findById(payment.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getEmail();
    }
}