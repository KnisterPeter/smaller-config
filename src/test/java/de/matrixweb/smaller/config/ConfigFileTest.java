package de.matrixweb.smaller.config;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

/**
 * @author markusw
 */
public class ConfigFileTest {

  /**
   * @throws IOException
   */
  @Test
  public void testJson() throws IOException {
    final File file = new File("src/test/resources/example.json");
    final ConfigFile config = ConfigFile.read(file);
    configFileAssertions(config);
  }

  /**
   * @throws IOException
   */
  @Test
  public void testYaml() throws IOException {
    final File file = new File("src/test/resources/example.yml");
    final ConfigFile config = ConfigFile.read(file);
    configFileAssertions(config);
  }

  private void configFileAssertions(final ConfigFile config) {
    assertThat(config.getDevServer(), is(not(nullValue())));
    assertThat(config.getDevServer().getIp(), is("0.0.0.0"));
    assertThat(config.getDevServer().getPort(), is(12345));
    assertThat(config.getDevServer().getProxyhost(), is("localhost"));
    assertThat(config.getDevServer().getProxyport(), is(3000));
    assertThat(config.getDevServer().isDebug(), is(true));
    assertThat(config.getDevServer().getProcess()[0], is("/app.js"));
    assertThat(config.getDevServer().getProcess()[1], is("/style.css"));
    assertThat(config.getDevServer().getTemplateEngine(), is("handlebars"));
    assertThat(config.getDevServer().isLiveReload(), is(true));
    assertThat(config.getDevServer().getTests().getFramework(), is("jasmine"));
    assertThat(config.getDevServer().getTests().getFolder(), is("./tests"));
    assertThat(config.getFiles().getFolder()[0], is("dir1"));
    assertThat(config.getFiles().getFolder()[1], is("dir2"));
    assertThat(config.getFiles().getIncludes()[0], is("**/*.coffee"));
    assertThat(config.getFiles().getIncludes()[1], is("**/*.less"));
    assertThat(config.getFiles().getExcludes()[0], is("**/*.bin"));
    assertThat(config.getTasks().size(), is(3));
    assertThat(config.getTasks().get("coffeeScript").getSrc()[0],
        is("**/*.coffee"));
    assertThat(config.getTasks().get("coffeeScript").getDest(), is("."));
    assertThat(
        config.getTasks().get("coffeeScript").getOptions().get("source-maps")
            .getBoolean(), is(true));
    assertThat(config.getTasks().get("coffeeScript").getOptions().get("bare")
        .getBoolean(), is(true));
    assertThat(config.getTasks().get("browserify").getOptions().get("aliases")
        .getMap().containsKey("./some-file"), is(true));
    assertThat(config.getTasks().get("browserify").getOptions().get("aliases")
        .getMap().get("./some-file").toString(), is("library"));
    assertThat(config.getProcessors().size(), is(2));
    assertThat(config.getProcessors().get("js")[0], is("coffeeScript"));
    assertThat(config.getProcessors().get("js")[1], is("browserify"));
    assertThat(config.getProcessors().get("css")[0], is("lessjs"));
  }
}
