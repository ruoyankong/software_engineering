#!/bin/bash

SOURCE_DIR=src
TARGET_DIR=bin
TEST_SOURCE_DIR=testing
TEST_TARGET_DIR=testing_bin
if [ -d $TEST_TARGET_DIR ]; then
    rm -rf $TEST_TARGET_DIR
fi
mkdir $TEST_TARGET_DIR
javac -cp .:$SOURCE_DIR:junit-4.12.jar:hamcrest-core-1.3.jar $TEST_SOURCE_DIR/voting_system/*.java -d $TEST_TARGET_DIR
java -cp .:$SOURCE_DIR:junit-4.12.jar:hamcrest-core-1.3.jar:$TEST_TARGET_DIR voting_system.TestSuiteRunner 
