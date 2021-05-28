package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.apache.catalina.connector.Response;
import org.hibernate.criterion.Order;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private OrderRepository orderRepository = mock(OrderRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);


    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);
        TestUtils.injectObject(orderController, "userRepository", userRepository);
    }

    @Test
    public void create_order_happy_path() {

        String userName = "Leonardo";
        BigDecimal price = BigDecimal.valueOf(50.00);

        User user = new User();
        user.setUsername(userName);
        user.setId(1);

        Item item = new Item();
        item.setId(1L);
        item.setName("something");
        item.setPrice(price);


        Cart cart = new Cart();
        cart.setId(1L);
        cart.addItem(item);
        cart.setUser(user);

        user.setCart(cart);

        when(userRepository.findByUsername("Leonardo")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("Leonardo");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals(user, userOrder.getUser());
        assertEquals(cart.getItems(), userOrder.getItems());
        assertEquals(cart.getTotal(), userOrder.getTotal());

    }

    @Test
    public void create_order_failure() {

        ResponseEntity<UserOrder> response = orderController.submit("Leonardo");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void testUserHistory() {

        String userName = "test";
        BigDecimal price = BigDecimal.valueOf(50.00);

        User user = new User();
        user.setUsername(userName);
        user.setId(0);

        Item item = new Item();
        item.setId(0L);
        item.setName("something");
        item.setPrice(price);


        Cart cart = new Cart();
        cart.setId(0L);
        cart.addItem(item);
        cart.setUser(user);

        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("test");

        UserOrder userOrder = response.getBody();
        List<UserOrder> orders = new ArrayList<UserOrder>(Arrays.asList(userOrder));

        when(orderRepository.findByUser(user)).thenReturn(orders);

        ResponseEntity<List<UserOrder>> historyResponse = orderController.getOrdersForUser("test");

        assertNotNull(historyResponse);
        assertEquals(200, historyResponse.getStatusCodeValue());

        List<UserOrder> history = historyResponse.getBody();
        assertNotNull(history);
        assertEquals(1, history.size());

    }

    @Test
    public void testHistoryUserNotFound() {

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Leonardo");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }



}
