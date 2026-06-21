$envFile = Join-Path $PSScriptRoot ".env"
if (Test-Path $envFile) {
    Get-Content $envFile | ForEach-Object {
        if ($_ -match "^\s*([^#][^=]+)=(.+)$") {
            $key = $matches[1].Trim()
            $value = $matches[2].Trim()
            [Environment]::SetEnvironmentVariable($key, $value, "Process")
            Write-Host "Loaded: $key"
        }
    }
    Write-Host ""
} else {
    Write-Host "Arquivo .env nao encontrado!" -ForegroundColor Red
    exit 1
}

.\mvnw.cmd spring-boot:run
