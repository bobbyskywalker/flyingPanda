#!/usr/bin/env bash
set -euo pipefail

JVER=17
SRC=../src/main/java
OUT=out
RES=../src/main/resources

mkdir -p "$OUT"

STAMP="$OUT/.stamp"

mapfile -t CHANGED < <(find "$SRC" -name '*.java' -newer "$STAMP" 2>/dev/null)

if [ ! -f "$STAMP" ] || [ ${#CHANGED[@]} -eq 0 ]; then
  find "$SRC" -name '*.java' > sources.txt
  javac --enable-preview --release "$JVER" -proc:none -g:none -d "$OUT" @"sources.txt"
else

  javac --enable-preview --release "$JVER" -proc:none -g:none -d "$OUT" "${CHANGED[@]}"
fi

echo "Main-Class: com.FlyingPanda.main.Main" > MANIFEST.MF
jar cfm flyingPanda.jar MANIFEST.MF -C "$OUT" . -C "$RES" .

touch "$STAMP"

echo "Built flyingPanda.jar"
