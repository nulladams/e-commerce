package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() {

        when(encoder.encode("testPass")).thenReturn("thisIsHashed");

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPass");
        r.setConfirmPassword("testPass");

        ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

    }

    @Test
    public void create_user_failure() {
        when(encoder.encode("testPass")).thenReturn("thisIsHashed");

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("test");
        r.setConfirmPassword("testPass");

        ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(401, response.getStatusCodeValue());

    }

    @Test
    public void create_user_exception() {
        thrown.expect(NullPointerException.class);

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("test");
        r.setConfirmPassword("testPass");

        ResponseEntity<User> response = userController.createUser(r);

        User u = response.getBody();
        assertEquals(0, u.getId());

    }

    @Test
    public void TestFindUserById() {
        when(encoder.encode("testPass")).thenReturn("thisIsHashed");

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPass");
        r.setConfirmPassword("testPass");

        ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

        when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(u));

        ResponseEntity<User> userResponse = userController.findById(0);

        assertNotNull(userResponse);
        assertEquals(200, response.getStatusCodeValue());

        User user = userResponse.getBody();

        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());



    }

    @Test
    public void TestFindUserByUsername() {
        when(encoder.encode("testPass")).thenReturn("thisIsHashed");

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPass");
        r.setConfirmPassword("testPass");

        ResponseEntity<User> response = userController.createUser(r);
        User u = response.getBody();

        when(userRepository.findByUsername("test")).thenReturn(u);

        ResponseEntity<User> userResponse = userController.findByUserName("test");

        assertNotNull(userResponse);
        assertEquals(200, response.getStatusCodeValue());

        User user = userResponse.getBody();

        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());


    }


}
