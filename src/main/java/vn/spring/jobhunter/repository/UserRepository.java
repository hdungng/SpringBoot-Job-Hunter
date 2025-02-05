package vn.spring.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.spring.jobhunter.domain.Company;
import vn.spring.jobhunter.domain.User;


public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    public User findByEmail(String email);

    public boolean existsByEmail(String email);

    public User findByRefreshTokenAndEmail(String token, String email);

    public List<User> findByCompany(Company company);
}
