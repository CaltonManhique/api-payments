package live.easytrainpayments.train_payments.service;

import live.easytrainpayments.train_payments.entity.Payment;

public interface PaymentServiceInterface {

    Payment createPayment(Payment payment);

    String getPaymentId(String encryptedData, double billValue);
}
