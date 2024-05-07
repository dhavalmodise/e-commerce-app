package com.learn.paymentservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learn.paymentservice.entities.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
