#!/bin/bash

# 变量
JAR_FILE="/opt/myapp/deploy/KungFu_Server-1.0.jar"
LOG_FILE="/opt/myapp/logs/app.log"

# 打印调试信息
echo "当前工作目录：$(pwd)"
echo "JAR文件路径：$JAR_FILE"
echo "日志文件路径：$LOG_FILE"

# 确保JAR文件存在
if [ ! -f "$JAR_FILE" ]; then
  echo "错误：在 $JAR_FILE 找不到JAR文件"
  exit 1
fi

# 确保日志目录存在
LOG_DIR=$(dirname "$LOG_FILE")
if [ ! -d "$LOG_DIR" ]; then
  echo "日志目录 $LOG_DIR 不存在，正在创建..."
  mkdir -p "$LOG_DIR"
  chmod 755 "$LOG_DIR"
fi

# 停止现有的Java应用程序（如果正在运行）
if pgrep -f "KungFu_Server-1.0.jar"; then
  pkill -f "KungFu_Server-1.0.jar"
  echo "已停止现有的Java应用程序。"
else
  echo "未找到现有的Java应用程序。"
fi

# 启动新的Java应用程序
nohup java -jar "$JAR_FILE" > "$LOG_FILE" 2>&1 &
echo "Java应用程序已启动。日志可以在 $LOG_FILE 中找到。"

# 等待并验证应用程序是否启动
sleep 10
if pgrep -f "KungFu_Server-1.0.jar"; then
  echo "Java应用程序正在运行。"
else
  echo "Java应用程序启动失败。"
  exit 1
fi