package com.gemini.Contripoint.repository.implementation;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemini.Contripoint.config.S3StorageConfig;
import com.gemini.Contripoint.model.Event;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.model.RewardResponse;
import com.gemini.Contripoint.model.Winner;
import com.gemini.Contripoint.repository.interfaces.EnrolledRepository;
import com.gemini.Contripoint.repository.interfaces.EventRepository;
import com.gemini.Contripoint.repository.interfaces.WinnerRepository;
import com.gemini.Contripoint.service.implementation.InterviewServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RewardRepositoryImpl {
    public static final Logger log = LoggerFactory.getLogger(InterviewServiceImp.class);


    @Lazy
    @Autowired
    EventRepository eventRepository;

    @Autowired
    WinnerRepository winnerRepository;

    @Autowired
    EnrolledRepository enrolledRepository;

    @Autowired
    S3StorageConfig s3Client;

    @Value("${accessKey}")
    private String accessKey;

    @Value("${secretKey}")
    private String accessSecret;

    @Value("${region}")
    private String region;

    @Value("${bucket-name}")
    private String bucketName;


    public ResponseMessage viewSingleReward(Integer eventId, String empId) {
        log.debug("Inside viewSingleEvent (Repository) with parameters {}{}", eventId, empId);
        List<String> rewardDetails = eventRepository.viewSingleReward(eventId, empId); // Fetching a specific certificate
        List<ResponseMessage> rewardResponse = rewardDetails.stream().map(rank -> {
            String[] arr = rank.split(","); // Splitting to name and id's
            String eventName = arr[0];
            String startDate = arr[1];
            String endDate = arr[2];    // Getting designation
            StringBuilder url = new StringBuilder(arr[3]);    // getting position
            String certificateName = enrolledRepository.getCertificateName(eventId, empId);

            try {
                AmazonS3 s3 = s3Client.s3Client();

                // Set the presigned URL to expire after one hour.
                Date expiration = new Date();
                long expTimeMillis = Instant.now().toEpochMilli();
                expTimeMillis += 1000 * 60 * 60;
                expiration.setTime(expTimeMillis);

                // Generate the presigned URL.
                GeneratePresignedUrlRequest generatePresignedUrlRequest =
                        new GeneratePresignedUrlRequest(bucketName, certificateName)
                                .withMethod(HttpMethod.GET)
                                .withExpiration(expiration);
                URL url1 = s3.generatePresignedUrl(generatePresignedUrlRequest);
                url = new StringBuilder(String.valueOf(url1));

                //  System.out.println("Pre-Signed URL: " + url.toString());
            } catch (AmazonServiceException e) {
                // The call was transmitted successfully, but Amazon S3 couldn't process
                // it, so it returned an error response.
                e.printStackTrace();
            } catch (SdkClientException e) {
                // Amazon S3 couldn't be contacted for a response, or the client
                // couldn't parse the response from Amazon S3.
                e.printStackTrace();
            }


            return new ResponseMessage(eventName, startDate, endDate, url);     // returning
        }).collect(Collectors.toList());

        return new ResponseMessage("null", rewardResponse); // Sending empty array if there is no rewards

    }

    public ResponseMessage getAllReward(String empId, Integer pageNo) throws JsonProcessingException {

        log.debug("Inside getAllReward(Repository) with parameters {} {}", empId, pageNo);
        Pageable pageable = PageRequest.of(pageNo, 5);  // Creating a pageable and seting its size

        Page<Event> eventRewards = eventRepository.findAllByEmployeeId(empId, pageable);// Fetching page from database.
        Page<Winner> winnerRewards = winnerRepository.findAllByEmployeeIdd(empId, pageable);
        long totalRows = eventRewards.getTotalElements(); // Getting total elements

        List<RewardResponse> rewardResponseList1 = eventRewards.stream().map(event -> {
            RewardResponse rewardResponseList = new RewardResponse();
            rewardResponseList.setEventId(event.getId());
            rewardResponseList.setEventName(event.getSummary());

            //
            List<String> ranking = eventRepository.getEventLeaderboardd(event.getId());    // Fetching the data from database
            List<ResponseMessage> rankingResponse = ranking.stream().map(rank -> {
                String[] arr = rank.split(","); // Splitting to name and id's
                String name = arr[0];
                String idd = arr[1];
                int position = Integer.parseInt(arr[2]);    // getting position
                return new ResponseMessage(name, idd, position);     // returning
            }).collect(Collectors.toList());

            for (int i = 0; i < rankingResponse.size(); i++) {
                if (empId.equalsIgnoreCase((String) rankingResponse.get(i).getData())) {
                    rewardResponseList.setPosition((Integer) rankingResponse.get(i).getTotal_rows());
                }
            }
            //

            //Integer position = eventRepository.getEmployeeRank(event.getId(), empId);
            // rewardResponseList.setPosition(position);
            return rewardResponseList;
        }).collect(Collectors.toList());


        List<Winner> winnerList1 = winnerRewards.stream().map(winner -> {
            Winner winner1 = new Winner();
            winner1.setAmount(winner.getAmount());
            winner1.setRewardType(winner.getRewardType());
            winner1.setRewardDate(winner.getRewardDate());
            return winner1;
        }).collect(Collectors.toList());


        for (int i = 0; i < winnerList1.size(); i++) {
            rewardResponseList1.get(i).setReward(winnerList1.get(i).getRewardType());
            rewardResponseList1.get(i).setAmount(winnerList1.get(i).getAmount());
            rewardResponseList1.get(i).setRewardDate(winnerList1.get(i).getRewardDate());
        }
        return new ResponseMessage("null", rewardResponseList1, totalRows); // Sending empty array if there is no rewards
    }

}