package live.easytrainpayments.train_payments.repository;

import live.easytrainpayments.train_payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment, Long> {

    Payment findByEncryptedData(String encryptedData);
}
