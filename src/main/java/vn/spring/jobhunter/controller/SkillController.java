package vn.spring.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import vn.spring.jobhunter.domain.Skill;
import vn.spring.jobhunter.domain.response.ResultPaginationDTO;
import vn.spring.jobhunter.service.SkillService;
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
@RequestMapping("/api/v1/skills")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public ResponseEntity<ResultPaginationDTO> getAllSkills(@Filter Specification<Skill> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(skillService.getAllSkills(spec, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Skill> getSkillById(@PathVariable Long id) throws IdInvalidException {
        Optional<Skill> fetchSkill = skillService.getSkillById(id);

        if(fetchSkill.isEmpty()) {
            throw new IdInvalidException("Skill with id " + id + " not exist");
        }

        return ResponseEntity.status(HttpStatus.OK).body(fetchSkill.get());
    }

    @PostMapping
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        // check name
        if(skill.getName() != null && this.skillService.isNameExist(skill.getName()));
        Skill newSkill = skillService.createSkill(skill);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSkill);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Skill> updateSkill(@RequestBody Skill skillDetails) throws IdInvalidException {
        Optional<Skill> skillOptional = skillService.getSkillById(skillDetails.getId());

        if(skillOptional.isEmpty()) {
            throw new IdInvalidException("Skill with id " + skillDetails.getId() + " not exist");
        }

        Skill updateSkill = skillOptional.get();

        if(skillDetails.getName() != null && skillService.isNameExist(skillDetails.getName())) {
            throw new IdInvalidException("Skill name " + skillDetails.getId() + " not exist");
        }

        updateSkill.setName(skillDetails.getName());
        return ResponseEntity.ok().body(skillService.updateSkill(updateSkill));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) throws IdInvalidException {
        skillService.deleteSkill(id);
        return ResponseEntity.ok(null);
    }

}
