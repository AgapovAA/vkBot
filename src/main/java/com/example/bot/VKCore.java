package com.example.bot;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;

import java.util.List;

public class VKCore {

    private VkApiClient vk;
    private GroupActor actor;
    private int ts;
    private int maxMsgId = -1;

    public VKCore(Integer groupId, String accessToken) throws ClientException, ApiException {
        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);
        actor = new GroupActor(groupId, accessToken);
        ts = vk.messages().getLongPollServer(actor).execute().getTs();
    }

    GroupActor getActor() {
        return actor;
    }

    VkApiClient getVk() {
        return vk;
    }

    Message getMessage() throws ClientException, ApiException {
        MessagesGetLongPollHistoryQuery eventsQuery = vk.messages()
                .getLongPollHistory(actor)
                .ts(ts);
        if (maxMsgId > 0) {
            eventsQuery.maxMsgId(maxMsgId);
        }
        List<Message> messages = eventsQuery
                .execute()
                .getMessages()
                .getItems();
        if (!messages.isEmpty()) {
            try {
                ts = vk.messages()
                        .getLongPollServer(actor)
                        .execute()
                        .getTs();
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        if (!messages.isEmpty() && !messages.get(0).isOut()) {
            int messageId = messages.get(0).getId();
            if (messageId > maxMsgId) {
                maxMsgId = messageId;
            }
            return messages.get(0);
        }
        return null;
    }
}

