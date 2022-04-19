package com.mygroup.watchlist.front.validation;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;

public class NotBlankStringValidator implements Validator<String> {

  private final String message;

  public NotBlankStringValidator(String message) {
    this.message = message;
  }

  @Override
  public ValidationResult apply(String value, ValueContext context) {
    if ((value != null) && !value.isBlank()) {
      return ValidationResult.ok();
    }
    return ValidationResult.error(message);
  }

}
