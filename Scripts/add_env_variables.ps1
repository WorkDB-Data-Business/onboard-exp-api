$condition = Test-Path -Path .env

if(!$condition){
    Write-Output "The .env file doesn't exist."
    exit;    
}

foreach($line in Get-Content .env) {
        $env_array = $line.split("=")
        $variable_name = $env_array[0]
        $variable_value = $env_array[1]
        [System.Environment]::SetEnvironmentVariable($variable_name, $variable_value, 'Machine');
        Write-Output "The ${variable_name} variable was added successful."
}



