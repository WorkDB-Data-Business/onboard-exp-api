$condition = Test-Path -Path .env
if(!$condition){
    Write-Output "O arquivo .env nao existe."
    exit;    
}

foreach($line in Get-Content .\.env) {
        $env_array = $line.split("=")
        $variable_name = $env_array[0]
        [Environment]::SetEnvironmentVariable($variable_name, $null, "Machine")
        Write-Output "A variavel ${variable_name} foi removida com sucesso."
}
