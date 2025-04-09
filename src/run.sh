#!/bin/bash

javac -cp .:/Users/noahvickerson/eclipse-workspace/Libraries/postgresql-42.7.5.jar edu/ucalgary/oop/$1.java
java -cp .:/Users/noahvickerson/eclipse-workspace/Libraries/postgresql-42.7.5.jar edu.ucalgary.oop.$1 $2
rm -f edu/ucalgary/oop/*.class

# ex. ./run.sh Calculator