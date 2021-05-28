package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);

    }

    @Test
    public void testGetItemById() {

        Item item = new Item();
        item.setName("test");
        item.setPrice(BigDecimal.valueOf(15));

        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(0L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item i = response.getBody();
        assertNotNull(i);
        assertEquals(item.getName(), i.getName());
        assertEquals(item.getPrice(), i.getPrice());

    }

    @Test
    public void testGetItemByName() {
        Item item = new Item();
        item.setName("test");
        item.setPrice(BigDecimal.valueOf(15));

        when(itemRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(item)));

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
    }

}
