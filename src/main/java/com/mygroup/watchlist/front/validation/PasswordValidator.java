package com.mygroup.watchlist.front.validation;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;

public class PasswordValidator implements Validator<String> {

  private static final String ERROR_MESSAGE = "Password must contain at least %d charaters";
  private static final int MIN_LENGTH = 8;

  @Override
  public ValidationResult apply(String value, ValueContext context) {
    if ((value != null) && (value.length() >= MIN_LENGTH)) {
      return ValidationResult.ok();
    }
    return ValidationResult.error(String.format(ERROR_MESSAGE, MIN_LENGTH));
  }
}
