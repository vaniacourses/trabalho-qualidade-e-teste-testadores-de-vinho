# Gera evidencias do PITest (relatorio HTML + capturas de tela)
# Uso: .\docs\gerar-evidencias-pit.ps1

$ErrorActionPreference = "Stop"
$root = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path)
Set-Location $root

if (-not $env:JAVA_HOME) {
    $candidates = @(
        "C:\Program Files\Eclipse Adoptium\jdk-21.0.3.9-hotspot",
        "C:\Program Files\Java\jdk-22"
    )
    foreach ($jdk in $candidates) {
        if (Test-Path $jdk) {
            $env:JAVA_HOME = $jdk
            break
        }
    }
    if (-not $env:JAVA_HOME) {
        Write-Error "JAVA_HOME nao definido. Instale o JDK 17+ e configure a variavel de ambiente."
    }
}

Write-Host "Executando teste de mutacao PIT..."
& .\mvnw.cmd org.pitest:pitest-maven:mutationCoverage | Out-Null
if ($LASTEXITCODE -ne 0) {
    Write-Error "PIT mutationCoverage falhou com codigo $LASTEXITCODE"
}

$reportDir = Join-Path $root "target\pit-reports"
if (-not (Test-Path (Join-Path $reportDir "index.html"))) {
    Write-Error "Relatorio nao encontrado em target\pit-reports\index.html"
}

$evidenciasDir = Join-Path $root "docs\evidencias-pit"
New-Item -ItemType Directory -Force -Path $evidenciasDir | Out-Null

# Extrai metricas do index.html (tabela Project Summary)
$indexHtml = Get-Content (Join-Path $reportDir "index.html") -Raw
$legendValues = [regex]::Matches($indexHtml, 'coverage_legend">(\d+)/(\d+)') | ForEach-Object {
    @{ Num = [int]$_.Groups[1].Value; Den = [int]$_.Groups[2].Value }
}

$lineCov = if ($legendValues.Count -ge 1) { "$($legendValues[0].Num)/$($legendValues[0].Den)" } else { "?" }
$mutKilled = if ($legendValues.Count -ge 2) { $legendValues[1].Num } else { 0 }
$mutTotal = if ($legendValues.Count -ge 2) { $legendValues[1].Den } else { 0 }
$strengthKilled = if ($legendValues.Count -ge 3) { $legendValues[2].Num } else { 0 }
$strengthTotal = if ($legendValues.Count -ge 3) { $legendValues[2].Den } else { 0 }

$mutPct = if ($mutTotal -gt 0) { [math]::Round(100 * $mutKilled / $mutTotal, 0) } else { 0 }
$linePct = if ($legendValues.Count -ge 1 -and $legendValues[0].Den -gt 0) {
    [math]::Round(100 * $legendValues[0].Num / $legendValues[0].Den, 0)
} else { 0 }
$strengthPct = if ($strengthTotal -gt 0) { [math]::Round(100 * $strengthKilled / $strengthTotal, 0) } else { 0 }

$resumo = @"
WaiterApp - Resumo PITest
Gerado em: $(Get-Date -Format 'yyyy-MM-dd HH:mm')

Classes analisadas: 6
Line Coverage (classes mutadas): $lineCov ($linePct%)
Mutantes gerados: $mutTotal
Mutantes mortos: $mutKilled ($mutPct%)
Test Strength: $strengthKilled/$strengthTotal ($strengthPct%)

Relatorio HTML: target\pit-reports\index.html

Capturas de tela nesta pasta:
  01-visao-geral.png          - resumo do projeto
  02-pacote-pedido.png        - pacote pedido
  03-pedido-service.png       - detalhe PedidoService
  04-pedido-entidade.png      - detalhe Pedido
  05-pacote-item.png          - pacote item (ItemService)
  06-pacote-pagamento.png     - pacote pagamento
"@
$resumo | Set-Content -Encoding UTF8 (Join-Path $evidenciasDir "resumo-mutacao.txt")
Write-Host $resumo

$chrome = "C:\Program Files\Google\Chrome\Application\chrome.exe"
if (-not (Test-Path $chrome)) {
    Write-Warning "Chrome nao encontrado. Relatorio HTML gerado, mas capturas de tela foram ignoradas."
    exit 0
}

$shots = @(
    @{ file = "index.html"; out = "01-visao-geral.png" },
    @{ file = "com.example.waiterapp.pedido/index.html"; out = "02-pacote-pedido.png" },
    @{ file = "com.example.waiterapp.pedido/PedidoService.java.html"; out = "03-pedido-service.png" },
    @{ file = "com.example.waiterapp.pedido/Pedido.java.html"; out = "04-pedido-entidade.png" },
    @{ file = "com.example.waiterapp.item/index.html"; out = "05-pacote-item.png" },
    @{ file = "com.example.waiterapp.pagamento/index.html"; out = "06-pacote-pagamento.png" }
)

foreach ($s in $shots) {
    $out = Join-Path $evidenciasDir $s.out
    $url = "file:///" + (Join-Path $reportDir $s.file).Replace("\", "/")
    Write-Host "Capturando $($s.out)..."
    & $chrome --headless --disable-gpu --window-size=1400,900 --screenshot="$out" "$url"
    Start-Sleep -Milliseconds 500
}

Write-Host "Evidencias salvas em docs\evidencias-pit\"
