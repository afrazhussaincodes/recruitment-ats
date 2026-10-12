
pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        DOCKER = 'C:\\Users\\admin\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe'
        REGISTRY = 'localhost:5000'
        IMAGE_NAME = 'recruitment-ats'
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

        stage('Verify Java, Maven and Docker') {
            steps {
                bat '''
                    java -version
                    .\\mvnw.cmd -version
                    "%DOCKER%" version
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

        stage('Build Versioned Docker Image') {
            steps {
                bat '''
                    "%DOCKER%" build -t "%REGISTRY%/%IMAGE_NAME%:%BUILD_NUMBER%" -t "%REGISTRY%/%IMAGE_NAME%:latest" .
                '''
            }
        }

        stage('Push Image to Local Registry') {
            steps {
                bat '''
                    "%DOCKER%" push "%REGISTRY%/%IMAGE_NAME%:%BUILD_NUMBER%"
                    "%DOCKER%" push "%REGISTRY%/%IMAGE_NAME%:latest"
                '''
            }
        }

        stage('Deploy Container') {
            steps {
                powershell '''
                    $ErrorActionPreference = "Stop"
                    $docker = $env:DOCKER
                    $image = "$($env:REGISTRY)/$($env:IMAGE_NAME):$($env:BUILD_NUMBER)"

                    # Keep the old container until the image has been built and pushed.
                    & $docker stop recruitment-ats 2>$null
                    & $docker rm recruitment-ats 2>$null

                    & $docker run -d `
                        --name recruitment-ats `
                        --restart unless-stopped `
                        -p 8080:8080 `
                        $image

                    if ($LASTEXITCODE -ne 0) {
                        throw "Failed to start the newly built container."
                    }
                '''
            }
        }

        stage('Check Application Health') {
            steps {
                powershell '''
                    $ErrorActionPreference = "Stop"
                    $healthy = $false

                    for ($i = 1; $i -le 30; $i++) {
                        try {
                            $response = Invoke-RestMethod `
                                -Uri $env:HEALTH_URL `
                                -TimeoutSec 5

                            if ($response.status -eq "UP") {
                                $healthy = $true
                                break
                            }
                        } catch {
                            Start-Sleep -Seconds 5
                        }
                    }

                    if (-not $healthy) {
                        & $env:DOCKER logs --tail 100 recruitment-ats
                        throw "Application health check failed."
                    }

                    Write-Host "Application health is UP."
                '''
            }
        }

        stage('Continuous UI Testing') {
            steps {
                bat '.\\mvnw.cmd -Dtest=SeleniumUITests test'
            }
        }

        stage('Verify Published Image') {
            steps {
                bat '''
                    "%DOCKER%" pull "%REGISTRY%/%IMAGE_NAME%:%BUILD_NUMBER%"
                    "%DOCKER%" images "%REGISTRY%/%IMAGE_NAME%"
                    "%DOCKER%" ps
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
            echo 'Week 12 Docker continuous deployment completed successfully.'
        }

        failure {
            echo 'Pipeline failed. Check Console Output and container logs.'
        }

        cleanup {
            echo 'Pipeline execution finished.'
        }
    }
}
