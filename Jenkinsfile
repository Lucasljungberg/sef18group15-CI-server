pipeline {
    agent any
    tools {
        maven "maven 3.5.2"
        jdk "jdk 8"
    }
    stages {
        stage("Test") {
            steps {
                bat "mvn test"
            }
        }
        stage("Build") {
            steps {
                bat "mvn package"
            }
        }
        stage("Archive") {
            steps {
                archiveArtifacts "target/*"
            }
        }
    }
}
