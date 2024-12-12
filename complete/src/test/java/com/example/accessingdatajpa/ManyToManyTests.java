package com.example.accessingdatajpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ManyToManyTests {

    @Autowired
    private CustomerRepository customers;

    @Test
    public void testFindCustomerByFriends() {
        customers.saveAll(List.of(
                new Customer("first1", "last1"),
                new Customer("first2", "last2"),
                new Customer("first3", "last3")
        ));

        Customer customer1 = customers.findByLastName("last1").get(0);
        Customer customer3 = customers.findByLastName("last3").get(0);
        customer1.addFriend(customer3);
        customers.save(customer1);

        List<Customer> friendsOf3 = customers.findByFriendsContains(customer3);
        assertThat(friendsOf3).extracting(Customer::getLastName).containsOnly("last1");

        List<Customer> friendsOf1 = customers.findByFriendsContains(customer1);
        assertThat(friendsOf1).isEmpty();
    }

    @Test
    public void testFindCustomerByReciprocalFriends() {
        customers.saveAll(List.of(
                new Customer("first1", "last1"),
                new Customer("first2", "last2"),
                new Customer("first3", "last3")
        ));

        Customer customer1 = customers.findByLastName("last1").get(0);
        Customer customer3 = customers.findByLastName("last3").get(0);
        customer1.addFriend(customer3);
        customer3.addFriend(customer1);
        customers.save(customer1);
        customers.save(customer3);

        List<Customer> friendsOf1 = customers.findByFriendsContains(customer1);
        assertThat(friendsOf1).extracting(Customer::getLastName).containsOnly("last3");

        List<Customer> friendsOf3 = customers.findByFriendsContains(customer3);
        assertThat(friendsOf3).extracting(Customer::getLastName).containsOnly("last1");
    }

}
