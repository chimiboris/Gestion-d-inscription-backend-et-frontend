package com.inscription.app.web.websocket.dto;

public class ChatMessageDTO {
    private String contenu;
    private String destinataireLogin;

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getDestinataireLogin() {
        return destinataireLogin;
    }

    public void setDestinataireLogin(String destinataireLogin) {
        this.destinataireLogin = destinataireLogin;
    }
}

