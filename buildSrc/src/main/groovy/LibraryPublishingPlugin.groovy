import com.jfrog.bintray.gradle.BintrayPlugin
import data.AuthData
import data.PublishData
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import task.*

class LibraryPublishingPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.plugins.apply(BintrayPlugin)
        project.plugins.apply(MavenPublishPlugin)

        configureProject(project)
        setupPublish(project)
    }

    private setupPublish(Project project) {
        project.afterEvaluate {

            def authData = AuthData.fromProject(project)
            def publishData = PublishData.fromProject(project)

            project.publishing {
                publications {
                    release(MavenPublication) {
                        groupId project.group
                        artifactId project.name
                        version project.version

                        from project.components["release"]
                        artifact("sourcesJar")
                        artifact("javadocsJar")
                    }
                }
            }

            project.bintray {
                user = authData.user
                key = authData.key
                publish = true
                configurations = [Dependency.ARCHIVES_CONFIGURATION]
                publications = ['release']

                pkg {
                    name = project.name
                    repo = publishData.repository
                    githubRepo = publishData.githubRepository
                    licenses = [publishData.licenseName]
                    vcsUrl = publishData.vcsUrl
                    issueTrackerUrl = publishData.issueTrackerUrl

                    version {
                        name = project.version
                        released = new Date()
                    }
                }
            }
        }
    }

    private configureProject(Project project) {
        if (project.plugins.hasPlugin("java-library")) {

            def sourcesJar = project.tasks.register("sourcesJar", SourceJarTask.class)
            def javadocsJar = project.tasks.register("javadocsJar", JavadocsJarTask.class)

            project.artifacts.add(Dependency.ARCHIVES_CONFIGURATION, sourcesJar)
            project.artifacts.add(Dependency.ARCHIVES_CONFIGURATION, javadocsJar)
        } else if (project.plugins.hasPlugin("com.android.library")) {

            project.tasks.register("androidJavadocs", AndroidJavadocsTask.class)

            def androidJavadocsJar = project.tasks.register("androidJavadocsJar", AndroidJavadocsJarTask.class)
            def androidSourcesJar = project.tasks.register("androidSourcesJar", AndroidSourcesJarTack.class)

            project.artifacts.add(Dependency.ARCHIVES_CONFIGURATION, androidJavadocsJar)
            project.artifacts.add(Dependency.ARCHIVES_CONFIGURATION, androidSourcesJar)
        }
    }
}