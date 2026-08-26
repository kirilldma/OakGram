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

echo "[*] Copying OakGram Java source files and resources..."
mkdir -p TMessagesProj/src/main/java/org/telegram/margelet/
mkdir -p TMessagesProj/src/main/java/org/telegram/ui/
mkdir -p TMessagesProj/src/main/java/org/telegram/messenger/
mkdir -p TMessagesProj/src/main/java/de/robv/android/xposed/
mkdir -p TMessagesProj/src/main/res/values/
mkdir -p TMessagesProj/src/main/res/values-ru/
mkdir -p TMessagesProj/src/main/res/drawable/
mkdir -p TMessagesProj/src/main/res/raw/

cp -r "$DIR/java/margelet/"* TMessagesProj/src/main/java/org/telegram/margelet/ 2>/dev/null || true
cp -r "$DIR/java/ui/"* TMessagesProj/src/main/java/org/telegram/ui/ 2>/dev/null || true
cp -r "$DIR/java/de/"* TMessagesProj/src/main/java/de/ 2>/dev/null || true
cp "$DIR/java/app/ApplicationLoaderImpl.java" TMessagesProj/src/main/java/org/telegram/messenger/ 2>/dev/null || true
cp "$DIR/java/app/MargeletPython.java" TMessagesProj/src/main/java/org/telegram/margelet/ 2>/dev/null || true

cp -r "$DIR/res/drawable/"* TMessagesProj/src/main/res/drawable/ 2>/dev/null || true
cp -r "$DIR/res/raw/"* TMessagesProj/src/main/res/raw/ 2>/dev/null || true
cp -r "$DIR/res/values/"* TMessagesProj/src/main/res/values/ 2>/dev/null || true
cp -r "$DIR/res/values-ru/"* TMessagesProj/src/main/res/values-ru/ 2>/dev/null || true

echo "[*] Building release APK..."
./gradlew assembleAfatRelease

APK_PATH="TMessagesProj/build/outputs/apk/afat/release/TMessagesProj-afat-release.apk"
if [ -f "$APK_PATH" ]; then
    echo "[+] SUCCESS: APK built at $APK_PATH"
else
    echo "[-] Build finished. Check output directory."
fi
