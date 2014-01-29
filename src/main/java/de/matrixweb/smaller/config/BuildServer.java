package de.matrixweb.smaller.config;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author markusw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildServer {

  @JsonProperty("output-only")
  boolean outputOnly = false;

  /**
   * @return the outputOnly
   */
  public boolean isOutputOnly() {
    return this.outputOnly;
  }

  /**
   * @param outputOnly
   *          the outputOnly to set
   */
  public void setOutputOnly(final boolean outputOnly) {
    this.outputOnly = outputOnly;
  }

}
