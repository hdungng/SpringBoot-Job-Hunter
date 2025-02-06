package vn.spring.jobhunter.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.spring.jobhunter.domain.Skill;
import vn.spring.jobhunter.domain.response.ResultPaginationDTO;
import vn.spring.jobhunter.repository.SkillRepository;
import vn.spring.jobhunter.util.error.IdInvalidException;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public ResultPaginationDTO getAllSkills(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageSkill = skillRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageSkill.getNumber() + 1);
        meta.setPageSize(pageSkill.getSize());
        meta.setPages(pageSkill.getTotalPages());
        meta.setTotal(pageSkill.getTotalElements());

        rs.setMeta(meta);
        return rs;
    }

    public boolean isNameExist(String name) {
        return skillRepository.existsByName(name);
    }

    public Optional<Skill> getSkillById(Long id) {
        return skillRepository.findById(id);
    }

    public Skill createSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public Skill updateSkill(Skill skillDetails) {
        return skillRepository.save(skillDetails);
    }

    public void deleteSkill(Long id) throws IdInvalidException {
        Optional<Skill> skillOptional = skillRepository.findById(id);

        if(skillOptional.isEmpty()) {
            throw new IdInvalidException("Skill with id" + id + " not exist");
        }

        Skill currentSkill = skillOptional.get();

        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        skillRepository.delete(currentSkill);
    }
}