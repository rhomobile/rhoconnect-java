#!/bin/sh

VERSION=0.0.1
jar cvf rhoconnect-${VERSION}.jar -C bin/ .

jar tvf rhoconnect-${VERSION}.jar

