package vn.spring.jobhunter.domain.response.user;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.spring.jobhunter.util.constant.GenderEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {
    private long id;
    private String name;
    private String email;
    private int age;
    public String address;
    private GenderEnum gender;
    private Instant createdAt;
    private Instant updatedAt;
    private CompanyUser company;
    private RoleUser role;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompanyUser {
        private long id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleUser {
        private long id;
        private String name;
    }
}
