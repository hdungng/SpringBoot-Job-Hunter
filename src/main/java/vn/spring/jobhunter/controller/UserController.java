package vn.spring.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.spring.jobhunter.domain.User;
import vn.spring.jobhunter.domain.response.ResultPaginationDTO;
import vn.spring.jobhunter.domain.response.user.ResCreateUserDTO;
import vn.spring.jobhunter.domain.response.user.ResUpdateUserDTO;
import vn.spring.jobhunter.domain.response.user.ResUserDTO;
import vn.spring.jobhunter.mapper.UserMapper;
import vn.spring.jobhunter.service.UserService;
import vn.spring.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<ResultPaginationDTO> getAllUsers(@Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers(spec, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable Long id) throws IdInvalidException {
        Optional<User> fetchUser = userService.getUserById(id);

        if(fetchUser.isEmpty()) {
            throw new IdInvalidException("User with id " + id + " not exist");
        }

        return ResponseEntity.status(HttpStatus.OK).body(userMapper.mapToUserDto(fetchUser.get()));
    }

    @PostMapping
    public ResponseEntity<ResCreateUserDTO> createUser(@Valid @RequestBody User user) throws IdInvalidException {
        boolean isEmailExist = userService.isEmailExist(user.getEmail());

        if(isEmailExist) {
            throw new IdInvalidException("Email " + user.getEmail() + " is exist, please use another email.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.mapToCreateUserDto(newUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User userDetails) throws IdInvalidException {
        User updateUser = userService.updateUser(userDetails);

        if(updateUser == null) {
            throw new IdInvalidException("User with id " + userDetails.getId() + " not exist");
        }
        return ResponseEntity.ok(userMapper.mapToUpdateUserDto(updateUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws IdInvalidException {
        Optional<User> currentUser = userService.getUserById(id);

        if(currentUser.isEmpty()) {
            throw new IdInvalidException("User with id " + id + " not exist");
        }
        userService.deleteUser(id);

        return ResponseEntity.ok(null);
    }

}
