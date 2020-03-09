#!/bin/bash

SOURCE_DIR=src
if [[ "$OSTYPE" == "linux-gnu" ]]; then
    FX_PATH=lib/javafx-sdk-11.0.2_linux/lib
elif [[ "$OSTYPE" == "darwin"* ]]; then
    FX_PATH=lib/javafx-sdk-11.0.2_macos/lib
fi
TARGET_DIR=bin
if [ -d $TARGET_DIR ]; then
    rm -rf $TARGET_DIR
fi
mkdir $TARGET_DIR
javac --module-path $FX_PATH --add-modules javafx.controls,javafx.fxml -sourcepath .:$SOURCE_DIR:$SOURCE_DIR/voting_system $SOURCE_DIR/voting_system/*.java -d $TARGET_DIR
java --module-path $FX_PATH --add-modules javafx.controls,javafx.fxml -cp .:$SOURCE_DIR:$TARGET_DIR voting_system.FileIO

