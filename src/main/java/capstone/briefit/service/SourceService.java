package capstone.briefit.service;

import capstone.briefit.dto.SourceDTO;
import capstone.briefit.repository.SourceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SourceService {
    SourceRepository sourceRepository;

    @Autowired
    public SourceService(SourceRepository articleSourceRepository) {
        this.sourceRepository = articleSourceRepository;
    }

    public List<SourceDTO.SourceCompanyDTO> getSourceCompanyCategory(){
        List<Object[]> sourceCompanyList = sourceRepository.getSourceCompanyCategory();

        List<SourceDTO.SourceCompanyDTO> sourceCompanyCategory = new ArrayList<>();
        String tempCompany = "기타";
        Long tempCount = 0L;
        for(Object[] sourceCompany : sourceCompanyList){
            if(((String)sourceCompany[0]).equals("기타")){
                tempCount = (Long) sourceCompany[1];
                continue;
            }
            sourceCompanyCategory.add(SourceDTO.SourceCompanyDTO.builder().company((String)sourceCompany[0]).count((Long)sourceCompany[1]).build());
        }
        sourceCompanyCategory.add(SourceDTO.SourceCompanyDTO.builder().company(tempCompany).count(tempCount).build());

        return sourceCompanyCategory;
    }
}
