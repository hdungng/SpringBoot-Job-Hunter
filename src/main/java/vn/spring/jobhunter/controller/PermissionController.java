package vn.spring.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import vn.spring.jobhunter.domain.Permission;
import vn.spring.jobhunter.domain.response.ResultPaginationDTO;
import vn.spring.jobhunter.service.PermissionService;
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
@RequestMapping("/api/v1/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public ResponseEntity<ResultPaginationDTO> getAllPermissions(@Filter Specification<Permission> spec,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(permissionService.getAllPermissions(spec, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id) throws IdInvalidException {
        Optional<Permission> fetchPermission = permissionService.getPermissionById(id);

        if (fetchPermission.isEmpty()) {
            throw new IdInvalidException("Permission with id " + id + " not exist");
        }

        return ResponseEntity.status(HttpStatus.OK).body(fetchPermission.get());
    }

    @PostMapping
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission permission)
            throws IdInvalidException {

        if (permissionService.isPermissionExist(permission)) {
            throw new IdInvalidException("Permission name " + permission.getName() + " has existed");
        }

        Permission newPermission = permissionService.createPermission(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPermission);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Permission> updatePermission(@RequestBody Permission permissionDetails)
            throws IdInvalidException {
        Optional<Permission> permissionOptional = permissionService.getPermissionById(permissionDetails.getId());

        if (permissionOptional.isEmpty()) {
            throw new IdInvalidException("Permission with id " + permissionDetails.getId() + " not exist");
        }

        Permission updatePermission = permissionOptional.get();

        if (permissionService.isPermissionExist(permissionDetails)) {
            throw new IdInvalidException("Permission name " + permissionDetails.getName() + " has existed");
        }

        return ResponseEntity.ok().body(permissionService.updatePermission(updatePermission));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) throws IdInvalidException {
        permissionService.deletePermission(id);
        return ResponseEntity.ok(null);
    }

}
