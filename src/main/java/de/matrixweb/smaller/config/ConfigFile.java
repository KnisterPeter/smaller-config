package de.matrixweb.smaller.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.yaml.snakeyaml.Yaml;

/**
 * @author markusw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigFile {

  @JsonProperty("dev-server")
  private DevServer devServer;

  @TypeHint(type = Environment.class)
  private Map<String, Environment> environments;

  /**
   * @param file
   * @return Returns the read {@link ConfigFile}
   * @throws IOException
   */
  public static ConfigFile read(final File file) throws IOException {
    try {
      return new ObjectMapper().readValue(file, ConfigFile.class);
    } catch (final JsonParseException e) {
      return readYaml(file);
    } catch (final JsonMappingException e) {
      return readYaml(file);
    }
  }

  @SuppressWarnings("unchecked")
  private static ConfigFile readYaml(final File file) throws IOException {
    class Mapper {
      <T> T map(final Class<T> clazz, final Map<String, Object> map) {
        try {
          final T object = clazz.newInstance();

          for (final Field field : clazz.getDeclaredFields()) {
            final String name = getMappedName(field);
            if (map.containsKey(name)) {
              Object value = map.get(name);
              if (value instanceof Map) {
                if (Map.class.isAssignableFrom(field.getType())) {
                  value = mapToMap(field, (Map<String, Object>) value);
                } else {
                  value = map(field.getType(), (Map<String, Object>) value);
                }
              }
              final boolean accessible = field.isAccessible();
              try {
                field.setAccessible(true);
                value = convertCollectionToArray(field.getType(), value);
                field.set(object, value);
              } catch (final IllegalAccessException e) {
                throw new RuntimeException(e);
              } finally {
                field.setAccessible(accessible);
              }
            }
          }

          return object;
        } catch (final InstantiationException e) {
          throw new RuntimeException(e);
        } catch (final IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }

      private String getMappedName(final Field field) {
        String name = field.getName();
        if (field.isAnnotationPresent(JsonProperty.class)) {
          name = field.getAnnotation(JsonProperty.class).value();
        }
        return name;
      }

      private Map<String, Object> mapToMap(final Field field,
          final Map<String, Object> value) {
        Map<String, Object> result = value;
        if (field.isAnnotationPresent(TypeHint.class)) {
          final Class<?> type = field.getAnnotation(TypeHint.class).type();
          final Map<String, Object> valueMap = new HashMap<String, Object>();
          for (final Entry<String, Object> entry : result.entrySet()) {
            final Object entryValue = entry.getValue();
            if (entryValue instanceof Map) {
              valueMap.put(entry.getKey(),
                  map(type, (Map<String, Object>) entry.getValue()));
            } else {
              valueMap.put(entry.getKey(),
                  convertCollectionToArray(type, entryValue));
            }
          }
          result = valueMap;
        }
        return result;
      }

      private Object convertCollectionToArray(final Class<?> type,
          final Object value) {
        Object result = value;
        if (type.isArray() && result instanceof Collection) {
          result = ((Collection<?>) result).toArray((Object[]) Array
              .newInstance(type.getComponentType(), 0));
        }
        return result;
      }

    }

    final FileReader reader = new FileReader(file);
    try {
      return new Mapper().map(ConfigFile.class,
          (Map<String, Object>) new Yaml().load(reader));
    } finally {
      reader.close();
    }
  }

  /**
   * @return the devServer
   */
  public DevServer getDevServer() {
    return this.devServer;
  }

  /**
   * @param devServer
   *          the devServer to set
   */
  public void setDevServer(final DevServer devServer) {
    this.devServer = devServer;
  }

  /**
   * @return the environments
   */
  public Map<String, Environment> getEnvironments() {
    return this.environments;
  }

  /**
   * @param environments
   *          the environments to set
   */
  public void setEnvironments(final Map<String, Environment> environments) {
    this.environments = environments;
  }

}
