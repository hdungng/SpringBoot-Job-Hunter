package vn.spring.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.spring.jobhunter.domain.Permission;
import vn.spring.jobhunter.domain.Role;
import vn.spring.jobhunter.domain.response.ResultPaginationDTO;
import vn.spring.jobhunter.repository.PermissionRepository;
import vn.spring.jobhunter.repository.RoleRepository;
import vn.spring.jobhunter.util.error.IdInvalidException;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public ResultPaginationDTO getAllRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> pageRole = roleRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageRole.getNumber() + 1);
        meta.setPageSize(pageRole.getSize());
        meta.setPages(pageRole.getTotalPages());
        meta.setTotal(pageRole.getTotalElements());

        rs.setMeta(meta);
        return rs;
    }

    public boolean isRoleExist(Role role) {
        return roleRepository.existsByName(role.getName());
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Role createRole(Role role) {

        // check Permission
        if (role.getPermissions() != null) {
            List<Long> reqPermissions = role.getPermissions().stream()
                    .map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Permission> dbPermission = permissionRepository.findByIdIn(reqPermissions);
            role.setPermissions(dbPermission);
        }
        return roleRepository.save(role);
    }

    public Role updateRole(Role roleDetails) {
        Optional<Role> roleDBOptional = getRoleById(roleDetails.getId());

        if (roleDBOptional.isPresent()) {
            Role roleDB = roleDBOptional.get();

            if (roleDetails.getPermissions() != null) {
                List<Long> reqPermissions = roleDetails.getPermissions().stream()
                        .map(x -> x.getId())
                        .collect(Collectors.toList());

                List<Permission> dbPermission = permissionRepository.findByIdIn(reqPermissions);

                roleDB.setPermissions(dbPermission);
            }

            roleDB.setName(roleDetails.getName());
            roleDB.setDescription(roleDetails.getDescription());
            roleDB.setActive(roleDetails.isActive());
            return roleRepository.save(roleDetails);
        }
        return null;
    }

    public void deleteRole(Long id) throws IdInvalidException {
        Optional<Role> roleOptional = roleRepository.findById(id);

        if (roleOptional.isEmpty()) {
            throw new IdInvalidException("Role with id" + id + " not exist");
        }

        Role currentRole = roleOptional.get();

        roleRepository.delete(currentRole);
    }
}