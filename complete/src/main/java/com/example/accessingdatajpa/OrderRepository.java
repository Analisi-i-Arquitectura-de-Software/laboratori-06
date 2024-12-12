package com.example.accessingdatajpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

	List<Order> findByCustomer(Customer customer);

	Order findOneById(long id);

}
