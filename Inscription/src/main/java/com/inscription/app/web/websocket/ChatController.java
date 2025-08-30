package com.inscription.app.web.websocket;

import com.inscription.app.domain.Message;
import com.inscription.app.domain.User;
import com.inscription.app.repository.MessageRepository;
import com.inscription.app.repository.UserRepository;
import com.inscription.app.security.SecurityUtils;
import java.time.Instant;
import java.util.Optional;

import com.inscription.app.web.websocket.dto.ChatMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final Logger log = LoggerFactory.getLogger(ChatController.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ChatController(
        SimpMessagingTemplate messagingTemplate,
        MessageRepository messageRepository,
        UserRepository userRepository
    ) {
        this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @MessageMapping("/chat/send")
    public void receiveMessage(@Payload ChatMessageDTO chatMessage) {
        log.debug("🔵 Nouveau message reçu : {}", chatMessage.getContenu());

        Optional<String> login = SecurityUtils.getCurrentUserLogin();

        if (login.isEmpty()) {
            log.warn("⚠️ Utilisateur non authentifié");
            return;
        }

        User auteur = userRepository.findOneByLogin(login.get()).orElseThrow();
        User destinataire = userRepository.findOneByLogin(chatMessage.getDestinataireLogin()).orElse(null);

        Message message = new Message();
        message.setContenu(chatMessage.getContenu());
        message.setDateEnvoie(Instant.now());
        message.setUser(auteur);
        message.setDestinataire(destinataire);

        messageRepository.save(message);

        // 📨 Envoie du message en temps réel à tous les abonnés du topic
        messagingTemplate.convertAndSend("/topic/chat", message);
    }
}
