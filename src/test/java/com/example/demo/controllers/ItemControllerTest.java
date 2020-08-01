package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

  private ItemController itemController;
  private ItemRepository itemRepository = mock(ItemRepository.class);

  @Before
  public void setup() {
    itemController = new ItemController();
    TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
  }

  @Test
  public void get_items() {
    Item item = new Item();
    item.setId((long)1);
    item.setName("widget");
    item.setDescription("large");
    item.setPrice(new BigDecimal(1.99));
    List<Item> itemList = new ArrayList<>();
    itemList.add(item);
    when(itemRepository.findAll()).thenReturn(itemList);

    ResponseEntity<List<Item>> response = itemController.getItems();

    List<Item> returnedItems = response.getBody();
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(1, returnedItems.size());
    assertEquals("widget", returnedItems.get(0).getName());
  }

  @Test
  public void get_item_by_id() {
    Item item = new Item();
    item.setId((long)1);
    item.setName("widget");
    item.setDescription("large");
    item.setPrice(new BigDecimal(1.99));
    when(itemRepository.findById((long) 1)).thenReturn(Optional.of(item));

    ResponseEntity<Item> response = itemController.getItemById((long)1);

    Item returnedItem = response.getBody();
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals("widget", returnedItem.getName());
  }

  @Test
  public void get_item_by_name() {
    Item widget = new Item();
    widget.setId((long)1);
    widget.setName("widget");
    widget.setDescription("large");
    widget.setPrice(new BigDecimal(1.99));

    Item gadget = new Item();
    gadget.setId((long)1);
    gadget.setName("gadget");
    gadget.setDescription("small");
    gadget.setPrice(new BigDecimal(2.99));

    List<Item> allItems = new ArrayList<>();
    allItems.add(widget);
    allItems.add(widget);

    List<Item> onlyGadgets = new ArrayList<>();
    onlyGadgets.add(gadget);
    onlyGadgets.add(gadget);
    onlyGadgets.add(gadget);

    when(itemRepository.findByName("gadget")).thenReturn(onlyGadgets);

    ResponseEntity<List<Item>> response = itemController.getItemsByName("gadget");

    List<Item> returnedItems = response.getBody();
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(3, returnedItems.size());
    assertEquals("gadget", returnedItems.get(0).getName());
  }
}
