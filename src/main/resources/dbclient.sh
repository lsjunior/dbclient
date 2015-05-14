#!/bin/bash

CP="~/.m2/repository/org/postgresql/postgresql/9.3-1103-jdbc4/postgresql-9.3-1103-jdbc4.jar:dbclient.jar"
CLASS="br.net.woodstock.dbclient.DBCLient"
URL="jdbc:postgresql://localhost:5432/nuxeo"
USER="nuxeo"
PASSWORD="nuxeo"

echo "java -cp $CP $CLASS \"$URL\" \"$USER\" \"$PASSWORD\"" > .init
. .init
rm -f .init
