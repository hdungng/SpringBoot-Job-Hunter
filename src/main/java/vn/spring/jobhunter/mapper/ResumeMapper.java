package vn.spring.jobhunter.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vn.spring.jobhunter.domain.Resume;
import vn.spring.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.spring.jobhunter.domain.response.resume.ResResumeDTO;
import vn.spring.jobhunter.domain.response.resume.ResUpdateResumeDTO;



@Service
public class ResumeMapper {
    public ResCreateResumeDTO mapToCreateResumeDto(Resume resume) {
        ResCreateResumeDTO dto = new ResCreateResumeDTO();
        dto.setId(resume.getId());
        dto.setCreatedAt(resume.getCreatedAt());
        dto.setCreatedBy(resume.getCreatedBy());
        return dto;
    }

    public ResUpdateResumeDTO mapToUpdateResumeDto(Resume resume) {
        ResUpdateResumeDTO dto = new ResUpdateResumeDTO();
        dto.setCreatedAt(resume.getCreatedAt());
        dto.setCreatedBy(resume.getCreatedBy());
        return dto;
    }

    public ResResumeDTO mapToResumeDto(Resume resume) {
        ResResumeDTO dto = new ResResumeDTO();
        dto.setId(resume.getId());
        dto.setEmail(resume.getEmail());
        dto.setUrl(resume.getUrl());
        dto.setStatus(resume.getStatus());
        dto.setCreatedAt(resume.getCreatedAt());
        dto.setCreatedAt(resume.getCreatedAt());
        dto.setUpdatedBy(resume.getUpdatedBy());
        dto.setCreatedBy(resume.getCreatedBy());

        if(resume.getJob() != null) {
            dto.setCompanyName(resume.getJob().getCompany().getName());
        }

        dto.setUser(new ResResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()));
        dto.setJob(new ResResumeDTO.JobResume(resume.getJob().getId(), resume.getJob().getName()));
        return dto;
    }

}
