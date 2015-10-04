package models.helpers;

import models.UserModel;

public class ConversationMemberHelper {
    public Long id;
    public UserModel.Role role;
    public String firstName;
    public String lastName;

    public ConversationMemberHelper(Long id, UserModel.Role role, String firstName, String lastName) {
        this.id = id;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
