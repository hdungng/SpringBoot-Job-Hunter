package vn.spring.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import vn.spring.jobhunter.domain.Job;
import vn.spring.jobhunter.domain.response.ResultPaginationDTO;
import vn.spring.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.spring.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.spring.jobhunter.mapper.JobMapper;
import vn.spring.jobhunter.service.JobService;
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
@RequestMapping("/api/v1/jobs")
public class JobController {
    private final JobService jobService;
    private final JobMapper jobMapper;

    
    public JobController(JobService jobService, JobMapper jobMapper) {
        this.jobService = jobService;
        this.jobMapper = jobMapper;
    }

    @GetMapping
    public ResponseEntity<ResultPaginationDTO> getAllJobs(@Filter Specification<Job> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getAllJobs(spec, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) throws IdInvalidException {
        Optional<Job> fetchJob = jobService.getJobById(id);

        if(!fetchJob.isPresent()) {
            throw new IdInvalidException("Job not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(fetchJob.get());
    }

    @PostMapping
    public ResponseEntity<ResCreateJobDTO> createJob(@Valid @RequestBody Job job) throws IdInvalidException {
        // check name
        if(job.getName() != null && this.jobService.isNameExist(job.getName()));
        Job newJob = jobService.createJob(job);

        return ResponseEntity.status(HttpStatus.CREATED).body(jobMapper.mapToCreateJobDto(newJob));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@RequestBody Job jobDetails) throws IdInvalidException {
        Optional<Job> updateJob = jobService.getJobById(jobDetails.getId());

        if(!updateJob.isPresent()) {
            throw new IdInvalidException("Job with id " + jobDetails.getId() + " not exist");
        }

        Job jobUpdated = jobService.updateJob(updateJob.get());

        return ResponseEntity.ok().body(jobMapper.mapToUpdateUserDto(jobUpdated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) throws IdInvalidException {
        jobService.deleteJob(id);
        return ResponseEntity.ok(null);
    }

}
