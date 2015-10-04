package models.helpers;

import play.data.validation.Constraints;

import java.util.List;

public class ConversationInviteHelper {
    @Constraints.Required
    public List<Long> membersIds;
}
