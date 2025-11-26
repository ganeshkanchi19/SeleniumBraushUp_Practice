pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk   'JDK17'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean compile -DskipTests=true'
            }
        }

        stage('Run Tests') {
            steps {
                bat 'mvn -Dsurefire.suiteXmlFiles=testng.xml test'
            }
        }

        stage('Archive Reports') {
            steps {
                archiveArtifacts artifacts: 'reports/**/*.html, Screenshots/**/*', allowEmptyArchive: true
            }
        }

        stage('Publish Extent HTML Report') {
            steps {
                publishHTML(target: [
                    reportDir: 'reports',
                    reportFiles: 'extent-report.html',
                    reportName: 'Extent Report'
                ])
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
    }
}
