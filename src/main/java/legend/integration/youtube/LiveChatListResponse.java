package legend.integration.youtube;

import java.util.List;

public record LiveChatListResponse(String nextPageToken, int pollingIntervalMillis, List<LiveChatMessage> items) {
}
