# Gera evidencias do JaCoCo (relatorio HTML + capturas de tela)
# Uso: .\docs\gerar-evidencias-jacoco.ps1

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

Write-Host "Executando testes e gerando relatorio JaCoCo..."
& .\mvnw.cmd test | Out-Null
if ($LASTEXITCODE -ne 0) {
    Write-Error "mvn test falhou com codigo $LASTEXITCODE"
}

$reportDir = Join-Path $root "target\site\jacoco"
if (-not (Test-Path (Join-Path $reportDir "index.html"))) {
    Write-Error "Relatorio nao encontrado em target\site\jacoco\index.html"
}

$evidenciasDir = Join-Path $root "docs\evidencias-jacoco"
New-Item -ItemType Directory -Force -Path $evidenciasDir | Out-Null

# Resumo textual a partir do jacoco.csv
$csv = Import-Csv (Join-Path $reportDir "jacoco.csv")
$bm = ($csv | Measure-Object -Property BRANCH_MISSED -Sum).Sum
$bc = ($csv | Measure-Object -Property BRANCH_COVERED -Sum).Sum
$lm = ($csv | Measure-Object -Property LINE_MISSED -Sum).Sum
$lc = ($csv | Measure-Object -Property LINE_COVERED -Sum).Sum
$mm = ($csv | Measure-Object -Property METHOD_MISSED -Sum).Sum
$mc = ($csv | Measure-Object -Property METHOD_COVERED -Sum).Sum
$cm = ($csv | Where-Object { $_.METHOD_MISSED -gt 0 -or $_.METHOD_COVERED -gt 0 }).Count
$cc = ($csv | Where-Object { [int]$_.METHOD_MISSED + [int]$_.METHOD_COVERED -gt 0 }).Count
$classesCovered = ($csv | Where-Object { [int]$_.METHOD_COVERED -gt 0 }).Count

$branchPct = if (($bc + $bm) -gt 0) { [math]::Round(100 * $bc / ($bc + $bm), 1) } else { 0 }
$linePct = if (($lc + $lm) -gt 0) { [math]::Round(100 * $lc / ($lc + $lm), 1) } else { 0 }
$methodPct = if (($mc + $mm) -gt 0) { [math]::Round(100 * $mc / ($mc + $mm), 1) } else { 0 }
$classPct = if ($cc -gt 0) { [math]::Round(100 * $classesCovered / $cc, 1) } else { 0 }

$resumo = @"
WaiterApp — Resumo JaCoCo
Gerado em: $(Get-Date -Format 'yyyy-MM-dd HH:mm')

INSTRUCTION: cobertura no relatorio HTML
BRANCH: $bc/$($bc + $bm) ($branchPct%)
LINE: $lc/$($lc + $lm) ($linePct%)
METHOD: $mc/$($mc + $mm) ($methodPct%)
CLASS: $classesCovered/$cc ($classPct%)

Relatorio HTML: target\site\jacoco\index.html

Capturas de tela nesta pasta:
  01-visao-geral.png       — painel principal do projeto
  02-pacote-pedido.png     — cobertura do pacote pedido
  03-pedido-service.png    — detalhe PedidoService (branches)
  04-pedido-entidade.png   — detalhe Pedido (codigo-fonte colorido)
  05-pacote-item.png       — cobertura do pacote item
  06-pacote-cliente.png    — cobertura do pacote cliente
  07-sessoes-execucao.png  — sessoes de execucao dos testes
"@
$resumo | Set-Content -Encoding UTF8 (Join-Path $evidenciasDir "resumo-cobertura.txt")
Write-Host $resumo

# Capturas com Chrome headless
$chrome = "C:\Program Files\Google\Chrome\Application\chrome.exe"
if (-not (Test-Path $chrome)) {
    Write-Warning "Chrome nao encontrado. Relatorio HTML gerado, mas capturas de tela foram ignoradas."
    exit 0
}

$shots = @(
    @{ file = "index.html"; out = "01-visao-geral.png" },
    @{ file = "com.example.waiterapp.pedido/index.html"; out = "02-pacote-pedido.png" },
    @{ file = "com.example.waiterapp.pedido/PedidoService.html"; out = "03-pedido-service.png" },
    @{ file = "com.example.waiterapp.pedido/Pedido.java.html"; out = "04-pedido-entidade.png" },
    @{ file = "com.example.waiterapp.item/index.html"; out = "05-pacote-item.png" },
    @{ file = "com.example.waiterapp.cliente/index.html"; out = "06-pacote-cliente.png" },
    @{ file = "jacoco-sessions.html"; out = "07-sessoes-execucao.png" }
)

foreach ($s in $shots) {
    $out = Join-Path $evidenciasDir $s.out
    $url = "file:///" + (Join-Path $reportDir $s.file).Replace("\", "/")
    Write-Host "Capturando $($s.out)..."
    & $chrome --headless --disable-gpu --window-size=1400,900 --screenshot="$out" "$url"
    Start-Sleep -Milliseconds 500
}

Write-Host "Evidencias salvas em docs\evidencias-jacoco\"
