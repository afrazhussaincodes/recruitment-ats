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
        APP_URL = 'http://localhost:8082'
        HEALTH_URL = 'http://localhost:8082/actuator/health'
        PROD_URL = 'http://localhost:8080'
        PROD_HEALTH_URL = 'http://localhost:8080/actuator/health'
        DB_URL = 'jdbc:mysql://host.docker.internal:3306/recruitment_ats?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true'
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
                bat '.\\mvnw.cmd clean test -Dtest=RecruitmentAtsApplicationTests'
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
                    "%DOCKER%" build ^
                      -t "%REGISTRY%/%IMAGE_NAME%:%BUILD_NUMBER%" ^
                      -t "%REGISTRY%/%IMAGE_NAME%:latest" .
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

        stage('Deploy Candidate') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'ats-mysql-credentials',
                        usernameVariable: 'ATS_DB_USERNAME',
                        passwordVariable: 'ATS_DB_PASSWORD'
                    )
                ]) {
                    powershell '''
                        $ErrorActionPreference = "Stop"
                        $docker = $env:DOCKER
                        $image = "$($env:REGISTRY)/$($env:IMAGE_NAME):$($env:BUILD_NUMBER)"

                        $containers = @(& $docker container ls -a --format "{{.Names}}")
                        if ($LASTEXITCODE -ne 0) {
                            throw "Unable to list Docker containers."
                        }

                        if ($containers -contains "recruitment-ats-candidate") {
                            & $docker rm -f recruitment-ats-candidate
                            if ($LASTEXITCODE -ne 0) {
                                throw "Could not remove the previous candidate container."
                            }
                        }

                        & $docker run -d `
                            --name recruitment-ats-candidate `
                            --restart unless-stopped `
                            --add-host host.docker.internal:host-gateway `
                            -p 8082:8080 `
                            -e "SPRING_DATASOURCE_URL=$env:DB_URL" `
                            -e "SPRING_DATASOURCE_USERNAME=$env:ATS_DB_USERNAME" `
                            -e "SPRING_DATASOURCE_PASSWORD=$env:ATS_DB_PASSWORD" `
                            $image

                        if ($LASTEXITCODE -ne 0) {
                            throw "Candidate container failed to start."
                        }

                        Write-Host "Candidate deployed on port 8082."
                    '''
                }
            }
        }

        stage('Check Candidate Health') {
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
                        & $env:DOCKER logs --tail 100 recruitment-ats-candidate
                        throw "Candidate health check failed."
                    }

                    Write-Host "Candidate health is UP."
                '''
            }
        }

        stage('Continuous UI Testing') {
            steps {
                bat '''
                    .\\mvnw.cmd -Dtest=SeleniumUITests "-Dapp.url=%APP_URL%" test
                '''
            }
        }

        stage('Verify Published Image') {
            steps {
                bat '''
                    "%DOCKER%" pull "%REGISTRY%/%IMAGE_NAME%:%BUILD_NUMBER%"
                    "%DOCKER%" images "%REGISTRY%/%IMAGE_NAME%"
                '''
            }
        }

        stage('Promote Candidate to Production') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'ats-mysql-credentials',
                        usernameVariable: 'ATS_DB_USERNAME',
                        passwordVariable: 'ATS_DB_PASSWORD'
                    )
                ]) {
                    powershell '''
                        $ErrorActionPreference = "Stop"
                        $docker = $env:DOCKER
                        $image = "$($env:REGISTRY)/$($env:IMAGE_NAME):$($env:BUILD_NUMBER)"
                        $backup = "recruitment-ats-previous"
                        $production = "recruitment-ats"
                        $candidate = "recruitment-ats-candidate"

                        # List names once; do not inspect a missing container.
                        $existingContainers = @(& $docker container ls -a --format "{{.Names}}")
                        if ($LASTEXITCODE -ne 0) {
                            throw "Unable to list Docker containers before promotion."
                        }

                        if ($existingContainers -contains $backup) {
                            throw "Rollback container already exists. Inspect it before promoting."
                        }

                        $hadPrevious = $existingContainers -contains $production
                        $promotionStarted = $false

                        try {
                            if ($hadPrevious) {
                                & $docker stop $production
                                if ($LASTEXITCODE -ne 0) {
                                    throw "Could not stop the previous production container."
                                }

                                & $docker rename $production $backup
                                if ($LASTEXITCODE -ne 0) {
                                    throw "Could not preserve the previous production container."
                                }
                            }

                            $promotionStarted = $true

                            & $docker run -d `
                                --name $production `
                                --restart unless-stopped `
                                --add-host host.docker.internal:host-gateway `
                                -p 8080:8080 `
                                -e "SPRING_DATASOURCE_URL=$env:DB_URL" `
                                -e "SPRING_DATASOURCE_USERNAME=$env:ATS_DB_USERNAME" `
                                -e "SPRING_DATASOURCE_PASSWORD=$env:ATS_DB_PASSWORD" `
                                $image

                            if ($LASTEXITCODE -ne 0) {
                                throw "Production container failed to start."
                            }

                            $healthy = $false
                            for ($i = 1; $i -le 30; $i++) {
                                try {
                                    $response = Invoke-RestMethod `
                                        -Uri $env:PROD_HEALTH_URL `
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
                                Write-Host "Production health check failed. Recent logs follow:"
                                & $docker logs --tail 100 $production
                                throw "Production health check failed."
                            }

                            Write-Host "Production health is UP."
                        } catch {
                            $promotionError = $_
                            Write-Host "Promotion failed: $promotionError"
                            Write-Host "Attempting rollback."

                            if ($promotionStarted) {
                                $currentContainers = @(& $docker container ls -a --format "{{.Names}}")
                                if ($LASTEXITCODE -eq 0 -and $currentContainers -contains $production) {
                                    & $docker rm -f $production
                                    if ($LASTEXITCODE -ne 0) {
                                        Write-Host "WARNING: Could not remove the failed production container."
                                    }
                                }
                            }

                            if ($hadPrevious) {
                                $currentContainers = @(& $docker container ls -a --format "{{.Names}}")
                                if ($LASTEXITCODE -eq 0 -and $currentContainers -contains $backup) {
                                    & $docker rename $backup $production
                                    if ($LASTEXITCODE -eq 0) {
                                        & $docker start $production
                                        if ($LASTEXITCODE -eq 0) {
                                            Write-Host "Previous production container restored and restarted."
                                        } else {
                                            Write-Host "WARNING: Previous container was restored but could not be restarted."
                                        }
                                    } else {
                                        Write-Host "WARNING: Could not restore the previous container name."
                                    }
                                } else {
                                    Write-Host "WARNING: Rollback container was not found."
                                }
                            }

                            throw "Production promotion failed. See messages above."
                        }

                        # Delete the backup only after the new production container is healthy.
                        if ($hadPrevious) {
                            & $docker rm -f $backup
                            if ($LASTEXITCODE -ne 0) {
                                Write-Host "WARNING: Production is healthy, but the old backup could not be removed."
                            }
                        }

                        $currentContainers = @(& $docker container ls -a --format "{{.Names}}")
                        if ($LASTEXITCODE -eq 0 -and $currentContainers -contains $candidate) {
                            & $docker rm -f $candidate
                            if ($LASTEXITCODE -ne 0) {
                                Write-Host "WARNING: Could not remove the candidate container."
                            }
                        }

                        Write-Host "Version $($env:BUILD_NUMBER) promoted successfully."
                    '''
                }
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
            echo 'Pipeline failed. Review Console Output and Docker container logs.'
        }

        cleanup {
            echo 'Pipeline execution finished.'
        }
    }
}
