package com.fabio.aws;

import java.io.IOException;
import java.io.InputStream;


import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.utils.IoUtils;

public class S3Util {
	public static final String BUCKET = "fabio-test-first-bucket";
	static String keyFile = "";
	static S3Client client;
	
	public static void uploadFile(String fileName, InputStream inputStream) throws S3Exception, AwsServiceException, SdkClientException, IOException{
		setProperties();
		keyFile = fileName;
		client = S3Client.builder().build();
		
		PutObjectRequest request = PutObjectRequest.builder()
				.bucket(BUCKET)
				.key(fileName)
				.acl("public-read")
				.contentType("image/jpg")			
				.build();
		
		client.putObject(request, RequestBody.fromInputStream(inputStream, inputStream.available()));
	}
	
	private static void setProperties() {
		System.setProperty("aws.region", "sa-east-1");
		System.setProperty("aws.accessKeyId", "");
		System.setProperty("aws.secretAccessKey", "");
	}
	
	public static byte[] downloadFromPostman(String fileName) throws IOException {
		keyFile = fileName;
		client = S3Client.builder().build();
		
		return downloadFile();
	}
	
	public static byte[] downloadFile() throws IOException {
		GetObjectRequest request = GetObjectRequest.builder()
				.bucket(BUCKET)
				.key(keyFile)
				.build();
		
		ResponseInputStream<GetObjectResponse> response = client.getObject(request);

		return IoUtils.toByteArray(response);
	}
	
}
