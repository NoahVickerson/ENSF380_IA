#!/bin/bash

for i in "$@"; do
echo
echo "Running tests for $i:"
javac -cp .:/Users/noahvickerson/eclipse-workspace/Libraries/hamcrest-core-1.3.jar:/Users/noahvickerson/eclipse-workspace/Libraries/junit-4.13.2.jar:/Users/noahvickerson/eclipse-workspace/Libraries/postgresql-42.7.5.jar edu/ucalgary/oop/$i\Test.java
java -cp .:/Users/noahvickerson/eclipse-workspace/Libraries/hamcrest-core-1.3.jar:/Users/noahvickerson/eclipse-workspace/Libraries/junit-4.13.2.jar:/Users/noahvickerson/eclipse-workspace/Libraries/postgresql-42.7.5.jar org.junit.runner.JUnitCore edu.ucalgary.oop.$i\Test
rm -f edu/ucalgary/oop/*.class
echo 
done

# ex. ./runtests.sh Calculator Integer