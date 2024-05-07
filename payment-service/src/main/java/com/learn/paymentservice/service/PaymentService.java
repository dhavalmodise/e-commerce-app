package com.learn.paymentservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.paymentservice.entities.Payment;
import com.learn.paymentservice.repositories.PaymentRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;

	public List<Payment> getAllPayments() {
		return paymentRepository.findAll();
	}

	public Optional<Payment> getPaymentById(Long id) {
		Optional<Payment> optionalPayment = paymentRepository.findById(id);
		if(optionalPayment.isPresent()) {
			return optionalPayment;
		}else {
			throw new EntityNotFoundException("Payment with id " + id + " not found");
		}
	}

	public Payment createPayment(Payment payment) {
		payment.setCreatedAt(LocalDateTime.now());
		payment.setUpdatedAt(LocalDateTime.now());
		return paymentRepository.save(payment);
	}

	public Payment updatePayment(Long id, Payment paymentDetails) {
		Optional<Payment> optionalPayment = paymentRepository.findById(id);
		if (optionalPayment.isPresent()) {
			Payment payment = optionalPayment.get();
			payment.setOrderId(paymentDetails.getOrderId());
			payment.setAmount(paymentDetails.getAmount());
			payment.setPaymentMethod(paymentDetails.getPaymentMethod());
			payment.setStatus(paymentDetails.getStatus());
			payment.setUpdatedAt(LocalDateTime.now());
			return paymentRepository.save(payment);
		} else {
			throw new EntityNotFoundException("Payment with id " + id + " not found");
		}
	}

	public void deletePayment(Long id) {
		paymentRepository.deleteById(id);
	}
}
