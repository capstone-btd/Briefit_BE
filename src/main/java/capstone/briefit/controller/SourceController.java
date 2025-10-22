package capstone.briefit.controller;

import capstone.briefit.dto.ArticleResponseDTO;
import capstone.briefit.dto.SourceDTO;
import capstone.briefit.service.SourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/source")
public class SourceController {
    SourceService sourceService;

    @Autowired
    public SourceController(SourceService sourceService) {
        this.sourceService = sourceService;
    }

    @GetMapping("/company/category")
    public List<SourceDTO.SourceCompanyDTO> getScraps(){
        return sourceService.getSourceCompanyCategory();
    }
}
