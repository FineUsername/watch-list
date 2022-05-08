package com.mygroup.watchlist.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.mygroup.watchlist.back.repositories.VerificationTokenRepository;
import com.mygroup.watchlist.back.services.VerificationTokenService;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VerificationTokenServiceTests {

  @Mock
  private VerificationTokenRepository tokenRepository;

  @InjectMocks
  private VerificationTokenService tokenService;

  @Test
  public void deleteTokenWhenNone() {
    UUID body = UUID.randomUUID();
    when(tokenRepository.existsByBody(body)).thenReturn(false);
    assertThrows(NoSuchElementException.class, () -> tokenService.deleteToken(body));
  }

  @Test
  public void deleteTokenWhenExists() {
    UUID body = UUID.randomUUID();
    when(tokenRepository.existsByBody(body)).thenReturn(true);
    tokenService.deleteToken(body);
    verify(tokenRepository).deleteByBody(body);
  }
}
