#!/usr/bin/env bash
# Simple-Solitaire 快速 Debug 构建脚本
# 用法: bash build.sh
# 产物: solitaire-debug.apk

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
APK_SRC="${SCRIPT_DIR}/app/build/outputs/apk/debug/app-debug.apk"
APK_DST="${SCRIPT_DIR}/solitaire-debug.apk"

echo "=== Simple-Solitaire Debug Build ==="
echo "项目目录: ${SCRIPT_DIR}"
echo ""

# Step 1: 构建
echo "[1/2] assembleDebug ..."
./gradlew --no-daemon assembleDebug

# Step 2: 复制并重命名
echo "[2/2] 复制 APK -> solitaire-debug.apk"
cp "${APK_SRC}" "${APK_DST}"

# 输出结果
APK_SIZE=$(du -h "${APK_DST}" | cut -f1)
echo ""
echo "=== 构建完成 ==="
echo "APK: ${APK_DST}"
echo "大小: ${APK_SIZE}"