# build.ps1
# Run: powershell -ExecutionPolicy Bypass -File .\build.ps1
$ErrorActionPreference = "Stop"

# ---- SETTINGS ----
$ENABLE_PREVIEW = $true   # set to $false if you DON'T use preview features
$JVER = 17                # used only when $ENABLE_PREVIEW -eq $false
# -------------------

# Detect javac major version (e.g., "javac 25.0.1" -> 25)
$javacVersionRaw = (& javac -version) 2>&1
if (-not $javacVersionRaw) { throw "Could not run 'javac -version'. Is JDK on PATH?" }
if ($javacVersionRaw -notmatch '(\d+)(\.\d+)?') { throw "Could not parse javac version from: $javacVersionRaw" }
$JAVAC_MAJOR = [int]($Matches[1])

$OUT  = Join-Path $PSScriptRoot "out"

function Resolve-Src {
  $p1 = Join-Path $PSScriptRoot "src\main\java"
  $p2 = Join-Path (Join-Path $PSScriptRoot "..") "src\main\java"
  if (Test-Path $p1) { return (Resolve-Path $p1).Path }
  if (Test-Path $p2) { return (Resolve-Path $p2).Path }
  throw "Could not find src\main\java relative to $PSScriptRoot"
}
function Resolve-Res {
  $p1 = Join-Path $PSScriptRoot "src\main\resources"
  $p2 = Join-Path (Join-Path $PSScriptRoot "..") "src\main\resources"
  if (Test-Path $p1) { return (Resolve-Path $p1).Path }
  if (Test-Path $p2) { return (Resolve-Path $p2).Path }
  return $null
}

$SRC = Resolve-Src
$RES = Resolve-Res

# Fresh OUT dir
New-Item -ItemType Directory -Force -Path $OUT | Out-Null
Get-ChildItem -Recurse -Force $OUT -ErrorAction SilentlyContinue | Remove-Item -Recurse -Force -ErrorAction SilentlyContinue

# Collect all sources
$allSources = Get-ChildItem -Path $SRC -Recurse -Filter *.java | Select-Object -ExpandProperty FullName
if (-not $allSources -or $allSources.Count -eq 0) {
  throw "No .java files found under '$SRC'."
}

# Write argfile for javac (safe with spaces)
$sourcesTxt = Join-Path $PSScriptRoot "sources.txt"
$allSources | Out-File -FilePath $sourcesTxt -Encoding ascii

# ---- COMPILE ----
if ($ENABLE_PREVIEW) {
  # With preview, pair with the CURRENT javac release (e.g., 25)
  & javac --release $JAVAC_MAJOR --enable-preview -proc:none -g:none -d "$OUT" "@$sourcesTxt"
} else {
  & javac --release $JVER -proc:none -g:none -d "$OUT" "@$sourcesTxt"
}
if ($LASTEXITCODE -ne 0) { throw "javac failed." }

# Find Main.class (prefer ...\main\Main.class if multiple)
$mainCandidates = Get-ChildItem -Recurse -Path $OUT -Filter Main.class
if (-not $mainCandidates -or $mainCandidates.Count -eq 0) {
  throw "No Main.class under '$OUT'. Check your entry point/package."
}
$cls = $mainCandidates | Where-Object { $_.FullName -match "\\main\\Main\.class$" } | Select-Object -First 1
if (-not $cls) { $cls = $mainCandidates | Select-Object -First 1 }

# Derive fully-qualified name from compiled path
$rel = (Resolve-Path $cls.FullName).Path.Substring((Resolve-Path $OUT).Path.Length + 1)
$mainFqn = ($rel -replace '\\','/') -replace '\.class$',''
$mainFqn = $mainFqn -replace '/','.'

# ---- MANIFEST ---- (must end with newline)
$manifest = "Manifest-Version: 1.0`nMain-Class: $mainFqn`n"
$manifestPath = Join-Path $PSScriptRoot "MANIFEST.MF"
$manifest | Out-File -FilePath $manifestPath -Encoding ascii

# ---- JAR ---- (include resources if present)
$flyingJar = Join-Path $PSScriptRoot "flyingPanda.jar"
if ($RES -and (Test-Path $RES)) {
  & jar cfm "$flyingJar" "$manifestPath" -C "$OUT" . -C "$RES" .
} else {
  & jar cfm "$flyingJar" "$manifestPath" -C "$OUT" .
}
if ($LASTEXITCODE -ne 0) { throw "jar failed." }

Write-Host "Built: $flyingJar"
Write-Host "Main-Class: $mainFqn"
Write-Host "Javac detected: $javacVersionRaw"

if ($ENABLE_PREVIEW) {
  Write-Host 'Run:  java --enable-preview -jar .\flyingPanda.jar'
} else {
  Write-Host 'Run:  java -jar .\flyingPanda.jar'
}
