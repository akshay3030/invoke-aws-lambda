# invoke-aws-lambda HTTP Get & Post Using Spring-Boot with Plain Old Java Way

#HTTP POST
endpoint : http://localhost:9000/invoke/lambda?name=scoring
body: {"x":6,"y":5}
#HTTP GET
invocation endpoints: http://localhost:9000/invoke/lambda/scoring?x=9&y=4
aws lambda function name = scoring

#Learinings
AWS Invoke lambda with BasicSessionCredentials & ProfileCredentialsProvider with AWSLambdaClient which is synchronous


This is Plain Old Java implementation, creating all object right where needed a new object with new.


//jax-rs has @QueryParam (javax.ws.rs.QueryParam) while spring has @RequestParam
//@PathVariable is spring , @PathParam is jax-rs
//@RequestMapping is spring, @Path is jax-rx
//@Beanparam can also be used to extracted both x & y from a single object

#Maven Commands:
mvn clean install
mvn spring-boot:run
mvn clean install -DskipTests=true (to skip unit tests)
mvn spring-boot:run -Drun.profiles=local (run a profile)

#Enable logging using application.properties
logging.level.org.org.springframework.web=DEBUG
logging.file=lambda.log



