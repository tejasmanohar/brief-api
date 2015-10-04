package models.helpers;

public class EmployerInfoHelper {
    public Long id;
    public String firstName;
    public String lastName;
    public Long companyId;

    public EmployerInfoHelper(Long id, String firstName, String lastName, Long companyId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyId = companyId;
    }
}
