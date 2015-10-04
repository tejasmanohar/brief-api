package models.helpers;

import play.data.validation.Constraints;

import java.util.List;

public class CreateConversationHelper {
    @Constraints.MaxLength(256)
    @Constraints.Required
    public String title;

    @Constraints.Required
    public List<Long> membersIds;
}
