package vn.spring.jobhunter.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.spring.jobhunter.domain.Permission;
import vn.spring.jobhunter.domain.response.ResultPaginationDTO;
import vn.spring.jobhunter.repository.PermissionRepository;
import vn.spring.jobhunter.util.error.IdInvalidException;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public ResultPaginationDTO getAllPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pagePermission = permissionRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pagePermission.getNumber() + 1);
        meta.setPageSize(pagePermission.getSize());
        meta.setPages(pagePermission.getTotalPages());
        meta.setTotal(pagePermission.getTotalElements());

        rs.setMeta(meta);
        return rs;
    }

    public boolean isPermissionExist(Permission permission) {
        return permissionRepository.existsByModuleAndApiPathAndMethod(
                permission.getModule(),
                permission.getApiPath(),
                permission.getMethod());
    }

    public Optional<Permission> getPermissionById(Long id) {
        return permissionRepository.findById(id);
    }

    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    public Permission updatePermission(Permission permissionDetails) {
        Optional<Permission> permissionDBOptional = getPermissionById(permissionDetails.getId());

        if (permissionDBOptional.isPresent()) {
            Permission permissionDB = permissionDBOptional.get();

            permissionDB.setName(permissionDetails.getName());
            permissionDB.setApiPath(permissionDetails.getApiPath());
            permissionDB.setMethod(permissionDetails.getMethod());
            permissionDB.setModule(permissionDB.getModule());

            return permissionRepository.save(permissionDetails);
        }
        return null;
    }

    public void deletePermission(Long id) throws IdInvalidException {
        Optional<Permission> permissionOptional = permissionRepository.findById(id);

        if (permissionOptional.isEmpty()) {
            throw new IdInvalidException("Permission with id" + id + " not exist");
        }

        Permission currentPermission = permissionOptional.get();

        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));

        permissionRepository.delete(currentPermission);
    }
}