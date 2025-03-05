package vn.spring.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

import vn.spring.jobhunter.domain.Job;
import vn.spring.jobhunter.domain.Resume;
import vn.spring.jobhunter.domain.User;
import vn.spring.jobhunter.domain.response.ResultPaginationDTO;
import vn.spring.jobhunter.domain.response.resume.ResResumeDTO;
import vn.spring.jobhunter.mapper.ResumeMapper;
import vn.spring.jobhunter.repository.JobRepository;
import vn.spring.jobhunter.repository.ResumeRepository;
import vn.spring.jobhunter.repository.UserRepository;
import vn.spring.jobhunter.util.SecurityUtil;
import vn.spring.jobhunter.util.error.IdInvalidException;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ResumeMapper resumeMapper;


    @Autowired
    FilterBuilder fb;

    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository, JobRepository jobRepository,
            ResumeMapper resumeMapper) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.resumeMapper = resumeMapper;
    }

    public ResultPaginationDTO getAllResumes(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> pageResume = resumeRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageResume.getNumber() + 1);
        meta.setPageSize(pageResume.getSize());
        meta.setPages(pageResume.getTotalPages());
        meta.setTotal(pageResume.getTotalElements());

        rs.setMeta(meta);

        // remove sensitive data

        List<ResResumeDTO> listResume = pageResume.getContent().stream()
                .map(item -> resumeMapper.mapToResumeDto(item))
                .collect(Collectors.toList());

        rs.setResult(listResume);
        return rs;
    }

    public boolean checkResumeExist(Resume resume) {
        if (resume.getUser() == null)
            return false;

        Optional<User> userOptional = userRepository.findById(resume.getUser().getId());

        if (userOptional.isEmpty())
            return false;

        if (resume.getJob() == null)
            return false;
        Optional<Job> jobOptional = jobRepository.findById(resume.getJob().getId());
        if (jobOptional.isEmpty())
            return false;

        return true;
    }

    public ResultPaginationDTO getResumesByUser(Pageable pageable) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true ? SecurityUtil.getCurrentUserLogin().get() : "";

        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageResume = resumeRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageResume.getNumber() + 1);
        meta.setPageSize(pageResume.getSize());
        meta.setPages(pageResume.getTotalPages());
        meta.setTotal(pageResume.getTotalElements());

        rs.setMeta(meta);

        return rs;
    }

    public Optional<Resume> getResumeById(Long id) {
        return resumeRepository.findById(id);
    }

    public Resume createResume(Resume resume) {
        return resumeRepository.save(resume);
    }

    public Resume updateResume(Resume resumeDetails) {
        return resumeRepository.save(resumeDetails);
    }

    public void deleteResume(Long id) throws IdInvalidException {
        Optional<Resume> resumeOptional = resumeRepository.findById(id);

        if (resumeOptional.isEmpty()) {
            throw new IdInvalidException("resume with id" + id + " not exist");
        }

        Resume currentResume = resumeOptional.get();

        resumeRepository.delete(currentResume);
    }
}