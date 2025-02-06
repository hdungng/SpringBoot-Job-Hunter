package vn.spring.jobhunter.domain.response.job;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.spring.jobhunter.util.constant.LevelEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResCreateJobDTO {
    private long id;

    private String name;

    private String location;

    private double salary;

    private int quantity;

    private LevelEnum level;

    private String description;

    private Instant startDate;
    private Instant endDate;

    private boolean active;

    private List<String> skills;

    private Instant createdAt;

    private String createdBy;
}