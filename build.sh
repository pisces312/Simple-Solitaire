#!/usr/bin/env bash
# Simple-Solitaire Debug 构建脚本
# 用法: bash build.sh

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

# ============================================================
# Step 0: 检测 AGP 版本并修复 gradle-wrapper.properties
# ============================================================
fix_gradle_wrapper() {
    local PROPERTIES_FILE="${SCRIPT_DIR}/gradle/wrapper/gradle-wrapper.properties"
    local CURRENT_URL
    CURRENT_URL=$(grep "distributionUrl=" "$PROPERTIES_FILE" | sed 's/distributionUrl=//')

    local AGP=""
    if [[ -f "${SCRIPT_DIR}/gradle/libs.versions.toml" ]]; then
        AGP=$(grep -i "^android" "${SCRIPT_DIR}/gradle/libs.versions.toml" | grep -oP '\d+\.\d+' | head -1)
    fi
    if [[ -z "$AGP" ]]; then
        AGP=$(grep -i "com.android.application" "${SCRIPT_DIR}/build.gradle" "${SCRIPT_DIR}/build.gradle.kts" 2>/dev/null | grep -oP '\d+\.\d+' | head -1)
    fi

    local REQUIRED_URL=""
    local MAJOR="${AGP%%.*}"
    if [[ "${MAJOR:-0}" -ge 9 ]]; then
        REQUIRED_URL="https://mirrors.cloud.tencent.com/gradle/gradle-9.4.1-bin.zip"
    else
        REQUIRED_URL="https://mirrors.cloud.tencent.com/gradle/gradle-8.10.2-bin.zip"
    fi

    if [[ "$CURRENT_URL" != *"$REQUIRED_URL"* ]]; then
        echo "[GRADLE] 版本不匹配 (AGP $AGP):"
        echo "  现状: $CURRENT_URL"
        echo "  更新: $REQUIRED_URL"
        sed -i "s|distributionUrl=.*|distributionUrl=$REQUIRED_URL|" "$PROPERTIES_FILE"
    fi
}
fix_gradle_wrapper

# ============================================================
# Step 1: 构建
# ============================================================
APK_SRC="${SCRIPT_DIR}/app/build/outputs/apk/debug/app-debug.apk"
APK_DST="${SCRIPT_DIR}/simple-solitaire-debug.apk"

echo "=== Simple-Solitaire Debug Build ==="
echo "AGP 检测已完成，开始 assembleDebug ..."
echo ""

./gradlew --no-daemon assembleDebug

echo ""
echo "[复制] APK -> simple-solitaire-debug.apk"
cp "$APK_SRC" "$APK_DST"

APK_SIZE=$(du -h "$APK_DST" | cut -f1)
echo ""
echo "=== 构建完成 ==="
echo "APK: simple-solitaire-debug.apk"
echo "大小: $APK_SIZE"