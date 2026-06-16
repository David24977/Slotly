package com.david.agenda_api.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailEmisor;

    @Override
    public void enviarMagicLink(String emailDestino, String token) {
        // URL definitiva que capturará el frontend de React
        String urlMagica = "http://localhost:8081/api/magic-link/verify?token=" + token;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(emailEmisor);
            helper.setTo(emailDestino);
            helper.setSubject("✨ Tu enlace mágico de acceso a Slotly");

            String contenidoHtml = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #eee; border-radius: 10px;">
                    <h2 style="color: #333; text-align: center;">¡Hola!</h2>
                    <p style="color: #555; font-size: 16px; line-height: 1.5;">
                        Has solicitado iniciar sesión en <strong>Slotly</strong>. Haz clic en el botón de abajo para acceder directamente a tu cuenta. Este enlace caducará en 15 minutos.
                    </p>
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" style="background-color: #4F46E5; color: white; padding: 12px 24px; text-decoration: none; font-weight: bold; border-radius: 5px; display: inline-block;">
                            Iniciar Sesión en Slotly
                        </a>
                    </div>
                    <p style="color: #999; font-size: 12px; text-align: center; margin-top: 30px;">
                        Si no has solicitado este correo, puedes ignorarlo de forma segura.
                    </p>
                </div>
                """.formatted(urlMagica);

            helper.setText(contenidoHtml, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error fatal al estructurar o enviar el correo electrónico vía SMTP", e);
        }
    }
}
