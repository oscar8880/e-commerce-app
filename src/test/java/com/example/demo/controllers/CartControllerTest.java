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
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

  private CartController cartController;

  private CartRepository cartRepository = mock(CartRepository.class);
  private UserRepository userRepository = mock(UserRepository.class);
  private ItemRepository itemRepository = mock(ItemRepository.class);

  @Before
  public void setup() {
    cartController = new CartController();
    TestUtils.injectObjects(cartController, "userRepository", userRepository);
    TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
  }

  @Test
  public void add_to_cart() throws Exception {
    User user = new User();
    user.setUsername("john");
    user.setCart(new Cart());
    when(userRepository.findByUsername(("john"))).thenReturn(user);

    Item item = new Item();
    item.setId((long)1);
    item.setName("widget");
    item.setDescription("large");
    item.setPrice(new BigDecimal(1.99));
    when(itemRepository.findById((long) 1)).thenReturn(Optional.of(item));

    ModifyCartRequest cartRequest = new ModifyCartRequest();
    cartRequest.setItemId(1);
    cartRequest.setUsername("john");
    cartRequest.setQuantity(5);

    ResponseEntity<Cart> response = cartController.addTocart(cartRequest);
    Cart returnedCart = response.getBody();

    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(5, returnedCart.getItems().size());
    assertEquals(returnedCart.getItems().get(0).getName(), "widget");
  }

  @Test
  public void add_to_cart_invalid_username() throws Exception {
    User user = new User();
    user.setUsername("john");
    user.setCart(new Cart());

    Item item = new Item();
    item.setId((long)1);
    item.setName("widget");
    item.setDescription("large");
    item.setPrice(new BigDecimal(1.99));
    when(itemRepository.findById((long) 1)).thenReturn(Optional.of(item));

    ModifyCartRequest cartRequest = new ModifyCartRequest();
    cartRequest.setItemId(1);
    cartRequest.setUsername("john");
    cartRequest.setQuantity(5);

    ResponseEntity<Cart> response = cartController.addTocart(cartRequest);

    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());
  }

  @Test
  public void remove_from_cart() throws Exception {
    Item item = new Item();
    item.setId((long)1);
    item.setName("widget");
    item.setDescription("large");
    item.setPrice(new BigDecimal(1.99));
    when(itemRepository.findById((long) 1)).thenReturn(Optional.of(item));

    User user = new User();
    user.setUsername("john");
    Cart cart = new Cart();
    // Add 5 widgets to cart
    cart.addItem(item);
    cart.addItem(item);
    cart.addItem(item);
    cart.addItem(item);
    cart.addItem(item);
    user.setCart(cart);
    when(userRepository.findByUsername(("john"))).thenReturn(user);

    ModifyCartRequest cartRequest = new ModifyCartRequest();
    cartRequest.setItemId(1);
    cartRequest.setUsername("john");
    cartRequest.setQuantity(3);

    ResponseEntity<Cart> response = cartController.removeFromCart(cartRequest);
    Cart returnedCart = response.getBody();

    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(2, returnedCart.getItems().size());
    assertEquals(returnedCart.getItems().get(0).getName(), "widget");
  }
}
