package com.david.agenda_api.verification_token;

import com.david.agenda_api.verification_token.dto.VerificationTokenRequest;
import com.david.agenda_api.verification_token.dto.VerificationTokenResponse;


public interface VerificationTokenService {

    VerificationTokenResponse generateAndSendMagicLink(VerificationTokenRequest request);
    VerificationToken validateToken(String tokenStr);
    void invalidateToken(VerificationToken token);
    String validateTokenAndGenerateJwt(String tokenStr);

}
