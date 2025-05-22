package com.example.billing.service;

import com.example.billing.model.User;
import com.example.billing.model.Role;
import com.example.billing.repository.UserRepository;
import com.example.billing.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User assignRoleToUser(Long userId, Long roleId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Role> roleOpt = roleRepository.findById(roleId);

        if (userOpt.isPresent() && roleOpt.isPresent()) {
            User user = userOpt.get();
            user.getRoles().add(roleOpt.get());
            return userRepository.save(user);
        }

        throw new RuntimeException("User or Role not found");
    }
}