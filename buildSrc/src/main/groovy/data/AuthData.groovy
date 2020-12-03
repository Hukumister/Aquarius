package data

import org.gradle.api.Project
import org.gradle.api.logging.Logging

class AuthData {

    final String user
    final String key


    static AuthData fromProject(final Project project) {
        def fileProp = tryToReadFromFile(project)
        String user
        String key

        if (fileProp.isEmpty()) {
            user = project.findProperty('BINTRAY_USER')
            key = project.findProperty('BINTRAY_KEY')
        } else {
            user = fileProp.getProperty('BINTRAY_USER')
            key = fileProp.getProperty('BINTRAY_KEY')
        }
        if (user.isEmpty() || key.isEmpty()) {
            Logging.getLogger(AuthData).warn("user or key is empty")
        }
        return new AuthData(user, key)
    }

    AuthData(user, key) {
        this.user = user
        this.key = key
    }

    private static def tryToReadFromFile(final Project project) {
        Properties properties = new Properties()
        def file = project.rootProject.file('local.properties')
        if (file.exists()) {
            properties.load(file.newInputStream())
        } else {
            Logging.getLogger(AuthData).debug("file local.properties is not found")
        }
        return properties
    }
}