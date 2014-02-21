package de.matrixweb.smaller.config;

import java.io.File;
import java.io.IOException;
import java.util.Map;

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

  @SuppressWarnings("unchecked")
  private void configFileAssertions(final ConfigFile config) {
    assertThat(config.getBuildServer(), is(not(nullValue())));
    assertThat(config.getBuildServer().isOutputOnly(), is(true));
    assertThat(config.getBuildServer().getEnvironments().length, is(1));
    assertThat(config.getBuildServer().getEnvironments()[0], is("second"));

    assertThat(config.getDevServer(), is(not(nullValue())));
    assertThat(config.getDevServer().getIp(), is("0.0.0.0"));
    assertThat(config.getDevServer().getPort(), is(12345));
    assertThat(config.getDevServer().getProxyhost(), is("localhost"));
    assertThat(config.getDevServer().getProxyport(), is(3000));
    assertThat(config.getDevServer().isDebug(), is(true));
    assertThat(config.getDevServer().isLiveReload(), is(true));
    assertThat(config.getDevServer().getEnvironments().length, is(1));
    assertThat(config.getDevServer().getEnvironments()[0], is("first"));
    assertThat(config.getDevServer().getStaticFiles().getFolder()[0],
        is("dir1"));
    assertThat(config.getDevServer().getStaticFiles().getFolder()[1],
        is("dir2"));
    assertThat(config.getDevServer().getStaticFiles().getIncludes()[0],
        is("**/*.coffee"));
    assertThat(config.getDevServer().getStaticFiles().getExcludes()[0],
        is("**/*.bin"));

    final Environment env = config.getEnvironments().get("first");
    assertThat(env.getProcess(), is("/app.js"));
    assertThat(env.getTemplateEngine(), is("handlebars"));
    assertThat(env.getTestFramework(), is("jasmine"));
    assertThat(env.getFiles().getFolder()[0], is("dir1"));
    assertThat(env.getFiles().getFolder()[1], is("dir2"));
    assertThat(env.getFiles().getIncludes()[0], is("**/*.coffee"));
    assertThat(env.getFiles().getExcludes()[0], is("**/*.bin"));
    assertThat(env.getTestFiles().getFolder()[0], is("./tests"));
    assertThat(env.getProcessors().size(), is(2));
    assertThat(env.getProcessors().get("coffeeScript").getSrc(),
        is("/main.coffee"));
    assertThat(
        env.getProcessors().get("coffeeScript").getOptions().get("source-maps")
            .getBoolean(), is(true));
    assertThat(env.getProcessors().get("coffeeScript").getOptions().get("bare")
        .getBoolean(), is(true));

    Map<String, Object> browserifyAliases = env.getProcessors()
        .get("browserify").getOptions().get("aliases").getMap();
    assertThat(browserifyAliases.containsKey("./some-file"), is(true));
    assertThat(browserifyAliases.get("./some-file").toString(), is("library"));
    browserifyAliases = (Map<String, Object>) env.getProcessors()
        .get("browserify").getPlainOptions().get("aliases");
    assertThat(browserifyAliases.containsKey("./some-file"), is(true));
    assertThat(browserifyAliases.get("./some-file").toString(), is("library"));

    assertThat(env.getPipeline().length, is(2));
    assertThat(env.getPipeline()[0], is("coffeeScript"));
    assertThat(env.getPipeline()[1], is("browserify"));

    final Environment second = config.getEnvironments().get("second");
    assertThat(second.getInherit(), is("first"));
    assertThat(second.getFiles().getFolder().length, is(1));
    assertThat(second.getFiles().getFolder()[0], is("."));
    assertThat(second.getProcessors().size(), is(3));
    assertThat(second.getProcessors().get("uglifyjs"), is(notNullValue()));
    assertThat(second.getPipeline().length, is(3));
  }

}
