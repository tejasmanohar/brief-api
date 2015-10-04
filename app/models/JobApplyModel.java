package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.utils.Model;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"job_id" , "candidate_id"})})
public class JobApplyModel extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 256, nullable = false)
    @Enumerated(EnumType.STRING)
    public Status status;

    @ManyToOne
    @JsonIgnore
    public JobModel job;

    @ManyToOne
    @JsonIgnore
    public CandidateModel candidate;

    @JsonProperty
    public Long candidateId() {
        if (candidate != null) {
            return candidate.id;
        } else {
            return null;
        }
    }

    @JsonProperty
    public Long jobId() {
        if (job != null) {
            return job.id;
        } else {
            return null;
        }
    }

    @JsonProperty
    public String jobTitle() {
        if (job != null) {
            return job.title;
        } else {
            return null;
        }
    }

    public enum Status {
        PENDING,
        REJECTED,
        INTERVIEWED,
        HIRED
    }

    public static Model.Finder<Long, JobApplyModel> find = new Model.Finder<>(Long.class, JobApplyModel.class);
}
