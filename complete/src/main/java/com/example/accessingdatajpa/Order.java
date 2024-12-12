package com.example.accessingdatajpa;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(
			name = "customer_id",
			nullable = false )
	private Customer customer;

	private BigDecimal amount;

	protected Order() {}

	public Order(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return String.format(
				"Order[id=%d, customer='%d', amount='%s']",
				id, customer.getId(), amount.toString());
	}

	public Long getId() {
		return id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public BigDecimal getAmount() {
		return amount;
	}


}
