package com.akshay.controller;


import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.akshay.lambda.model.LambdaInput;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

//import com.amazonaws.services.lambda.AWSLambdaClient;

@RestController
@RequestMapping("/invoke")
public class InvokeLambdaController {

	//jax-rs has @QueryParam (javax.ws.rs.QueryParam) while spring has @RequestParam
		//@PathVariable is spring , @PathParam is jax-rs
		//@RequestMapping is spring, @Path is jax-rx
		//@Beanparam can also be used to extracted both x & y from a single object
		@RequestMapping("/lambda/{lname}")
		public String invokeLambda(@PathVariable("lname") String lambda_name,@RequestParam String x, @RequestParam String y){

			//AWSLambdaClientBuilder.defaultClient();
			//AWSLambdaClient awsLambdaClient = new AWSLambdaClient(BasicAWSCredentials);

			////Below is deprecated, use builder pattern instead as above
			//to use AWS credentials tokens manually
			long start = new Date().getTime();
			String AccessKey = "ASIAITE34BJSKM6BQVIQ";
			String SecretAccessKey = StringEscapeUtils.unescapeJava("CbOLgrZyFtPF3DC/7UuXs8DlXLzQHYWJvPT6XxNf");
			String SessionToken = StringEscapeUtils.unescapeJava("FQoDYXdzELT//////////wEaDG3X83lck1E+SHUosyLGAfGrcWUZ1XaKygCKCw0xGaqaqbTF/Lt+1ng5QnZqK9GQMkjaBtqjw386fueT/v+BFRZrZztpboRQZbJzYwQZNCbazBvfk48qQbCHW+UjxX8MuaoYe8QxM8CdQl7A18JAZHdwiplorTI7NgEulyjfjOymmSyoHcWPB/2TICzT1loPa2ebvX6VX3R6/SFuDRe5V8nLMUQ9UKjk9IQNOUt81fC5whv0/kAjaOECMp6k2UzTuNCJjFSvi1BqhSMtp/MLSmSuGhmwoCiMvcrEBQ==");
			BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(AccessKey,SecretAccessKey,SessionToken);

			//use AWS Profile from local machine
			ProfileCredentialsProvider profileCredentialsProvide = new ProfileCredentialsProvider("sts");

			//Below is to pass Proxy
			ClientConfiguration clientConfiguration = new ClientConfiguration();
			clientConfiguration.setProtocol(Protocol.HTTPS);
			clientConfiguration.setProxyHost("localhost");
			clientConfiguration.setProxyPort(8099);

			//		Async AWS Lambda Client	
			//		AWSLambdaAsyncClient awsLambdaAsyncClient = new AWSLambdaAsyncClient(profileCredentialsProvide);

			//to use aws profile --profile sts
			AWSLambdaClient awsLambdaClient = new AWSLambdaClient(profileCredentialsProvide);
			//AWSLambdaClient awsLambdaClient = new AWSLambdaClient(profileCredentialsProvide,clientConfiguration);	

			//to be used with BasicSessionCredentials
			//AWSLambdaClient awsLambdaClient = new AWSLambdaClient(basicSessionCredentials);
			//AWSLambdaClient awsLambdaClient = new AWSLambdaClient(basicSessionCredentials,clientConfiguration);

			String lambdaoutput =null;
			String responsetime = null;
			String final_response=null;
			try{
				//for sync call
				InvokeRequest invokeRequest = new InvokeRequest();
				invokeRequest.setFunctionName(lambda_name);
				invokeRequest.setPayload("{\"x\":"+x+",\"y\":"+y+"}");
				lambdaoutput = byteBufferToString(awsLambdaClient.invoke(invokeRequest).getPayload(), Charset.forName("UTF-8"));
				//System.out.println(lambdaoutput);

				//for async call
				/*InvokeRequest invokeRequest = new InvokeRequest();
				invokeRequest.withFunctionName("arn:aws:lambda:us-east-1:022341759614:function:scoring").withPayload("{\"x\":9,\"y\":7}");
				InvokeResult invokeResult = awsLambdaClient.invoke(invokeRequest);
				System.out.println(invokeResult);*/

			}catch (Exception e)
			{
				System.out.println(e.getMessage());
				lambdaoutput = e.getMessage();
			}
			final String newline = System.getProperty("line.separator");
			StringBuilder sb = new StringBuilder(500);
			responsetime = "HTTPGET: Response Time is : "+ ( new Date().getTime()- start ) +"ms";
			final_response = "HTTPGET: Response from AWS Lambda - "+lambda_name+" :  "+lambdaoutput;

			System.out.println(final_response);
			System.out.println(responsetime);
			sb.append(final_response);
			sb.append(newline);
			sb.append("  ****  ");
			sb.append(responsetime);
			//return ("Response from AWS Lambda - "+lambda_name+" :  "+lambdaoutput + newline + responsetime);
			return sb.toString();
		}

	//default method is GET
	@RequestMapping(value="/lambda",method=RequestMethod.POST)
	public String invokeLambda(@RequestBody LambdaInput lambdaInput,@RequestParam("name") String lambda_name){

		String lambdaoutput = null,responsetime=null,final_response=null;
		long start = new Date().getTime();
		
		ProfileCredentialsProvider profileCredentialsProvide = new ProfileCredentialsProvider("sts");
		AWSLambdaClient awsLambdaClient = new AWSLambdaClient(profileCredentialsProvide);
		
		try{
			ObjectMapper objectMapper = new ObjectMapper();
			String modelInputJson = objectMapper.writeValueAsString(lambdaInput);
			
			InvokeRequest invokeRequest = new InvokeRequest();
			invokeRequest.setFunctionName(lambda_name);
			invokeRequest.setPayload(modelInputJson);
			lambdaoutput = byteBufferToString(awsLambdaClient.invoke(invokeRequest).getPayload(), Charset.forName("UTF-8"));
		}catch (Exception e)
		{
			System.out.println(e.getMessage());
			lambdaoutput = e.getMessage();
		}

		responsetime = "HTTPPOST: Response Time is : "+ ( new Date().getTime()- start ) +"ms";
		final_response = "HTTPPOST: Response from AWS Lambda - "+lambda_name+" :  "+lambdaoutput;

		System.out.println(final_response);
		System.out.println(responsetime);
	
		return lambdaoutput;
	}

	public static String byteBufferToString(ByteBuffer buffer, Charset charset){

		byte[] bytes;
		if(buffer.hasArray()){
			bytes = buffer.array();
		}else{
			bytes = new byte[buffer.remaining()];
			buffer.get(bytes);
		}
		return new String(bytes, charset);


	}

}
