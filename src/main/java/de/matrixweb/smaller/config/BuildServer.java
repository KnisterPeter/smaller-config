package de.matrixweb.smaller.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
