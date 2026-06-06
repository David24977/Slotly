package com.david.agenda_api.verification_token;

import com.david.agenda_api.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationToken extends BaseEntity {

    @Column(nullable = false, unique = true, length = 255)
    private String token;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean used = false;

    // Método de utilidad rápido para saber si ha caducado
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }
}
