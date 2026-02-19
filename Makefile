build-AwsLamdaFunction:
	mvn clean package
	cp target/aws-lamda-1.0-SNAPSHOT-lambda-package.zip $(ARTIFACTS_DIR)
