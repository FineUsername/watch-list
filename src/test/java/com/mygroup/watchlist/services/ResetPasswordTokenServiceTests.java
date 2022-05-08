package com.mygroup.watchlist.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.mygroup.watchlist.back.entities.ResetPasswordToken;
import com.mygroup.watchlist.back.repositories.ResetPasswordTokenRepository;
import com.mygroup.watchlist.back.services.ResetPasswordTokenService;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ResetPasswordTokenServiceTests {

  @Mock
  private ResetPasswordTokenRepository tokenRepository;

  @InjectMocks
  private ResetPasswordTokenService tokenService;

  @Test
  public void findByBodyWhenNoneExist() {
    UUID body = UUID.randomUUID();
    when(tokenRepository.findByBody(body)).thenReturn(Optional.empty());
    assertThrows(NoSuchElementException.class, () -> tokenService.findByBody(body));
  }

  @Test
  public void findByBodyWhenExists() {
    ResetPasswordToken token = new ResetPasswordToken();
    when(tokenRepository.findByBody(token.getBody())).thenReturn(Optional.of(token));
    ResetPasswordToken actualToken = tokenService.findByBody(token.getBody());
    assertEquals(token.getBody(), actualToken.getBody());
  }

  @Test
  public void deleteTokenNullArg() {
    assertThrows(IllegalArgumentException.class, () -> tokenService.deleteToken(null));
  }

  @Test
  public void deleteTokenCommonCase() {
    ResetPasswordToken token = new ResetPasswordToken();
    doNothing().when(tokenRepository).delete(token);
    tokenService.deleteToken(token);
    verify(tokenRepository).delete(token);
  }
}
