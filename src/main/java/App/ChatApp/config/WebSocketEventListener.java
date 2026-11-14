package App.ChatApp.config;

import App.ChatApp.chat.ChatMessage;
import App.ChatApp.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = stompHeaderAccessor.getSessionAttributes().get("username").toString();

        if (username != null) {
            log.info("User Disconnected : {}", username);
            var chatMessage = ChatMessage.builder()
                    .messageType(MessageType.LEAVE)
                    .sender(username)
                    .build();

            messagingTemplate.convertAndSend("/topic", chatMessage);

        }

    }

}
