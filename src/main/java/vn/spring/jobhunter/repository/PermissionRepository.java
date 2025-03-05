package vn.spring.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.spring.jobhunter.domain.Permission;



public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    public boolean existsByName(String name);

    public List<Permission> findByIdIn(List<Long> permissions);

    public boolean existsByModuleAndApiPathAndMethod(String module, String apiPath, String method);
}
