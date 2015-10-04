package services;

import models.ConversationModel;
import models.UserModel;
import models.helpers.ConversationHelper;
import models.helpers.ConversationInviteHelper;

import java.util.List;

public interface IConversationService {
    ConversationHelper create(UserModel owner, String title, Long applicationId, List<Long> membersIds);
    List<ConversationHelper> list(Long userId);
    ConversationModel get(UserModel user, Long id);
    ConversationHelper show(UserModel user, Long id);
    ConversationHelper invite(UserModel user, Long id, ConversationInviteHelper helper);
}
