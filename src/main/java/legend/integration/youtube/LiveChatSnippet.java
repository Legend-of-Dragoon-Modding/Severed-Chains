package legend.integration.youtube;

/*
chatEndedEvent – The chat has ended and no more messages can be inserted after this one. This will occur naturally a little while after a broadcast ends. Note that this type of message is not currently sent for live chats on a channel's default broadcast.
messageDeletedEvent – A message has been deleted by a moderator. The author field contains the moderator's details. This event does not have any display content.
sponsorOnlyModeEndedEvent – The chat is no longer in sponsors-only mode, which means that users that are not sponsors are now able to send messages. This event does not have any display content.
sponsorOnlyModeStartedEvent – The chat has entered sponsors-only mode, which means that only sponsors are able to send messages. This event has no display content.
newSponsorEvent – A new user has sponsored the channel that owns the live chat. The author fields contain the new sponsor's details.
memberMilestoneChatEvent – A user has sent a Member Milestone Chat.
superChatEvent – A user has purchased a Super Chat.
superStickerEvent – A user has purchased a Super Sticker.
textMessageEvent – A user has sent a text message.
tombstone – A tombstone signifies that a message used to exist with this id and publish time, but it has since been deleted. It is not sent upon deletion of a message, but rather is shown to signify where the message used to be before deletion. Only the snippet.liveChatId, snippet.type, and snippet.publishedAt fields are present in this type of message.
userBannedEvent – A user has been banned by a moderator. The author field contains the moderator's details.
membershipGiftingEvent – A user has purchased memberships for other viewers.
giftMembershipReceivedEvent – A user has received a gift membership.
 */

record LiveChatTextMessageDetails(String messageText) {}

public record LiveChatSnippet(String type, LiveChatTextMessageDetails textMessageDetails) {
}
