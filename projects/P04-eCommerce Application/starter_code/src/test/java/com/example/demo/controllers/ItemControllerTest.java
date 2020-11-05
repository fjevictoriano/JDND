package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemController itemController;


    @Before
    public void setUp() throws Exception {
        Item item = new Item();
        item.setDescription("desc");
        item.setId(1L);
        item.setName("itemName");
        item.setPrice(new BigDecimal(34));
        given(itemRepository.findById(1L)).willReturn(Optional.of(item));
        given(itemRepository.findByName("itemName")).willReturn(getAllItems());
        given(itemRepository.findAll()).willReturn(getAllItems());

    }

    private List<Item> getAllItems() {
        return Collections.singletonList(new Item());
    }

    @Test
    public void getItems() {
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    public void getItemById() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response.getBody());
        assertEquals(Long.valueOf(1), response.getBody().getId());
        assertEquals("desc", response.getBody().getDescription());

    }

    @Test
    public void getItemsByName() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("itemName");
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertFalse(items.isEmpty());
    }
}