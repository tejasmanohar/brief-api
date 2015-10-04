package models.helpers;

import models.CandidateModel;
import play.data.validation.Constraints;

public class PublicCandidateInfoHelper {
    @Constraints.Required
    public Long id;

    @Constraints.Required
    public CandidateInfoHelper candidateInfoHelper;

    public PublicCandidateInfoHelper(Long id, CandidateModel basicInfo) {
        this.id = id;
        candidateInfoHelper = new CandidateInfoHelper(basicInfo);
    }
}
