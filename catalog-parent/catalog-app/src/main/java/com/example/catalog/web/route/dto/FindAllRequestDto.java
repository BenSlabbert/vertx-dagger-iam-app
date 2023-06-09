package com.example.catalog.web.route.dto;

import com.example.commons.web.serialization.JsonWriter;
import io.vertx.core.json.JsonObject;

public record FindAllRequestDto() implements JsonWriter {

  public FindAllRequestDto(JsonObject jsonObject) {
    this();
  }

  @Override
  public JsonObject toJson() {
    return new JsonObject();
  }
}
