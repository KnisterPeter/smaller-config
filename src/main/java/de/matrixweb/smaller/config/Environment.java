package de.matrixweb.smaller.config;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author markusw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Environment {

  private String[] process;

  @JsonProperty("templates")
  private String templateEngine;

  @JsonProperty("test-framework")
  private String testFramework;

  private Files files;

  @JsonProperty("test-files")
  private Files testFiles;

  @TypeHint(type = Processor.class)
  private Map<String, Processor> processors;

  private String[] pipeline;

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
   * @return the testFramework
   */
  public String getTestFramework() {
    return this.testFramework;
  }

  /**
   * @param testFramework
   *          the testFramework to set
   */
  public void setTestFramework(final String testFramework) {
    this.testFramework = testFramework;
  }

  /**
   * @return the files
   */
  public Files getFiles() {
    if (this.files == null) {
      this.files = new Files();
    }
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
   * @return the testFiles
   */
  public Files getTestFiles() {
    if (this.testFiles == null) {
      this.testFiles = new Files();
    }
    return this.testFiles;
  }

  /**
   * @param testFiles
   *          the testFiles to set
   */
  public void setTestFiles(final Files testFiles) {
    this.testFiles = testFiles;
  }

  /**
   * @return the processors
   */
  public Map<String, Processor> getProcessors() {
    if (this.processors == null) {
      this.processors = new HashMap<String, Processor>();
    }
    return this.processors;
  }

  /**
   * @param processors
   *          the processors to set
   */
  public void setProcessors(final Map<String, Processor> processors) {
    this.processors = processors;
  }

  /**
   * @return the pipeline
   */
  public String[] getPipeline() {
    return this.pipeline;
  }

  /**
   * @param pipeline
   *          the pipeline to set
   */
  public void setPipeline(final String[] pipeline) {
    this.pipeline = pipeline;
  }

}
