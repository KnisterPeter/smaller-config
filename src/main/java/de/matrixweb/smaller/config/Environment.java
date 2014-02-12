package de.matrixweb.smaller.config;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author markusw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Environment {

  private ConfigFile configFile;

  private String inherit;

  private String process;

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
   * @return the configFile
   */
  public ConfigFile getConfigFile() {
    return this.configFile;
  }

  /**
   * @param configFile
   *          the configFile to set
   */
  public void setConfigFile(final ConfigFile configFile) {
    this.configFile = configFile;
  }

  /**
   * @return the inherit
   */
  public String getInherit() {
    return this.inherit;
  }

  /**
   * @param inherit
   *          the inherit to set
   */
  public void setInherit(final String inherit) {
    this.inherit = inherit;
  }

  /**
   * @return the process
   */
  public String getProcess() {
    if (this.process == null) {
      final Environment env = getInherited();
      if (env != null) {
        return env.getProcess();
      }
    }
    return this.process;
  }

  /**
   * @param process
   *          the process to set
   */
  public void setProcess(final String process) {
    this.process = process;
  }

  /**
   * @return the templateEngine
   */
  public String getTemplateEngine() {
    if (this.templateEngine == null) {
      final Environment env = getInherited();
      if (env != null) {
        return env.getTemplateEngine();
      }
    }
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
    if (this.testFramework == null) {
      final Environment env = getInherited();
      if (env != null) {
        return env.getTestFramework();
      }
    }
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
      final Environment env = getInherited();
      if (env != null) {
        this.files = env.getFiles();
      }
    }
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
      final Environment env = getInherited();
      if (env != null) {
        this.testFiles = env.getTestFiles();
      }
    }
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
    final Environment env = getInherited();
    if (env != null) {
      final Map<String, Processor> inherited = env.getProcessors();
      if (inherited != null) {
        this.processors.putAll(inherited);
      }
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
    if (this.pipeline == null) {
      final Environment env = getInherited();
      if (env != null) {
        return env.getPipeline();
      }
    }
    return this.pipeline;
  }

  /**
   * @param pipeline
   *          the pipeline to set
   */
  public void setPipeline(final String[] pipeline) {
    this.pipeline = pipeline;
  }

  private Environment getInherited() {
    if (this.inherit != null) {
      return getConfigFile().getEnvironments().get(this.inherit);
    }
    return null;
  }

}
