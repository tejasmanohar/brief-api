package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.helpers.*;

import models.utils.Model;
import utils.secure.SecurityHelper;

import javax.persistence.*;
import java.util.*;

@Entity
public class UserModel extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 256, unique = true)
    public String emailAddress;

    @Column
    public String imageUrl;

    @Column
    @JsonIgnore
    public String imageFileName;

    @Column(unique = true)
    @JsonIgnore
    public String authToken;

    @Column(length = 256, nullable = false)
    @Enumerated(EnumType.STRING)
    public Role role;

    @Column(unique = true)
    @JsonIgnore
    public Long twitterId;

    @Transient
    @JsonIgnore
    public String password;

    @Column(length = 64, nullable = false)
    @JsonIgnore
    private byte[] shaPassword;

    @JsonIgnore
    @ManyToMany(mappedBy = "members", cascade = CascadeType.ALL)
    public Set<ConversationModel> conversations = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    public CandidateModel candidate;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    public CompanyModel company;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    public EmployerModel employer;

    public static Finder<Long, UserModel> find = new Finder<>(Long.class, UserModel.class);

    public String generateAuthTokenToken(){
        if(authToken == null){
            authToken = id.toString() + UUID.randomUUID().toString();
            save();
        }
        return authToken;
    }

    public void setPassword(String password) {
        this.password = password;
        this.shaPassword = SecurityHelper.getSha512(password);
        save();
    }

    public UserModel() {
    }

    public UserModel(String emailAddress, String password, UserModel.Role role) {

        if(emailAddress != null) {
            this.emailAddress = emailAddress.toLowerCase();
        }

        this.role = role;
        this.twitterId = null;

        if(password == null) {
            password = UUID.randomUUID().toString();
        }

        setPassword(password);
    }

    public UserModel(String emailAddress, String password, UserModel.Role role, Long twitterId) {
        this(emailAddress, password, role);
        this.twitterId = twitterId;
    }

    public AuthHelper authResponse() {
        return new AuthHelper(authToken, emailAddress);
    }

    public SelfCandidateInfoHelper toSelfCandidateInfo() {
        return new SelfCandidateInfoHelper(candidate.id, emailAddress, candidate);
    }

    public PublicCandidateInfoHelper toCandidatePublicInfo() {
        return new PublicCandidateInfoHelper(candidate.id, candidate);
    }

    public ConversationMemberHelper toConversationMemberHelper() {
        Long id = null;
        String firstName = null;
        String lastName = null;

        switch (role) {
            case CANDIDATE:
                id = candidate.id;
                firstName = candidate.firstName;
                lastName = candidate.lastName;
                break;
            case EMPLOYER:
                id = employer.id;
                firstName = employer.firstName;
                lastName = employer.lastName;
                break;
        }

        return new ConversationMemberHelper(id, role, firstName, lastName);
    }

    public Long getId() {
        switch (role) {
            case CANDIDATE:
                return candidate.id;
            case EMPLOYER:
                return employer.id;
            case COMPANY:
                return company.id;
            default:
                return id;
        }
    }

    public EmployerInfoHelper toEmployerInfoHelper() {
        return new EmployerInfoHelper(employer.id, employer.firstName, employer.lastName, employer.companyId());
    }

    public CompanyInfoHelper toCompanyInfoHelper() {
        return new CompanyInfoHelper(company.id, company.name);
    }

    public enum Role {
        CANDIDATE,
        COMPANY,
        EMPLOYER
    }
}
