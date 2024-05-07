package com.learn.customerservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.customerservice.entities.Customer;
import com.learn.customerservice.repositories.CustomerRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	public Optional<Customer> getCustomerById(Long id) {
		Optional<Customer> optionalCustomer = customerRepository.findById(id);
		if (optionalCustomer.isPresent()) {
			return optionalCustomer;
		} else {
			throw new EntityNotFoundException("Customer with id " + id + " not found");
		}
	}

	public Customer createCustomer(Customer customer) {
		customer.setCreatedAt(LocalDateTime.now());
		customer.setUpdatedAt(LocalDateTime.now());
		return customerRepository.save(customer);
	}

	public Customer updateCustomer(Long id, Customer customerDetails) {
		Optional<Customer> optionalCustomer = customerRepository.findById(id);
		if (optionalCustomer.isPresent()) {
			Customer customer = optionalCustomer.get();
			customer.setFirstName(customerDetails.getFirstName());
			customer.setLastName(customerDetails.getLastName());
			customer.setEmail(customerDetails.getEmail());
			customer.setPhoneNumber(customerDetails.getPhoneNumber());
			customer.setAddress(customerDetails.getAddress());
			customer.setUpdatedAt(LocalDateTime.now());
			return customerRepository.save(customer);
		} else {
			throw new EntityNotFoundException("Customer with id " + id + " not found");
		}
	}

	public void deleteCustomer(Long id) {
		customerRepository.deleteById(id);
	}
}
