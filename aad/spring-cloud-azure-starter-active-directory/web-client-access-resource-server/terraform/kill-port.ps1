param ($port)

$foundProcesses = netstat -ano|findstr $port
$activePortPattern = ":$port\s.+LISTENING\s+\d+$"
$pidNumberPattern = "\d+$"

IF ($foundProcesses | Select-String -Pattern $activePortPattern -Quiet) {
    $matches = $foundProcesses | Select-String -Pattern $activePortPattern
    $firstMatch = $matches.Matches.Get(0).Value

    $pidNumber = [regex]::match($firstMatch, $pidNumberPattern).Value

    taskkill /pid $pidNumber /f
}