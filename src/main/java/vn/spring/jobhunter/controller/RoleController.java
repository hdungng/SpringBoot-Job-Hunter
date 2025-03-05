package vn.spring.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import vn.spring.jobhunter.domain.Role;
import vn.spring.jobhunter.domain.response.ResultPaginationDTO;
import vn.spring.jobhunter.service.RoleService;
import vn.spring.jobhunter.util.error.IdInvalidException;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<ResultPaginationDTO> getAllRoles(@Filter Specification<Role> spec,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.getAllRoles(spec, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) throws IdInvalidException {
        Optional<Role> fetchRole = roleService.getRoleById(id);

        if (fetchRole.isEmpty()) {
            throw new IdInvalidException("Role with id " + id + " not exist");
        }

        return ResponseEntity.status(HttpStatus.OK).body(fetchRole.get());
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role)
            throws IdInvalidException {

        if (roleService.isRoleExist(role)) {
            throw new IdInvalidException("Role name " + role.getName() + " has existed");
        }

        Role newRole = roleService.createRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRole);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@RequestBody Role roleDetails)
            throws IdInvalidException {
        Optional<Role> roleOptional = roleService.getRoleById(roleDetails.getId());

        if (roleOptional.isEmpty()) {
            throw new IdInvalidException("Role with id " + roleDetails.getId() + " not exist");
        }

        // Role updateRole = roleOptional.get();

        // if (roleService.isRoleExist(roleDetails)) {
        //     throw new IdInvalidException("Role name " + roleDetails.getName() + " has existed");
        // }
        
        return ResponseEntity.ok().body(roleService.updateRole(roleOptional.get()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) throws IdInvalidException {
        roleService.deleteRole(id);
        return ResponseEntity.ok(null);
    }

}
