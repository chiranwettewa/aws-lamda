build-AwsLamdaFunction:
	mvn clean package -DskipTests
	cp target/aws-lamda-1.0-SNAPSHOT-lambda-package.zip $(ARTIFACTS_DIR)
