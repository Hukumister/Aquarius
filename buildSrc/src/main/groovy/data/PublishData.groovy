package data

import org.gradle.api.Project

class PublishData {

    final String licenseName
    final String vcsUrl
    final String issueTrackerUrl
    final String description
    final String repository
    final String githubRepository

    static PublishData fromProject(final Project project) {
        return new PublishData(
                project.findProperty("lib.licence.name"),
                project.findProperty("lib.vsc.url"),
                project.findProperty("lib.issue.tracker.url"),
                project.findProperty("lib.description"),
                project.findProperty("lib.repository.name"),
                project.findProperty("lib.github.repo"),
        )
    }

    PublishData(licenseName, vcsUrl, issueTrackerUrl, description, repository, githubRepository) {
        this.licenseName = licenseName
        this.vcsUrl = vcsUrl
        this.issueTrackerUrl = issueTrackerUrl
        this.description = description
        this.repository = repository
        this.githubRepository = githubRepository
    }
}