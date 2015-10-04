package models.helpers;

import play.data.validation.Constraints;

import java.util.List;

public class ConversationHelper {
    @Constraints.Required
    public Long id;

    @Constraints.MaxLength(256)
    @Constraints.Required
    public String title;

    @Constraints.Required
    public List<ConversationMemberHelper> members;

    @Constraints.Required
    public Long unread;

    public ConversationHelper(Long id, String title, List<ConversationMemberHelper> members, Long unread) {
        this.id = id;
        this.title = title;
        this.members = members;
        this.unread = unread;
    }
}
