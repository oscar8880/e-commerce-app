package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
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

public class OrderControllerTest {

  private OrderController orderController;

  private OrderRepository orderRepository = mock(OrderRepository.class);
  private UserRepository userRepository = mock(UserRepository.class);

  @Before
  public void setup() {
    orderController = new OrderController();
    TestUtils.injectObjects(orderController, "userRepository", userRepository);
    TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
  }

  @Test
  public void submit_order() {
    Item item = new Item();
    item.setId((long)1);
    item.setName("widget");
    item.setDescription("large");
    item.setPrice(new BigDecimal(1.99));

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
    when(userRepository.findByUsername("john")).thenReturn(user);

    ResponseEntity<UserOrder> response = orderController.submit("john");
    UserOrder userOrder = response.getBody();
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals((double)9.95, userOrder.getTotal().doubleValue(), 0.0001);
  }

  @Test
  public void get_orders_for_user() {
    User user = new User();
    user.setUsername("john");

    Cart cart = new Cart();
    cart.setUser(user);

    Item item = new Item();
    item.setId((long)1);
    item.setName("widget");
    item.setDescription("large");
    item.setPrice(new BigDecimal(1.99));

    cart.addItem(item);
    cart.addItem(item);
    cart.addItem(item);

    user.setCart(cart);

    UserOrder userOrder = UserOrder.createFromCart(cart);
    List<UserOrder> usersOrders = new ArrayList<>();
    usersOrders.add(userOrder);

    when(userRepository.findByUsername("john")).thenReturn(user);
    when(orderRepository.findByUser(user)).thenReturn(usersOrders);

    ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("john");
    List<UserOrder> returnedOrders = response.getBody();
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals((double)5.97, returnedOrders.get(0).getTotal().doubleValue(), 0.0001);
  }
}
