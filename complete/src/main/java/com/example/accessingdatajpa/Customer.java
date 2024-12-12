package com.example.accessingdatajpa;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customers")
public class Customer {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String firstName;

	private String lastName;

	@OneToMany(
			mappedBy = "customer",
			cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE },
			orphanRemoval = true
	)
	private Set<Order> orders = new HashSet<>();

	@ManyToMany
	private Set<Customer> friends = new HashSet<>();

	protected Customer() {}

	public Customer(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return String.format(
				"Customer[id=%d, firstName='%s', lastName='%s']",
				id, firstName, lastName);
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void addOrder(Order order) {
		order.setCustomer(this);
		this.orders.add(order);
	}

	public void removeOrder(Order order) {
		order.setCustomer(null);
		this.orders.remove(order);
	}

	public void addFriend(Customer customer) {
		this.friends.add(customer);
	}

	public void removeFriend(Customer customer) {
		this.friends.remove(customer);
	}

}
