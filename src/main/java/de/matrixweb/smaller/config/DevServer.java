package de.matrixweb.smaller.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author markusw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DevServer {

  private String ip = "0.0.0.0";

  private int port = 12345;

  private String proxyhost = "localhost";

  private int proxyport = 80;

  private boolean debug;

  @JsonProperty("live-reload")
  private boolean liveReload;

  @JsonProperty("force-full-reload")
  private boolean forceFullReload;

  @JsonProperty("inject-partials")
  private boolean injectPartials;

  private String[] environments;

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
   * @return the forceFullReload
   */
  public boolean isForceFullReload() {
    return this.forceFullReload;
  }

  /**
   * @param forceFullReload
   *          the forceFullReload to set
   */
  public void setForceFullReload(final boolean forceFullReload) {
    this.forceFullReload = forceFullReload;
  }

  /**
   * @return the injectPartials
   */
  public boolean isInjectPartials() {
    return this.injectPartials;
  }

  /**
   * @param injectPartials
   *          the injectPartials to set
   */
  public void setInjectPartials(final boolean injectPartials) {
    this.injectPartials = injectPartials;
  }

  /**
   * @return the environments
   */
  public String[] getEnvironments() {
    return this.environments;
  }

  /**
   * @param environments
   *          the environments to set
   */
  public void setEnvironments(final String[] environments) {
    this.environments = environments;
  }

}
