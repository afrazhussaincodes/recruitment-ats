pipeline {
    agent any

    parameters {
        string(name: 'DEPLOY_DIR', defaultValue: 'C:\\deploy\\recruitment-ats',
               description: 'Deployment directory')
        string(name: 'APP_PORT', defaultValue: '8080',
               description: 'Spring Boot port')
    }

    environment {
        ARTIFACT = 'target/recruitment-ats-0.0.1-SNAPSHOT.jar'
    }

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Build & Test') {
            steps { bat 'mvnw.cmd clean test' }
        }

        stage('Package') {
            steps { bat 'mvnw.cmd package -DskipTests' }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Deploy') {
            steps {
                bat '''
                    if not exist "%DEPLOY_DIR%" mkdir "%DEPLOY_DIR%"
                    copy /Y "%WORKSPACE%\\%ARTIFACT%" "%DEPLOY_DIR%\\recruitment-ats.jar"
                '''
            }
        }

        stage('Deployment Evidence') {
            steps {
                echo "Deployed JAR: ${params.DEPLOY_DIR}\\recruitment-ats.jar"
                echo "Nginx should reverse proxy to the Spring Boot service on port ${params.APP_PORT}."
            }
        }
    }

    post {
        success { echo 'Recruitment ATS CI/CD pipeline completed successfully.' }
        failure { echo 'Pipeline failed. Check the stage console output.' }
    }
}
