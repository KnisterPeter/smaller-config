package de.matrixweb.smaller.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author markusw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Files {

  private String[] folder;

  private String[] includes;

  private String[] excludes;

  /**
   * @return the folder
   */
  public String[] getFolder() {
    if (this.folder == null) {
      return new String[0];
    }
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
