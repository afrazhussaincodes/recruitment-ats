
pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        APP_NAME = 'Recruitment-ATS'
        DEPLOY_DIR = 'C:\\deploy\\recruitment-ats'
        DEPLOY_JAR = 'C:\\deploy\\recruitment-ats\\recruitment-ats.jar'
        APP_URL = 'http://localhost:8080'
        HEALTH_URL = 'http://localhost:8080/actuator/health'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
                bat 'git status --short --branch'
            }
        }

        stage('Verify Java and Maven') {
            steps {
                bat '''
                    java -version
                    .\\mvnw.cmd -version
                '''
            }
        }

        stage('Build and Unit Tests') {
            steps {
                bat '.\\mvnw.cmd clean test'
            }
        }

        stage('Package Application') {
            steps {
                bat '.\\mvnw.cmd package -DskipTests'
            }
        }

        stage('Archive Application JAR') {
            steps {
                archiveArtifacts(
                    artifacts: 'target/recruitment-ats-0.0.1-SNAPSHOT.jar',
                    fingerprint: true
                )
            }
        }

        stage('Deploy Application JAR') {
            steps {
                powershell '''
                    $ErrorActionPreference = "Stop"

                    $sourceJar = Join-Path $env:WORKSPACE `
                        "target\\recruitment-ats-0.0.1-SNAPSHOT.jar"

                    $deployDir = $env:DEPLOY_DIR
                    $destinationJar = $env:DEPLOY_JAR

                    if (-not (Test-Path $sourceJar)) {
                        throw "Build JAR not found: $sourceJar"
                    }

                    New-Item -ItemType Directory `
                        -Force -Path $deployDir | Out-Null

                    Copy-Item -Path $sourceJar `
                        -Destination $destinationJar -Force

                    Write-Host "JAR deployed to $destinationJar"
                '''
            }
        }

        stage('Check Application Health') {
            steps {
                powershell '''
                    $ErrorActionPreference = "Stop"
                    $healthUrl = $env:HEALTH_URL

                    try {
                        $response = Invoke-RestMethod `
                            -Uri $healthUrl -TimeoutSec 10

                        if ($response.status -ne "UP") {
                            throw "Application health is not UP."
                        }

                        Write-Host "Application health check passed."
                    }
                    catch {
                        Write-Host "Health endpoint is not responding."
                        Write-Host "The deployment stage copies the JAR but does not start it."
                        throw
                    }
                '''
            }
        }

        stage('Continuous UI Testing') {
            steps {
                powershell '''
                    $ErrorActionPreference = "Stop"

                    $healthUrl = $env:HEALTH_URL
                    $health = Invoke-RestMethod `
                        -Uri $healthUrl -TimeoutSec 10

                    if ($health.status -ne "UP") {
                        throw "Application must be UP before Selenium tests."
                    }

                    .\\mvnw.cmd -Dtest=SeleniumUITests test

                    if ($LASTEXITCODE -ne 0) {
                        throw "Selenium UI tests failed."
                    }
                '''
            }
        }
    }

    post {
        always {
            junit(
                testResults: 'target/surefire-reports/*.xml',
                allowEmptyResults: true
            )

            archiveArtifacts(
                artifacts: 'target/surefire-reports/**',
                allowEmptyArchive: true,
                fingerprint: true
            )
        }

        success {
            echo 'Recruitment ATS pipeline completed successfully.'
        }

        failure {
            echo 'Pipeline failed. Review Console Output and test reports.'
        }

        cleanup {
            echo 'Pipeline execution finished.'
        }
    }
}
