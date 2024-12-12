package com.example.accessingdatajpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OneToManyTests {

    @Autowired
    private CustomerRepository customers;

    @Autowired
    private OrderRepository orders;


    @Test
    public void testFindOrderByCustomer() {
        Customer customer = new Customer("first", "last");
        Order order = new Order(new BigDecimal("15.75"));
        customer.addOrder(order);
        customer = customers.save(customer);

        List<Order> foundOrders = orders.findByCustomer(customer);

        assertThat(foundOrders).hasSize(1);
        assertThat(foundOrders.get(0).getAmount()).isEqualTo(new BigDecimal("15.75"));
    }

    @Test
    public void testOrphanRemovalAll() {
        Customer customer = new Customer("first", "last");
        Order order = new Order(new BigDecimal("15.75"));
        customer.addOrder(order);
        customer = customers.save(customer);

        customer.getOrders().clear();
        customers.save(customer);

        List<Order> foundOrder = orders.findByCustomer(customer);
        assertThat(foundOrder).hasSize(0);
    }

    @Test
    public void testOrphanRemovalOne() {
        Customer customer = new Customer("first", "last");
        Order order1 = new Order(new BigDecimal("15.75"));
        Order order2 = new Order(new BigDecimal("45.22"));
        customer.addOrder(order1);
        customer.addOrder(order2);
        customer = customers.save(customer);

        customer.removeOrder(customer.getOrders().stream().findFirst().orElseThrow());
        customers.save(customer);

        List<Order> foundOrder = orders.findByCustomer(customer);
        assertThat(foundOrder).hasSize(1);
    }

    @Test
    public void testDeleteCascade() {
        Customer customer = new Customer("first", "last");
        Order order = new Order(new BigDecimal("15.75"));
        customer.addOrder(order);
        customer = customers.save(customer);

        customers.delete(customer);

        Iterable<Order> foundOrders = orders.findAll();
        assertThat(StreamSupport.stream(foundOrders.spliterator(), false).count()).isEqualTo(0);
    }

}
