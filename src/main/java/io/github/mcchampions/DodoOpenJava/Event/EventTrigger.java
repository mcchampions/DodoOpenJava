package io.github.mcchampions.DodoOpenJava.Event;

import com.alibaba.fastjson2.JSONObject;
import io.github.mcchampions.DodoOpenJava.Event.events.*;
import okhttp3.*;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 事件触发
 */
public class EventTrigger {
    static EventTrigger p;
    static String wssLo="";
    static OkHttpClient okHttpClient = new OkHttpClient();
    static OkHttpClient wss=new OkHttpClient.Builder()
            .pingInterval(30, TimeUnit.SECONDS) //保活心跳
            .build();
    static WebSocket mWebSocket;

    static Boolean enable = false;
    public static void main(String Authorization) {
        if (enable = true) return;
        Request requestc = new Request.Builder().url("https://botopen.imdodo.com/api/v1/websocket/connection").addHeader("Content-Type", "application/json").addHeader("Authorization", Authorization)
                .post(RequestBody.create(MediaType.parse("application/json"), "{}"))
                .build();

        enable = true;

        okHttpClient.newCall(requestc).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                wssLo= JSONObject.parseObject(Objects.requireNonNull(response.body()).string()).getJSONObject("data").getString("endpoint");
                //TODO 建立wss链接
                //getLogger().info(wssLo);
                response.close();
                Request request = new Request.Builder()
                        .url(wssLo).build();
                mWebSocket = wss.newWebSocket(request, new WsListenerC(p));//TODO 这里是处理wss发来的数据
            }
        });
    }

    private static class WsListenerC extends WebSocketListener {
        EventTrigger p;
        public WsListenerC(EventTrigger dodo) {
            p=dodo;
        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, ByteString bytes) {
            JSONObject jsontext = JSONObject.parseObject(bytes.utf8());
            switch (jsontext.getJSONObject("data").getString("eventType")) {
                case "1001":
                    try {
                        new EventManage().fireEvent(new PersonalMessageEvent(jsontext));
                    } catch (EventException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "2001":
                    try {
                        new EventManage().fireEvent(new MessageEvent(jsontext));
                    } catch (EventException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "3001":
                    try {
                        new EventManage().fireEvent(new MessageReactionEvent(jsontext));
                    } catch (EventException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "3002":
                    try {
                        new EventManage().fireEvent(new CardMessageButtonClickEvent(jsontext));
                    } catch (EventException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "3003":
                    try {
                        new EventManage().fireEvent(new CardMessageFormSubmitEvent(jsontext));
                    } catch (EventException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "3004":
                    try {
                        new EventManage().fireEvent(new CardMessageListSubmitEvent(jsontext));
                    } catch (EventException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "4001":
                    try {
                        new EventManage().fireEvent(new MemberJoinEvent(jsontext));
                    } catch (EventException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "4002":
                    try {
                        new EventManage().fireEvent(new MemberLeaveEvent(jsontext));
                    } catch (EventException e) {
                        throw new RuntimeException(e);
                    }
                    break;
            }
        }

        @Override
        public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            mWebSocket.close(1000,"正常关闭");
        }
    }
}
