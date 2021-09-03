package pl.fissst.lbd.lambda.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.fissst.lbd.damagereport.model.BetApi;
import pl.fissst.lbd.lambda.mapper.InvalidS3ObjectContentException;
import pl.fissst.lbd.lambda.mapper.S3ObjectContentMapper;


public class Handler implements RequestHandler<S3EventNotification, Void> {

    @Override
    public Void handleRequest(S3EventNotification event,
                              Context context) {

        String bucket = retrieveBucketName(event);
        String key = retrieveObjectKey(event);
        S3Object object = retrieveS3Object(bucket, key);

        try {
            BetApi betApi = mapToBetApi(object);
            sendMessageToSQS(betApi);
            moveS3ObjectToFolder("success/", key, bucket, "ok");

        } catch (InvalidS3ObjectContentException e) {
            moveS3ObjectToFolder("error/", key, bucket, "err");
        }

        return null;
    }

    private String retrieveBucketName(S3EventNotification event) {
        return event.getRecords()
                .get(0)
                .getS3()
                .getBucket()
                .getName();
    }

    private String retrieveObjectKey(S3EventNotification event) {
        return event.getRecords()
                .get(0)
                .getS3()
                .getObject()
                .getKey();
    }

    private S3Object retrieveS3Object(String bucket, String key) {
        AmazonS3 s3Client = AWSClient.getAmazonS3Client();
        return s3Client.getObject(
                new GetObjectRequest(bucket, key)
        );
    }

    private BetApi mapToBetApi(S3Object s3Object) {
        return S3ObjectContentMapper.mapToBetApi(s3Object);
    }

    private void sendMessageToSQS(BetApi betApi) {
        AmazonSQS sqsClient = AWSClient.getAmazonSQSClient();
        String queueUrl = sqsClient.getQueueUrl("lotto.bet").getQueueUrl();

        try {
            String message = new ObjectMapper().writeValueAsString(betApi);
            sqsClient.sendMessage(new SendMessageRequest(queueUrl, message));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void moveS3ObjectToFolder(String folderName, String key, String bucket, String suffix) {
        AmazonS3 s3Client = AWSClient.getAmazonS3Client();
        s3Client.copyObject(bucket, key, bucket, folderName + replaceSuffix(key, suffix));
        s3Client.deleteObject(bucket, key);
    }

    private String replaceSuffix(String key, String suffix) {
        return key.substring(0, key.length() - 3) + suffix;
    }
}