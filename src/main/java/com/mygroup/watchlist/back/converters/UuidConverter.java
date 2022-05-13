package com.mygroup.watchlist.back.converters;

import java.util.UUID;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class UuidConverter implements AttributeConverter<UUID, String> {

  @Override
  public String convertToDatabaseColumn(UUID entityValue) {
    if (entityValue == null) {
      return null;
    }
    return entityValue.toString();
  }

  @Override
  public UUID convertToEntityAttribute(String databaseValue) {
    if (databaseValue == null) {
      return null;
    }
    return UUID.fromString(databaseValue);
  }
}
