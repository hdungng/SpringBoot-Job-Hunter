package vn.spring.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.spring.jobhunter.domain.Company;
import vn.spring.jobhunter.domain.Role;
import vn.spring.jobhunter.domain.User;
import vn.spring.jobhunter.domain.response.ResultPaginationDTO;
import vn.spring.jobhunter.domain.response.user.ResUserDTO;
import vn.spring.jobhunter.mapper.UserMapper;
import vn.spring.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final RoleService roleService;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, CompanyService companyService, RoleService roleService,
            UserMapper userMapper) {
        this.userRepository = userRepository;
        this.companyService = companyService;
        this.roleService = roleService;
        this.userMapper = userMapper;
    }

    public ResultPaginationDTO getAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = userRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageUser.getNumber() + 1);
        meta.setPageSize(pageUser.getSize());
        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        rs.setMeta(meta);

        List<ResUserDTO> listUser = pageUser
                .getContent().stream()
                .map(item -> userMapper.mapToUserDto(item))
                .collect(Collectors.toList());

        rs.setResult(listUser);
        return rs;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public User createUser(User user) {
        // check company
        if (user.getCompany() != null) {
            Optional<Company> companyOptional = companyService.getCompanyById(user.getCompany().getId());
            user.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
        }

        // check role
        if (user.getRole() != null) {
            Optional<Role> RoleOptional = roleService.getRoleById(user.getRole().getId());
            user.setRole(RoleOptional.isPresent() ? RoleOptional.get() : null);
        }
        return userRepository.save(user);
    }

    public User updateUser(User userDetails) {

        User user = userRepository.getById(userDetails.getId());

        if (user != null) {
            user.setName(userDetails.getName());
            user.setAddress(userDetails.getAddress());
            user.setAge(userDetails.getAge());
            user.setGender(userDetails.getGender());

            // check company
            if (userDetails.getCompany() != null) {
                Optional<Company> companyOptional = companyService.getCompanyById(user.getCompany().getId());
                userDetails.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
            }

            // check role
            if (userDetails.getRole() != null) {
                Optional<Role> RoleOptional = roleService.getRoleById(user.getRole().getId());
                userDetails.setRole(RoleOptional.isPresent() ? RoleOptional.get() : null);
            }

            // update
            userRepository.save(user);
        }
        return user;
    }

    public void deleteUser(Long id) {
        Optional<User> user = getUserById(id);

        userRepository.delete(user.get());
    }

    public void updateUserToken(String token, String email) {
        User currentUser = getUserByEmail(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}