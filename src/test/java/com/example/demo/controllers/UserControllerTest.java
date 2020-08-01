package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

  private UserController userController;

  private UserRepository userRepository = mock(UserRepository.class);

  private CartRepository cartRepository = mock(CartRepository.class);

  private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

  @Before
  public void setup() {
    userController = new UserController();
    TestUtils.injectObjects(userController, "userRepository", userRepository);
    TestUtils.injectObjects(userController, "cartRepository", cartRepository);
    TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
  }

  @Test
  public void create_user_happy_path() throws Exception {
    when(bCryptPasswordEncoder.encode("password")).thenReturn("thisIsHashed");
    CreateUserRequest userRequest = new CreateUserRequest();
    userRequest.setUsername("test");
    userRequest.setPassword("password");
    userRequest.setConfirmPassword("password");

    ResponseEntity<User> response = userController.createUser(userRequest);

    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());

    User user = response.getBody();
    assertNotNull(user);
    assertEquals(0, user.getId());
    assertEquals("test", user.getUsername());
  }

  @Test
  public void fail_to_create_user_with_too_short_password() throws Exception {
    CreateUserRequest userRequest = new CreateUserRequest();
    userRequest.setUsername("test");
    userRequest.setPassword("pass");
    userRequest.setConfirmPassword("pass");

    ResponseEntity<User> response = userController.createUser(userRequest);

    assertNotNull(response);
    assertEquals(400, response.getStatusCodeValue());
  }

  @Test
  public void fail_to_create_user_with_non_matching_passwords() throws Exception {
    CreateUserRequest userRequest = new CreateUserRequest();
    userRequest.setUsername("test");
    userRequest.setPassword("password1");
    userRequest.setConfirmPassword("password2");

    ResponseEntity<User> response = userController.createUser(userRequest);

    assertNotNull(response);
    assertEquals(400, response.getStatusCodeValue());
  }

  @Test
  public void find_user_by_id() throws Exception {
    User user = new User();
    user.setUsername("john");
    when(userRepository.findById((long) 1)).thenReturn(Optional.of(user));

    ResponseEntity<User> response = userController.findById((long) 1);

    User returnedUser = response.getBody();
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals("john", returnedUser.getUsername());
  }

  @Test
  public void find_user_by_username() throws Exception {
    User user = new User();
    user.setUsername("john");
    user.setId(1);
    when(userRepository.findByUsername("john")).thenReturn(user);

    ResponseEntity<User> response = userController.findByUserName("john");

    User returnedUser = response.getBody();
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals("john", returnedUser.getUsername());
    assertEquals(1, returnedUser.getId());
  }
}