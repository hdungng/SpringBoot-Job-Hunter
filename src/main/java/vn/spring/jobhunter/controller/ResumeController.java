package vn.spring.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import vn.spring.jobhunter.domain.Resume;
import vn.spring.jobhunter.domain.response.ResultPaginationDTO;
import vn.spring.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.spring.jobhunter.domain.response.resume.ResResumeDTO;
import vn.spring.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.spring.jobhunter.mapper.ResumeMapper;
import vn.spring.jobhunter.service.ResumeService;
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
@RequestMapping("/api/v1/resumes")
public class ResumeController {
    private final ResumeService resumeService;
    private final ResumeMapper resumeMapper;

    public ResumeController(ResumeService resumeService, ResumeMapper resumeMapper) {
        this.resumeService = resumeService;
        this.resumeMapper = resumeMapper;
    }

    @GetMapping
    public ResponseEntity<ResultPaginationDTO> getAllResumes(@Filter Specification<Resume> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(resumeService.getAllResumes(spec, pageable));
    }

    @PostMapping("/by-user")
    public ResponseEntity<ResultPaginationDTO> getResumesByUser(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(resumeService.getResumesByUser(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResResumeDTO> getResumeById(@PathVariable Long id) throws IdInvalidException {
        Optional<Resume> fetchResume = resumeService.getResumeById(id);

        if(fetchResume.isEmpty()) {
            throw new IdInvalidException("resume with id " + id + " not exist");
        }

        return ResponseEntity.status(HttpStatus.OK).body(resumeMapper.mapToResumeDto(fetchResume.get()));
    }

    @PostMapping
    public ResponseEntity<ResCreateResumeDTO> createResume(@Valid @RequestBody Resume resume) throws IdInvalidException {
        // check 
        boolean isIdExist = resumeService.checkResumeExist(resume);

        if(!isIdExist) {
            throw new IdInvalidException("Resume is not exist");
        }

        Resume createdResume = resumeService.createResume(resume);
        return ResponseEntity.status(HttpStatus.CREATED).body(resumeMapper.mapToCreateResumeDto(createdResume));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume resumeDetails) throws IdInvalidException {
        Optional<Resume> resumeOptional = resumeService.getResumeById(resumeDetails.getId());

        if(resumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume with id " + resumeDetails.getId() + " not exist");
        }

        Resume updateResume = resumeOptional.get();

        updateResume.setStatus(resumeDetails.getStatus());
        
        Resume updatedResume = resumeService.updateResume(updateResume);
        return ResponseEntity.ok().body(resumeMapper.mapToUpdateResumeDto(updatedResume));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) throws IdInvalidException {
        resumeService.deleteResume(id);
        return ResponseEntity.ok(null);
    }

}
