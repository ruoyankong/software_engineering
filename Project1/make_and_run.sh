#!/bin/bash

SOURCE_DIR=src
TARGET_DIR=bin
TEST_SOURCE_DIR=src
TEST_TARGET_DIR=bin
if [ -d $TEST_TARGET_DIR ]; then
    rm -rf $TEST_TARGET_DIR
fi
mkdir $TEST_TARGET_DIR
javac -cp .:$SOURCE_DIR $TEST_SOURCE_DIR/voting_system/*.java -d $TEST_TARGET_DIR
java -cp .:$SOURCE_DIR:$TEST_TARGET_DIR voting_system.Menu

