#!/bin/bash

cd LogFile_CS441 &&
sbt clean compile run &&
d=$(date +%Y-%m-%d)
d='LogFileGenerator.'$d'.log'
aws s3 cp LogFile_CS441/log/d s3://bucket-cc2