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
                bat 'mvn -B -Dci.headless=true -Dheadless=true -Duse.remote=false -Dsurefire.suiteXmlFiles=testng.xml test'
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

    /* POST BUILD ACTIONS */
    post {

        success {
            emailext(
                subject: "BUILD SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
Hi Ganesh,

Build Status: SUCCESS

Job Name: ${env.JOB_NAME}
Build Number: ${env.BUILD_NUMBER}

Build URL:
${env.BUILD_URL}

Extent Report:
${env.BUILD_URL}Extent_20Report/

Regards,
Jenkins
""",
                to: 'ganukanchi2018@gmail.com'
            )
        }

        failure {
            emailext(
                subject: "BUILD FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
Hi Ganesh,

Build Status: FAILED

Job Name: ${env.JOB_NAME}
Build Number: ${env.BUILD_NUMBER}

Check logs here:
${env.BUILD_URL}

Regards,
Jenkins
""",
                to: 'ganukanchi2018@gmail.com'
            )
        }

        always {
            junit 'target/surefire-reports/*.xml'
        }
    }
}
