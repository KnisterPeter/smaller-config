package de.matrixweb.smaller.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author markusw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Processor {

  private String src;

  private String dest;

  private Map<String, Object> options;

  /**
   * @return the src
   */
  public String getSrc() {
    return this.src;
  }

  /**
   * @param src
   *          the src to set
   */
  public void setSrc(final String src) {
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
    final Map<String, Option> map = new HashMap<String, Option>();
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
      return this.value != null ? Boolean.valueOf(this.value.toString()) : null;
    }

    /**
     * @return The integer
     */
    public Integer getInteger() {
      return this.value != null ? Integer.valueOf(this.value.toString()) : null;
    }

    /**
     * @return The double
     */
    public Double getDouble() {
      return this.value != null ? Double.valueOf(this.value.toString()) : null;
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
