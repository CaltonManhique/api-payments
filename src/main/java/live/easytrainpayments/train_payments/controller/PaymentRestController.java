package live.easytrainpayments.train_payments.controller;

import live.easytrainpayments.train_payments.entity.Payment;
import live.easytrainpayments.train_payments.exceptions.PaymentBalanceExceptions;
import live.easytrainpayments.train_payments.exceptions.PaymentExceptions;
import live.easytrainpayments.train_payments.exceptions.PaymentInvalidCardException;
import live.easytrainpayments.train_payments.service.PaymentServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class PaymentRestController {

    private PaymentServiceInterface paymentService;

    @Autowired
    public PaymentRestController(PaymentServiceInterface paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/createCard")
    public Payment createCard(@RequestBody Payment payment) {
        payment.setId(0L);

        return paymentService.createPayment(payment);
    }

    @GetMapping("/pay")
   public String pay(@RequestParam("encryptedData") String encryptedData, @RequestParam("billValue") double billValue) {

        System.out.println(encryptedData);
        return paymentService.getPaymentId(encryptedData, billValue);
    }


    @ExceptionHandler
    public ResponseEntity<PaymentExceptions> handleInvalidExceptions(PaymentInvalidCardException e) {

        PaymentExceptions exception = new PaymentExceptions();
        exception.setStatus(HttpStatus.NOT_FOUND.value());
        exception.setMessage(e.getMessage());
        exception.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<PaymentExceptions> handleBalanceExceptions(PaymentBalanceExceptions e) {

        PaymentExceptions exception = new PaymentExceptions();
        exception.setStatus(HttpStatus.PAYMENT_REQUIRED.value());
        exception.setMessage(e.getMessage());
        exception.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        return new ResponseEntity<>(exception, HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler
    public ResponseEntity<PaymentExceptions> handlerBadReqExceptions(Exception e) {

        PaymentExceptions exception = new PaymentExceptions();
        exception.setStatus(HttpStatus.BAD_REQUEST.value());
        exception.setMessage(e.getMessage());
        exception.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }
}
