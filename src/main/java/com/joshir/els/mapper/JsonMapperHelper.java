package com.joshir.els.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.joshir.els.mapper.exceptions.MappingException;

import java.util.Arrays;
import java.util.List;

public class JsonMapperHelper {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    objectMapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
    objectMapper.findAndRegisterModules();
  }

  public static <T> List<T> parseJsonArray(String json,
                                           Class<T> classOnWhichArrayIsDefined)
          throws MappingException, ClassNotFoundException, JsonProcessingException {
    Class<T[]> arrayClass = (Class<T[]>) Class.forName("[L" + classOnWhichArrayIsDefined.getName() + ";");
    T[] objects = objectMapper.readValue(json, arrayClass);
    return Arrays.asList(objects);
  }

  public static <T> T readFromJson(String json, Class<T> clazz) throws MappingException {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (Exception e) {
      throw new MappingException(e);
    }
  }

  public static String writeToJson(Object obj) throws MappingException {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw new MappingException(e);
    }
  }
}