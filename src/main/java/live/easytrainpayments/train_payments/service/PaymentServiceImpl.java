package live.easytrainpayments.train_payments.service;

import jakarta.transaction.Transactional;
import live.easytrainpayments.train_payments.entity.Payment;
import live.easytrainpayments.train_payments.exceptions.PaymentBalanceExceptions;
import live.easytrainpayments.train_payments.exceptions.PaymentInvalidCardException;
import live.easytrainpayments.train_payments.repository.PaymentRepo;
import live.easytrainpayments.train_payments.security.PaymentsEncryptConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentServiceInterface{

    private PaymentRepo paymentRepo;
    private PaymentsEncryptConfig securityConfig;

    @Autowired
    public PaymentServiceImpl(PaymentRepo paymentRepo, PaymentsEncryptConfig securityConfig) {
        this.paymentRepo = paymentRepo;
        this.securityConfig = securityConfig;
    }

    @Override
    @Transactional
    public Payment createPayment(Payment payment) {


        String encrypted = null;

            encrypted = securityConfig.encrypt(payment.getCardNumber() + payment.getExpiryDate()
                    + payment.getCvc(), "Eisenbahn");

        payment.setEncryptedData(encrypted);
        paymentRepo.save(payment);

        return payment;
    }

    @Override
    public String getPaymentId(String encryptedData, double billValue) {

        Payment payment = paymentRepo.findByEncryptedData(encryptedData);

        String dec = securityConfig.decrypt(encryptedData, "EasyTrain");
            System.out.println(dec + " : data");


        if(payment == null) {
           // throw new PaymentInvalidCardException("Unsuccessful: Card is not valid");
            return  "Unsuccessful: Card is not valid";
        }

        if(payment.getBalance() < billValue){
//            throw new PaymentBalanceExceptions("Unsuccessful: Insufficient balance");
           return  "Unsuccessful: Insufficient balance";
        }

        payment.setBalance(payment.getBalance() - billValue);
        paymentRepo.save(payment);

        return "success";
    }
}
