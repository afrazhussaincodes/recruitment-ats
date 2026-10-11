
param(
    [Parameter(Mandatory = $true)]
    [string]$DeployDir,

    [Parameter(Mandatory = $true)]
    [ValidateRange(1024, 65535)]
    [int]$AppPort
)

$ErrorActionPreference = 'Stop'

$jar = Join-Path $DeployDir 'recruitment-ats.jar'
$pidFile = Join-Path $DeployDir 'recruitment-ats.pid'
$stdout = Join-Path $DeployDir 'app.stdout.log'
$stderr = Join-Path $DeployDir 'app.stderr.log'
$java = 'C:\Program Files\Eclipse Adoptium\jdk-17.0.20.8-hotspot\bin\java.exe'

if (!(Test-Path $jar)) {
    throw "Deployment JAR not found: $jar"
}

if (!(Test-Path $java)) {
    throw "Java 17 executable not found: $java"
}

# Stop only the previous application instance recorded by this deployment.
if (Test-Path $pidFile) {
    $oldPid = 0
    $rawPid = Get-Content $pidFile -ErrorAction SilentlyContinue

    if ([int]::TryParse("$rawPid", [ref]$oldPid)) {
        $oldProcess = Get-CimInstance Win32_Process `
            -Filter "ProcessId = $oldPid" `
            -ErrorAction SilentlyContinue

        if ($oldProcess -and
            $oldProcess.CommandLine -like '*recruitment-ats.jar*') {
            Stop-Process -Id $oldPid -Force
            Start-Sleep -Seconds 2
        }
    }

    Remove-Item $pidFile -Force -ErrorAction SilentlyContinue
}

# Clear old logs so failures are easier to diagnose.
Remove-Item $stdout, $stderr -Force -ErrorAction SilentlyContinue

# Start Spring Boot independently of the Jenkins build process.
$process = Start-Process `
    -FilePath $java `
    -ArgumentList @(
        '-jar',
        $jar,
        "--server.port=$AppPort"
    ) `
    -WorkingDirectory $DeployDir `
    -RedirectStandardOutput $stdout `
    -RedirectStandardError $stderr `
    -PassThru

Set-Content -Path $pidFile -Value $process.Id

# Wait up to 90 seconds for the health endpoint.
$healthUrl = "http://localhost:$AppPort/actuator/health"

for ($i = 0; $i -lt 45; $i++) {
    Start-Sleep -Seconds 2

    $process.Refresh()

    if ($process.HasExited) {
        Get-Content $stdout -Tail 40 -ErrorAction SilentlyContinue
        Get-Content $stderr -Tail 40 -ErrorAction SilentlyContinue
        throw "Spring Boot exited with code $($process.ExitCode)."
    }

    try {
        $response = Invoke-RestMethod `
            -Uri $healthUrl `
            -TimeoutSec 3

        if ($response.status -eq 'UP') {
            Write-Host "Application started successfully."
            Write-Host "Health endpoint: $healthUrl"
            Write-Host "Process ID: $($process.Id)"
            exit 0
        }
    }
    catch {
        # Retry until the health-check deadline.
    }
}

Get-Content $stdout -Tail 40 -ErrorAction SilentlyContinue
Get-Content $stderr -Tail 40 -ErrorAction SilentlyContinue
throw "Health check failed after 90 seconds: $healthUrl"
