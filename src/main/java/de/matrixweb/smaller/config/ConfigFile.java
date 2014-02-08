package de.matrixweb.smaller.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.matrixweb.smaller.config.Processor.Option;

/**
 * @author markusw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigFile {

  @JsonProperty("build-server")
  private BuildServer buildServer;

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
    return read(file.toURI().toURL());
  }

  /**
   * @param url
   * @return Returns the read {@link ConfigFile}
   * @throws IOException
   */
  public static ConfigFile read(final URL url) throws IOException {
    try {
      return new ObjectMapper().readValue(url, ConfigFile.class);
    } catch (final JsonProcessingException e) {
      return readYaml(url);
    }
  }

  @SuppressWarnings("unchecked")
  private static ConfigFile readYaml(final URL url) throws IOException {
    class OptionConstructor extends Constructor {
      public OptionConstructor() {
        this.yamlConstructors.put(new Tag("!option"), new ConstructOption());
      }

      class ConstructOption extends AbstractConstruct {
        @Override
        public Object construct(final Node node) {
          switch (node.getNodeId()) {
          case sequence:
            return constructSequence((SequenceNode) node);
          case mapping:
            return constructMapping((MappingNode) node);
          case scalar:
            return constructScalar((ScalarNode) node);
          default:
            return OptionConstructor.super.constructObject(node);
          }
        }
      }
    }
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

    final InputStream in = url.openStream();
    try {
      final Object object = new Yaml(new OptionConstructor()).load(in);
      if (object instanceof ConfigFile) {
        return (ConfigFile) object;
      }
      return new Mapper().map(ConfigFile.class, (Map<String, Object>) object);
    } finally {
      in.close();
    }
  }

  /**
   * @return Returns this config file instance as yaml string
   */
  public String dumpYaml() {
    class OptionRepresenter extends Representer {
      private OptionRepresenter() {
        this.representers.put(Option.class, new RepresentOption());
      }

      class RepresentOption implements Represent {
        @Override
        @SuppressWarnings("rawtypes")
        public Node representData(final Object data) {
          final Object value = ((Option) data).getValue();
          if (value instanceof Iterable) {
            return representSequence(new Tag("!option"), (Iterable) value, true);
          } else if (value instanceof Map) {
            return representMapping(new Tag("!option"), (Map) value, true);
          } else if (value instanceof String) {
            return representScalar(new Tag("!option"), (String) value);
          }
          return OptionRepresenter.super.representData(value);
        }

      }
    }
    return new Yaml(new OptionRepresenter(), new DumperOptions()).dump(this);
  }

  /**
   * @return the buildServer
   */
  public BuildServer getBuildServer() {
    if (this.buildServer == null) {
      this.buildServer = new BuildServer();
    }
    return this.buildServer;
  }

  /**
   * @param buildServer
   *          the buildServer to set
   */
  public void setBuildServer(final BuildServer buildServer) {
    this.buildServer = buildServer;
  }

  /**
   * @return the devServer
   */
  public DevServer getDevServer() {
    if (this.devServer == null) {
      this.devServer = new DevServer();
    }
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
    if (this.environments == null) {
      this.environments = new HashMap<String, Environment>();
      this.environments.put("first", new Environment());
    }
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
