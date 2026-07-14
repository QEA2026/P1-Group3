package com.expense.manager.controller;
import com.expense.manager.models.User;
import com.expense.manager.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController{
    @Autowired
    private UserDao dao;

    @GetMapping
    public List<User> getUsers() throws SQLException {
        return dao.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable int id) throws SQLException {
        return dao.findById(id);
    }

    @GetMapping("/username/{username}")
    public User findByUsername(@PathVariable String username) throws SQLException {
        return dao.findByUsername(username);
    }

    @PostMapping("/login")
    public User validateManagerLogin(@RequestBody Map<String, String> request) throws SQLException {
        return dao.validateManagerLogin(request.get("username"), request.get("password"));
    }

}
