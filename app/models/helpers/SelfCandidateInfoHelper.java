package models.helpers;

import models.CandidateModel;
import play.data.validation.Constraints;

public class SelfCandidateInfoHelper {
    @Constraints.Required
    public Long id;

    @Constraints.Required
    @Constraints.Email
    public String emailAddress;

    @Constraints.Required
    public CandidateInfoHelper candidateInfoHelper;

    public SelfCandidateInfoHelper(Long id, String emailAddress, CandidateModel basicInfo) {
        this.id = id;
        this.emailAddress = emailAddress;
        candidateInfoHelper = new CandidateInfoHelper(basicInfo);
    }
}
