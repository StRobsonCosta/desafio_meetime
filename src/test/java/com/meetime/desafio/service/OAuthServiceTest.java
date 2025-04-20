package com.meetime.desafio.service;

import com.meetime.desafio.application.dto.OAuthTokenResponse;
import com.meetime.desafio.application.service.OAuthService;
import com.meetime.desafio.domain.port.out.TokenRepository;
import com.meetime.desafio.infrastructure.config.HubspotOAuthProperties;
import com.meetime.desafio.infrastructure.hubspot.client.HubspotOAuthClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OAuthServiceTest {

    @Mock
    private HubspotOAuthProperties properties;

    @Mock
    private HubspotOAuthClient hubspotOAuthClient;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private OAuthService oAuthService;

    private static final String AUTH_URL = "https://app.hubspot.com/oauth/authorize";
    private static final String CLIENT_ID = "client-id";
    private static final String REDIRECT_URI = "http://localhost:8081/callback";
    private static final String SCOPES = "crm.objects.contacts.read crm.objects.contacts.write";

    private static OAuthTokenResponse getTokenResponse() {
        return new OAuthTokenResponse("access-token", "refresh-token", 3600, "token_type");
    }

    @Test
    void shouldGenerateAuthorizationUrlCorrectly() {
        when(properties.getAuthUrl()).thenReturn(AUTH_URL);
        when(properties.getClientId()).thenReturn(CLIENT_ID);
        when(properties.getRedirectUri()).thenReturn(REDIRECT_URI);
        when(properties.getClientScopes()).thenReturn(SCOPES);

        String result = oAuthService.generateAuthorizationUrl();

        assertNotNull(result);
        assertTrue(result.contains("client_id=" + CLIENT_ID));
        assertTrue(result.contains("redirect_uri=" + REDIRECT_URI));
        assertTrue(result.contains("scope=" + SCOPES));
        assertTrue(result.contains("response_type=code"));

        verify(properties).getAuthUrl();
        verify(properties).getClientId();
        verify(properties).getRedirectUri();
        verify(properties).getClientScopes();
    }

    @Test
    void shouldHandleCallbackSuccessfully() {
        String code = "auth-code";
        OAuthTokenResponse response = getTokenResponse();

        when(hubspotOAuthClient.exchangeCodeForToken(code)).thenReturn(response);

        String result = oAuthService.handleCallback(code);

        assertEquals("access-token", result);

        verify(hubspotOAuthClient).exchangeCodeForToken(code);
        verify(tokenRepository).saveAccessToken("access-token", 3600L);
        verify(tokenRepository).saveRefreshToken("refresh-token");
    }
}
