#!/usr/bin/env sh

export JAVA_HOME=/home/Suraj/CascadeProjects/TaskTracker/jdk-21.0.2
export PATH=$PATH:$JAVA_HOME/bin
export ANDROID_HOME=~/android-sdk
export ANDROID_SDK_ROOT=~/android-sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools

# Use the local gradle installation
exec ./gradle-8.4/bin/gradle "$@"
