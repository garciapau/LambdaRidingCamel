# LambdaRidingCamel

## Lab Description
In this Lab I'm testing Apache Camel embedded in an AWS Lambda Function
* How Apache Camel and Spring fit into an AWS Lambda
* Instances Tracking and Monitoring
* Check whether it is possible to track/monitor the instances, taking into account that Lambdas are serverless and ephemeral (no EC2 instance is required)
* Performance and starting time, due to the Lambda's restriction of 15 minutes of life

## Design
The Lambda Function is triggered when a new file is dropped in a specific S3 folder. As an input parameter, we define an S3 Event (not the file content itself). Then, the file is retrieved and passed to Camel using the "direct" transport. Camel unmarshalls the XML file and builds a JSON representation of it which is returned to the Lambda Function, which produces it as output.

## How to use it
git clone https://github.com/garciapau/LambdaRidingCamel.git

cd LambdaRidingCamel

mvn package shade:shade

Create an AWS Lambda Function and add the jar located in the "target" folder

Attach that Lambda Function to an S3 Event of type ObjectCreatedByPut (under Triggers tab)
