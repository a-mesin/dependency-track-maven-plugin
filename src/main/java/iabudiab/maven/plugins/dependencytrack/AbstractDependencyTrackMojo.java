package iabudiab.maven.plugins.dependencytrack;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

import iabudiab.maven.plugins.dependencytrack.api.DTrackClient;

public abstract class AbstractDependencyTrackMojo extends AbstractMojo {

	@Parameter(property = "dependencyTrackUrl", required = true)
	private String dependencyTrackUrl;

	@Parameter(property = "dependencyTrackApiKey", required = true)
	private String dependencyTrackApiKey;

	@Parameter(defaultValue = "${project.groupId}.${project.artifactId}", property = "projectName", required = false)
	protected String projectName;

	@Parameter(defaultValue = "${project.version}", property = "projectVersion", required = false)
	protected String projectVersion;

	@Parameter(property = "failOnError", defaultValue = "true", required = true)
	private boolean failOnError;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		logConfiguration();

		try {
			DTrackClient client = new DTrackClient(dependencyTrackUrl, dependencyTrackApiKey, getLog());
			doWork(client);
		} catch (Exception e) {
			if (failOnError) {
				throw new MojoExecutionException("Error during plugin execution", e);
			} else {
				getLog().warn("failOnError: false => logging exception");
				getLog().warn("Error during plugin execution", e);
			}
		}
	}

	protected abstract void doWork(DTrackClient client) throws PluginException;

	private void logConfiguration() {
		getLog().info("DependencyTrack Maven Plugin");
		getLog().info("DependencyTrack URL   : " + dependencyTrackUrl);
		getLog().info("Project name          : " + projectName);
		getLog().info("Project version       : " + projectVersion);
	}
}