package vn.spring.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.spring.jobhunter.domain.Role;



public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    public boolean existsByName(String name);

    public List<Role> findByIdIn(List<Long> roles);
}
