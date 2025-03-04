package com.bluehawana.rentingcarsys.repository;

import com.bluehawana.rentingcarsys.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
