#!/bin/bash

make && 
cd cls
jar cmf ../manifest SylWord.jar *.class

cp SylWord.jar ../
