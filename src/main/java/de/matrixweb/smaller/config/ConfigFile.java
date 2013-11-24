package de.matrixweb.smaller.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
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

  private Files files;

  @TypeHint(type = Task.class)
  private Map<String, Task> tasks;

  @TypeHint(type = String[].class)
  private Map<String, String[]> processors;

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
   * @return the files
   */
  public Files getFiles() {
    return this.files;
  }

  /**
   * @param files
   *          the files to set
   */
  public void setFiles(final Files files) {
    this.files = files;
  }

  /**
   * @return the tasks
   */
  public Map<String, Task> getTasks() {
    return this.tasks;
  }

  /**
   * @param tasks
   *          the tasks to set
   */
  public void setTasks(final Map<String, Task> tasks) {
    this.tasks = tasks;
  }

  /**
   * @return the processors
   */
  public Map<String, String[]> getProcessors() {
    return this.processors;
  }

  /**
   * @param processors
   *          the processors to set
   */
  public void setProcessors(final Map<String, String[]> processors) {
    this.processors = processors;
  }

  /** */
  @Target({ ElementType.FIELD })
  @Retention(RetentionPolicy.RUNTIME)
  public static @interface TypeHint {

    /** */
    Class<?> type();

  }

  /** */
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class DevServer {

    private String ip;

    private int port;

    private String proxyhost;

    private int proxyport;

    private boolean debug;

    private String[] process;

    @JsonProperty("templates")
    private String templateEngine;

    @JsonProperty("live-reload")
    private boolean liveReload;

    private Tests tests;

    /**
     * @return the ip
     */
    public String getIp() {
      return this.ip;
    }

    /**
     * @param ip
     *          the ip to set
     */
    public void setIp(final String ip) {
      this.ip = ip;
    }

    /**
     * @return the port
     */
    public int getPort() {
      return this.port;
    }

    /**
     * @param port
     *          the port to set
     */
    public void setPort(final int port) {
      this.port = port;
    }

    /**
     * @return the proxyhost
     */
    public String getProxyhost() {
      return this.proxyhost;
    }

    /**
     * @param proxyhost
     *          the proxyhost to set
     */
    public void setProxyhost(final String proxyhost) {
      this.proxyhost = proxyhost;
    }

    /**
     * @return the proxyport
     */
    public int getProxyport() {
      return this.proxyport;
    }

    /**
     * @param proxyport
     *          the proxyport to set
     */
    public void setProxyport(final int proxyport) {
      this.proxyport = proxyport;
    }

    /**
     * @return the debug
     */
    public boolean isDebug() {
      return this.debug;
    }

    /**
     * @param debug
     *          the debug to set
     */
    public void setDebug(final boolean debug) {
      this.debug = debug;
    }

    /**
     * @return the process
     */
    public String[] getProcess() {
      return this.process;
    }

    /**
     * @param process
     *          the process to set
     */
    public void setProcess(final String[] process) {
      this.process = process;
    }

    /**
     * @return the templateEngine
     */
    public String getTemplateEngine() {
      return this.templateEngine;
    }

    /**
     * @param templateEngine
     *          the templateEngine to set
     */
    public void setTemplateEngine(final String templateEngine) {
      this.templateEngine = templateEngine;
    }

    /**
     * @return the liveReload
     */
    public boolean isLiveReload() {
      return this.liveReload;
    }

    /**
     * @param liveReload
     *          the liveReload to set
     */
    public void setLiveReload(final boolean liveReload) {
      this.liveReload = liveReload;
    }

    /**
     * @return the tests
     */
    public Tests getTests() {
      return this.tests;
    }

    /**
     * @param tests
     *          the tests to set
     */
    public void setTests(final Tests tests) {
      this.tests = tests;
    }

    /** */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tests {

      private String framework;

      private String folder;

      /**
       * @return the framework
       */
      public String getFramework() {
        return this.framework;
      }

      /**
       * @param framework
       *          the framework to set
       */
      public void setFramework(final String framework) {
        this.framework = framework;
      }

      /**
       * @return the folder
       */
      public String getFolder() {
        return this.folder;
      }

      /**
       * @param folder
       *          the folder to set
       */
      public void setFolder(final String folder) {
        this.folder = folder;
      }

    }

  }

  /** */
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Files {

    private String[] folder;

    private String[] includes;

    private String[] excludes;

    /**
     * @return the folder
     */
    public String[] getFolder() {
      return this.folder;
    }

    /**
     * @param folder
     *          the folder to set
     */
    public void setFolder(final String[] folder) {
      this.folder = folder;
    }

    /**
     * @return the includes
     */
    public String[] getIncludes() {
      return this.includes;
    }

    /**
     * @param includes
     *          the includes to set
     */
    public void setIncludes(final String[] includes) {
      this.includes = includes;
    }

    /**
     * @return the excludes
     */
    public String[] getExcludes() {
      return this.excludes;
    }

    /**
     * @param excludes
     *          the excludes to set
     */
    public void setExcludes(final String[] excludes) {
      this.excludes = excludes;
    }

  }

  /** */
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Task {

    private String[] src;

    private String dest;

    private Map<String, Object> options;

    /**
     * @return the src
     */
    public String[] getSrc() {
      return this.src;
    }

    /**
     * @param src
     *          the src to set
     */
    public void setSrc(final String[] src) {
      this.src = src;
    }

    /**
     * @return the dest
     */
    public String getDest() {
      return this.dest;
    }

    /**
     * @param dest
     *          the dest to set
     */
    public void setDest(final String dest) {
      this.dest = dest;
    }

    /**
     * @return the options
     */
    public Map<String, Option> getOptions() {
      final Map<String, Option> map = new HashMap<String, ConfigFile.Task.Option>();
      for (final Entry<String, Object> entry : this.options.entrySet()) {
        map.put(entry.getKey(), new Option(entry.getValue()));
      }
      return map;
    }

    /**
     * @param options
     *          the options to set
     */
    public void setOptions(final Map<String, Object> options) {
      this.options = options;
    }

    /** */
    public static class Option {

      private Object value;

      private Option(final Object value) {
        this.value = value;
      }

      /**
       * @return the value
       */
      public Object getValue() {
        return this.value;
      }

      /**
       * @param value
       *          the value to set
       */
      public void setValue(final Object value) {
        this.value = value;
      }

      /**
       * @return The string
       */
      public String getString() {
        return this.value != null ? this.value.toString() : null;
      }

      /**
       * @return The boolean
       */
      public Boolean getBoolean() {
        return this.value != null ? Boolean.valueOf(this.value.toString())
            : null;
      }

      /**
       * @return The integer
       */
      public Integer getInteger() {
        return this.value != null ? Integer.valueOf(this.value.toString())
            : null;
      }

      /**
       * @return The double
       */
      public Double getDouble() {
        return this.value != null ? Double.valueOf(this.value.toString())
            : null;
      }

      /**
       * @return The map
       */
      @SuppressWarnings("unchecked")
      public Map<String, Object> getMap() {
        return (Map<String, Object>) this.value;
      }

    }

  }

}
