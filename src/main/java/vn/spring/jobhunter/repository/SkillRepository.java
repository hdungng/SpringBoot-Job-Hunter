package vn.spring.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.spring.jobhunter.domain.Skill;
import vn.spring.jobhunter.domain.Job;



public interface SkillRepository extends JpaRepository<Skill, Long>, JpaSpecificationExecutor<Skill> {
    public boolean existsByName(String name);

    public List<Skill> findByIdIn(List<Long> skills);
}
