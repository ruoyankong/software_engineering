#!/bin/bash

SOURCE_DIR=src
if [[ "$OSTYPE" == "linux-gnu" ]]; then
    FX_PATH=lib/javafx-sdk-11.0.2_linux/lib
elif [[ "$OSTYPE" == "darwin"* ]]; then
    FX_PATH=lib/javafx-sdk-11.0.2_macos/lib
fi

javadoc --module-path $FX_PATH --add-modules javafx.controls,javafx.fxml -d documentation -sourcepath $SOURCE_DIR voting_system
