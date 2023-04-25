package com.joshir.els.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.joshir.els.mapper.exceptions.MappingException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class JsonMapperHelper {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    objectMapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
    objectMapper.findAndRegisterModules();
  }

  public static <T> List<T> parseJsonArray(String json, Class<T> classOnWhichArrayIsDefined) throws MappingException {
    Class<T[]> arrayClass = null ;
    T[] objects = null;
    try {
      Objects.requireNonNull(classOnWhichArrayIsDefined);
      arrayClass = (Class<T[]>) Class.forName("[L" + classOnWhichArrayIsDefined.getName() + ";");
      objects = objectMapper.readValue(json, arrayClass);
    } catch(ClassNotFoundException ex ){
      throw new MappingException("Class " + classOnWhichArrayIsDefined.getName()+ "not found.");
    } catch ( JsonProcessingException ex ){
      throw new MappingException(ex);
    }
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