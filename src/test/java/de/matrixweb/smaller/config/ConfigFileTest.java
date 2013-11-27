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
    assertThat(config.getDevServer().isLiveReload(), is(true));

    final Environment env = config.getEnvironments().get("first");
    assertThat(env.getProcess()[0], is("/app.js"));
    assertThat(env.getProcess()[1], is("/style.css"));
    assertThat(env.getTemplateEngine(), is("handlebars"));
    assertThat(env.getTestFramework(), is("jasmine"));
    assertThat(env.getFiles().getFolder()[0], is("dir1"));
    assertThat(env.getFiles().getFolder()[1], is("dir2"));
    assertThat(env.getFiles().getIncludes()[0], is("**/*.coffee"));
    assertThat(env.getFiles().getIncludes()[1], is("**/*.less"));
    assertThat(env.getFiles().getExcludes()[0], is("**/*.bin"));
    assertThat(env.getTestFiles().getFolder()[0], is("./tests"));
    assertThat(env.getProcessors().size(), is(3));
    assertThat(env.getProcessors().get("coffeeScript").getSrc()[0],
        is("**/*.coffee"));
    assertThat(env.getProcessors().get("coffeeScript").getDest(), is("."));
    assertThat(
        env.getProcessors().get("coffeeScript").getOptions().get("source-maps")
            .getBoolean(), is(true));
    assertThat(env.getProcessors().get("coffeeScript").getOptions().get("bare")
        .getBoolean(), is(true));
    assertThat(env.getProcessors().get("browserify").getOptions()
        .get("aliases").getMap().containsKey("./some-file"), is(true));
    assertThat(env.getProcessors().get("browserify").getOptions()
        .get("aliases").getMap().get("./some-file").toString(), is("library"));
    assertThat(env.getPipeline().size(), is(2));
    assertThat(env.getPipeline().get("js")[0], is("coffeeScript"));
    assertThat(env.getPipeline().get("js")[1], is("browserify"));
    assertThat(env.getPipeline().get("css")[0], is("lessjs"));
  }
}
