package vn.spring.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.spring.jobhunter.domain.Company;
import vn.spring.jobhunter.domain.Job;
import vn.spring.jobhunter.domain.Skill;
import vn.spring.jobhunter.domain.response.ResultPaginationDTO;
import vn.spring.jobhunter.repository.CompanyRepository;
import vn.spring.jobhunter.repository.JobRepository;
import vn.spring.jobhunter.repository.SkillRepository;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;

    

    public JobService(JobRepository jobRepository, SkillRepository skillRepository,
            CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyRepository = companyRepository;
    }

    public ResultPaginationDTO getAllJobs(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJob = jobRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageJob.getNumber() + 1);
        meta.setPageSize(pageJob.getSize());
        meta.setPages(pageJob.getTotalPages());
        meta.setTotal(pageJob.getTotalElements());

        rs.setMeta(meta);
        return rs;
    }

    public boolean isNameExist(String name) {
        return jobRepository.existsByName(name);
    }

    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    public Job createJob(Job job) {
        // check skills
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills().stream()
                    .map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Skill> dbSkills = skillRepository.findByIdIn(reqSkills);
            job.setSkills(dbSkills);
        }

        return jobRepository.save(job);
    }

    public Job updateJob(Job jobDetails) {
        // check skills
        if (jobDetails.getSkills() != null) {
            List<Long> reqSkills = jobDetails.getSkills().stream()
                    .map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Skill> dbSkills = skillRepository.findByIdIn(reqSkills);
            jobDetails.setSkills(dbSkills);
        }

        if(jobDetails.getCompany() != null) {
            Optional<Company> companyOptional = companyRepository.findById(jobDetails.getCompany().getId());

            if(companyOptional.isPresent()) {
                jobDetails.setCompany(companyOptional.get());
            }
        }

        
        return jobRepository.save(jobDetails);
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }
}