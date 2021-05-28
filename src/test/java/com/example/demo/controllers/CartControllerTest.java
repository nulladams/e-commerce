package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void testAddToCart() {

        String userName = "test";
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

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setQuantity(1);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item));


        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart c = response.getBody();
        assertNotNull(c);
        assertEquals("test", c.getUser().getUsername());
        assertEquals(cart.getTotal(), c.getTotal());

    }
}
