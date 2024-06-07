package live.easytrainpayments.train_payments.exceptions;

public class PaymentInvalidCardException extends RuntimeException {

    public PaymentInvalidCardException(String message) {
        super(message);
    }
}
