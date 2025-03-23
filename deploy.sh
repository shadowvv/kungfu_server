#!/bin/bash

# 检查应用程序是否正在运行的函数
check_app_running() {
  if pgrep -f "KungFu_Server-1.0.jar" > /dev/null; then
    echo "Java 应用程序正在运行。"
    return 0
  else
    echo "Java 应用程序未运行。"
    return 1
  fi
}

# 尝试启动 Java 应用程序
start_app() {
  echo "启动 Java 应用程序..."
  nohup java -jar /path/to/KungFu_Server-1.0.jar &
  sleep 10  # 等待应用程序启动
}

# 主逻辑
start_app
if check_app_running; then
  echo "Java 应用程序启动成功。"
else
  echo "Java 应用程序启动失败。"
  exit 1
fi