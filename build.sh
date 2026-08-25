#!/usr/bin/env bash
set -e

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TG_DIR="${1:-../Telegram}"

echo "[*] OakGram Build Script"
echo "[*] Target Telegram repo: $TG_DIR"

if [ ! -d "$TG_DIR" ]; then
    echo "[*] Cloning DrKLO/Telegram..."
    git clone --depth=1 --recursive https://github.com/DrKLO/Telegram.git "$TG_DIR"
fi

cd "$TG_DIR"

echo "[*] Applying patch..."
if [ -f "$DIR/patch/margelet.patch" ]; then
    git apply --ignore-whitespace --whitespace=nowarn "$DIR/patch/margelet.patch" || true
fi

echo "[*] Copying OakGram Java source files..."
mkdir -p TMessagesProj/src/main/java/org/telegram/
cp -r "$DIR/java/"* TMessagesProj/src/main/java/org/telegram/ 2>/dev/null || cp -r "$DIR/java/"* TMessagesProj/src/main/java/

echo "[*] Building release APK..."
./gradlew assembleAfatRelease

APK_PATH="TMessagesProj/build/outputs/apk/afat/release/TMessagesProj-afat-release.apk"
if [ -f "$APK_PATH" ]; then
    echo "[+] SUCCESS: APK built at $APK_PATH"
else
    echo "[-] Build finished. Check output directory."
fi
