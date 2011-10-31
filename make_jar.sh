#!/bin/sh

VERSION=0.0.1
jar -cvfM rhoconnect-java-${VERSION}.jar -C bin/ .

jar -tvf rhoconnect-java-${VERSION}.jar

