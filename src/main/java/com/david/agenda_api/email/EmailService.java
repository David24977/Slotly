package com.david.agenda_api.email;



public interface EmailService {
    void enviarMagicLink(String emailDestino, String token);
}
