package vn.spring.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.spring.jobhunter.domain.Job;



public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    public boolean existsByName(String name);
}
