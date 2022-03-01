$condition = Test-Path -Path .env
if(!$condition){
    Write-Output "The .env file doesn't exist."
    exit;    
}

foreach($line in Get-Content .\.env) {
        $env_array = $line.split("=")
        $variable_name = $env_array[0]
        [Environment]::SetEnvironmentVariable($variable_name, $null, "Machine")
        Write-Output "The ${variable_name} variable was removed successful."
}
