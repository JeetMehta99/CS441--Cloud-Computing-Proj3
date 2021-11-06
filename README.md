# Homework 3
## Information
Name: Jeet Mehta

UIN: 668581235

Youtube Link: 

AWS URL: https://d4lvtsa96b.execute-api.us-east-2.amazonaws.com/testing/lambda_function

## Tasks:

### Task 1:

Given the input of timestamp and interval have to check whether that timestamp is present or not. 
Here, interval = Range of (giventimestamp - dT, giventimestamp + dT)

## Task2: 

If timestamp is found we have to return a md5 generated hash code or else a 400 level client response since timestamp is not found.

## Instructions

### My Environment & Technologies used:

The environment used for the project was:

1. OS: Windows 10
2. IDE: IntelliJ IDEA 2021
3. Scala, Python
4. AWS: EC2, Lambda & API Gateway

### Working: 

We start off by creating a binary search for our implementation. We then made the created the getIndex function to get the leftmost and rightmost timestamp so that our interval is defined and our implementation to search our timestamp begins.

Once the timestamp is found which matches the regex pattern, a 200 response will be sent and a MD5 generated hash code will be returned. 


## AWS ec2 to s3:

### 1. Create an EC2 instance and ssh into it.
### 2. Perform these intructions:

Java Installation:

`wget https://download.oracle.com/java/17/latest/jdk-17_linux-x54_bin.rpm`

`rpm -ivh jdk-17_linux-x54_bin.rpm`

SBT Installation:

`curl -L https://www.scala-sbt.org/sbt-rpm.repo > sbt-rpm.repo`

`sudo su`

`mv sbt-rpm.repo/etc/yum.repos.d/`

`yum install sbt`

`scp-i <path where key pair is key.pem> -r <path of the main file> ec-2 user@ec2-instance url:~/`

`scp -i <path where key pair is key.pem> <path of dailyrun.sh> ec-2 user@ec2-instance url:~/`

## How to run on local machine

1. Clone the project on your local machines using 
2. Build the project
3. Execute sbt clean compile
4. Execute sbt clean compile run
5. Then you will be asked which main is to executed: Choose Rest client or Grpc 
6. Open a new terminal in case of gRPC then execute sbt. And then execute runMain Client.lambdaGrpcClient