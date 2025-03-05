package vn.spring.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.spring.jobhunter.domain.Resume;



public interface ResumeRepository extends JpaRepository<Resume, Long>, JpaSpecificationExecutor<Resume> {
    public boolean existsByEmail(String email);

    public List<Resume> findByIdIn(List<Long> skills);
}
