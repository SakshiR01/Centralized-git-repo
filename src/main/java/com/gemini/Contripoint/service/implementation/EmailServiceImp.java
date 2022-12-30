package com.gemini.Contripoint.service.implementation;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.gemini.Contripoint.config.S3StorageConfig;
import com.gemini.Contripoint.controller.CertificateController;
import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.*;
import com.gemini.Contripoint.repository.interfaces.*;
import com.gemini.Contripoint.service.interfaces.EmailService;
import com.gemini.Contripoint.service.interfaces.EncryptDecryptService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class EmailServiceImp implements EmailService {

    public static final Logger log = LoggerFactory.getLogger(CertificateController.class);
    @Autowired
    ContributionsRepository contributionsRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    ContributionCategoryRepository contributionCategoryRepository;
    @Autowired
    Configuration configuration;
    @Autowired
    EnrolledRepository enrolledRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    S3StorageConfig s3Client;
    @Autowired
    JavaMailSender sender;
    @Autowired
    EncryptDecryptService encryptDecryptService;
    @Autowired
    WinnerRepository winnerRepository;
    @Value("${accessKey}")
    private String accessKey;

    @Value("${secretKey}")
    private String accessSecret;

    @Value("${region}")
    private String region;

    @Value("${bucket-name}")
    private String bucketName;

    @Override
    @Async
    public EmailResponse sendMail() throws IOException {
        try {
            EmailNotification emailNotification = new EmailNotification();   // Creating a new object of emailNotification
            EmailResponse response = new EmailResponse();
            MimeMessage message = sender.createMimeMessage(); // Creating a new MimeMessage
            try {
                List<String> mail = new ArrayList<>();
                Map<String, Object> model = new HashMap<>();
                List<String> name = contributionsRepository.getManagerId();  // Getting the list of managers who have contributions margin-left for approval.

                for (int i = 0; i < name.size(); i++) {
                    mail.add(i, employeeRepository.getEmail(name.get(i)));    // Adding their mail id to list
                }
                for (int i = 0; i < name.size(); i++)      // for each manager
                {
                    String n = name.get(i);
                    String ManagerName = employeeRepository.getName(n); // Getting manager name
                    int ManagerPendingCount = contributionsRepository.getManagerPendingCount(n);  // getting total pending count
                    List<String> SpecificContributionCount = contributionsRepository.getSpecificContributionCount(n);

                    ArrayList<ContributionCount> contributionCountList = new ArrayList<>();
                    for (int j = 0; j < SpecificContributionCount.size(); j++) {
                        String[] count = SpecificContributionCount.get(j).split(",");
                        String contributionCategory = contributionCategoryRepository.findContributionName(Integer.parseInt(count[0]));    // Getting contribution name
                        int contributionCategorySize = Integer.parseInt(count[1]);   // getting unread notification count
                        ContributionCount contributionCount = new ContributionCount(contributionCategory, contributionCategorySize);
                        contributionCountList.add(contributionCount);
                    }
                    List<ContributionCount> finalList = new ArrayList<ContributionCount>(Collections.nCopies(8, null));
                    // Setting model structure for email template
                    for (int k = 0; k < contributionCountList.size(); k++) {
                        if (contributionCountList.get(k).getContributionCategory().equalsIgnoreCase("certificate")) {
                            finalList.set(0, contributionCountList.get(k));
                        } else if (contributionCountList.get(k).getContributionCategory().equalsIgnoreCase("clientfeedback")) {
                            finalList.set(1, contributionCountList.get(k));
                        } else if (contributionCountList.get(k).getContributionCategory().equalsIgnoreCase("interview")) {
                            finalList.set(2, contributionCountList.get(k));
                        } else if (contributionCountList.get(k).getContributionCategory().equalsIgnoreCase("trainingsession")) {
                            finalList.set(3, contributionCountList.get(k));
                        } else if (contributionCountList.get(k).getContributionCategory().equalsIgnoreCase("mentorship")) {
                            finalList.set(4, contributionCountList.get(k));
                        } else if (contributionCountList.get(k).getContributionCategory().equalsIgnoreCase("projects")) {
                            finalList.set(5, contributionCountList.get(k));
                        } else if (contributionCountList.get(k).getContributionCategory().equalsIgnoreCase("teambuilding")) {
                            finalList.set(6, contributionCountList.get(k));
                        } else if (contributionCountList.get(k).getContributionCategory().equalsIgnoreCase("selfdevelopment")) {
                            finalList.set(7, contributionCountList.get(k));
                        }
                    }

                    EmailTemplate emailTemplate = new EmailTemplate(ManagerName, ManagerPendingCount, finalList);

                    model.put("emailTemplate", emailTemplate); // Adding model to emailTemplate
                    MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());


                    Template t = configuration.getTemplate("email-template.ftl");    // adding the html file here

                    String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

                    emailNotification.setTo(mail.get(i));    // Setting send to

                    emailNotification.setFrom("contripoint@geminisolutions.in");    // Adding email id

                    emailNotification.setSubject("Pending Contributions for Approval");  // Setting subject

                    helper.setTo(emailNotification.getTo());

                    helper.setText(html, true);

                    helper.setSubject(emailNotification.getSubject());

                    helper.setFrom(emailNotification.getFrom());

                    helper.addInline("background", new ClassPathResource("Assets/Android2.jpg")); // Setting background

                    // Adding image assets if present else doing nothing

                    helper.addInline("hourglass", new ClassPathResource("Assets/Hourglass.jpg"));

                    if (emailTemplate.getContributionCountList().get(0) != null) {
                        helper.addInline("certifications", new ClassPathResource("Assets/certifications.jpg"));
                    }

                    if (emailTemplate.getContributionCountList().get(2) != null) {
                        helper.addInline("interviews-taken", new ClassPathResource("Assets/interviews-taken.jpg"));
                    }

                    if (emailTemplate.getContributionCountList().get(3) != null) {
                        helper.addInline("trainings-sessions", new ClassPathResource("Assets/trainings-sessions.jpg"));
                    }

                    if (emailTemplate.getContributionCountList().get(4) != null) {
                        helper.addInline("mentorship", new ClassPathResource("Assets/mentorship.jpg"));
                    }

                    if (emailTemplate.getContributionCountList().get(5) != null) {
                        helper.addInline("projects", new ClassPathResource("Assets/Projects.jpg"));
                    }

                    if (emailTemplate.getContributionCountList().get(6) != null) {
                        helper.addInline("team-building", new ClassPathResource("Assets/team-building.jpg"));
                    }

                    if (emailTemplate.getContributionCountList().get(1) != null) {
                        helper.addInline("client-feedback", new ClassPathResource("Assets/client-feedback.jpg"));
                    }

                    if (emailTemplate.getContributionCountList().get(7) != null) {
                        helper.addInline("self-development", new ClassPathResource("Assets/self-development.jpg"));
                    }
                    // Sending message
                    sender.send(message);
                }
                response.setMessage("mail send to : " + emailNotification.getTo());
                response.setStatus(Boolean.TRUE);
            } catch (MessagingException | IOException | TemplateException e) {
                response.setMessage("Mail Sending failure : " + e);
                response.setStatus(Boolean.FALSE);
            }
            return response;
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Async
    public void sendEnrolledEmail(String empId, int eventId) throws IOException {
        log.debug("Inside sendEnrolledEmail() (ServiceImpl) with parameters {}", empId);
        MimeMessage message = sender.createMimeMessage();   //initializing MimeMessage object
//        MimeMultipart multipart = new MimeMultipart("related"); //initializing MimeMultipart
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED,
                    StandardCharsets.UTF_8.name()); //setting up MimeMessageHelper

            Employee emp = employeeRepository.getById(empId);   //fetching the employee via the id

            if (emp == null) {   //checking if such an employee exists
                throw new ContripointException("Employee Not Found", HttpStatus.NOT_FOUND); //setting up a condition in case employee is not found
            }

            String empName = emp.getName(); //adding employee name to template variable
            Event event = eventRepository.getEventById(eventId);
            String eventName = event.getSummary();
//            String eventName = eventRepository.getEventName(emp.getId());    //adding event name to template variable


            EnrolledEmail enrolledEmail = new EnrolledEmail(empName, eventName);    //initializing template variable model

            Map<String, Object> model = new HashMap<>();    //initializing object for storing enrolledEmail model
            model.put("e", enrolledEmail);  //setting key and value to enrolled email via which the template will access the variables

            Template t = configuration.getTemplate("enrolled-email-template.ftl");  //fetching the ftl template

            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);  //setting the variable inside the template to the model we initialized before

            helper.setTo(emp.getEmail());   //setting the recipient to the email of the employee whose id has been provided
//            helper.setTo("riknirikku@vusra.com");  //for testing purposes
//            helper.setTo("suvansh.shukla@geminisolutions.in");
            helper.setText(html, true);  //setting the text (body) of the email to be the ftl template
            helper.setSubject("You Have Successfully Enrolled In an Event!");   //setting the subject of the email

            helper.addInline("background", new ClassPathResource("Assets/Android2.jpg"));   //setting the background image

            helper.addInline("enrolled", new ClassPathResource("Assets/enrolled-min.png"));

            sender.send(message);   //sending the email

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Async
    public void sendWinnerNonwinnerEmails(Integer eventId, List<String> winnerList) throws IOException { // CHANGE HERE IN NON WINNER LIST
        log.debug("Inside sendWinnerNonwinnerEmails() (EmailServiceImp) with parameters {}, {}", eventId, winnerList);

        try {
            File theDir = new File("/home/" + "contripoint_temp_files");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }

            Event event = eventRepository.getById(eventId);
            String eventName = event.getSummary();
            String eventRewards = event.getRewards();

            List<String> participants = eventRepository.getEmployeesInEvent(eventId);
            participants.removeAll(winnerList);


            for (String winner : winnerList) {

                System.out.println(winner);  //empId

                String rewardType = winnerRepository.getRewardType(winner, eventId);

                String rewardType1 = "Amazon Coupon";


                if (rewardType.equalsIgnoreCase(rewardType1)) {

                    System.out.println("Inside rewardType.equalsIngoreCase()");
                    MimeMessage message1 = sender.createMimeMessage();   //initializing MimeMessage object
//                MimeMultipart multipart = new MimeMultipart("related");

                    MimeMessageHelper helper = new MimeMessageHelper(message1, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                            StandardCharsets.UTF_8.name());

                    Employee employee = employeeRepository.getById(winner);


                    String employeeEmail = employee.getEmail();
                    String employeeName = employee.getName();

                    String certificateFileName = enrolledRepository.getCertificateName(eventId, employee.getId());


                    final AmazonS3 s3 = s3Client.s3Client();
                    S3Object object = s3.getObject(new GetObjectRequest(bucketName, certificateFileName));

                    byte[] certificate = new byte[10000];
                    S3ObjectInputStream s3ObjectInputStream = object.getObjectContent();
                    try {
                        certificate = IOUtils.toByteArray(s3ObjectInputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String newPath = "/home/" + "contripoint_temp_files/" + certificateFileName;

                    File file = new File(newPath);
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(certificate);
                    } catch (Exception e) {
                        throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
                    }

                    AtomicReference<String> code = new AtomicReference<>();

                    event.getWinners().forEach(w -> {
                        if (w.getEmpId().equalsIgnoreCase(winner)) {
                            String vocher = w.getVoucher();
                            code.set(encryptDecryptService.decrypt(vocher));
                        }
                    });

                    WinnerNonwinnerEmail winnerNonwinnerEmail = new WinnerNonwinnerEmail(employeeName, eventName, eventRewards, code);

                    Map<String, Object> model = new HashMap<>();
                    model.put("winnerNonwinnerEmail", winnerNonwinnerEmail);

                    Template t = configuration.getTemplate("winner-email.ftl");

                    String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

                    helper.setTo(employeeEmail);    //setting receiver email
                    //  helper.setTo("pamidimarri.sai@geminisolutions.com");
                    helper.setSubject("Congratulations! You've Won!");    //setting email subject
                    helper.setText(html, true);  //setting email body
                    helper.addAttachment("Certificate.pdf", file);
                    //helper.addInline("background", new ClassPathResource("Assets/Android2.jpg"));
                    helper.addInline("trophy", new ClassPathResource("Assets/trophy-min.png"));

                    System.out.println("sending email to " + winner);
                    sender.send(message1);   //sending the email | uncomment to send email
                    System.out.println("Finding the path to delete the file");
                    Path fileToDeletePath = Paths.get(newPath);
                    System.out.println("Deleting the file");
                    Files.delete(fileToDeletePath);
                    System.out.println("Deleted File");
                }

            }

            for (String nonWinner : participants) {
                MimeMessage message2 = sender.createMimeMessage();   //initializing MimeMessage object
//                MimeMultipart multipart = new MimeMultipart("related");

                MimeMessageHelper helper = new MimeMessageHelper(message2, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                        StandardCharsets.UTF_8.name());

                Employee employee = employeeRepository.getById(nonWinner);
                String employeeEmail1 = employee.getEmail();
                String employeeName1 = employee.getName();

                String certificateFileName = enrolledRepository.getCertificateName(eventId, employee.getId());


                final AmazonS3 s3 = s3Client.s3Client();
                S3Object object = s3.getObject(new GetObjectRequest(bucketName, certificateFileName));

                byte[] certificate = new byte[10000];
                S3ObjectInputStream s3ObjectInputStream = object.getObjectContent();
                try {
                    certificate = IOUtils.toByteArray(s3ObjectInputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String newPath = "/home/" + "contripoint_temp_files/" + certificateFileName;
                File file = new File(newPath);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(certificate);
                } catch (Exception e) {
                    throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
                }


                WinnerNonwinnerEmail winnerNonwinnerEmail = new WinnerNonwinnerEmail(employeeName1, eventName, eventRewards);

                Map<String, Object> model = new HashMap<>();
                model.put("winnerNonwinnerEmail", winnerNonwinnerEmail);

                Template t = configuration.getTemplate("non-winner-email-template.ftl");

                String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

                helper.setTo(employeeEmail1);    //setting receiver email
                //   helper.setTo("pamidimarri.sai@geminisolutions.com");
                helper.setSubject(eventName + " Event Results Are Out");    //setting email subject
                helper.setText(html, true);  //setting email body

                helper.addAttachment("Certificate.pdf", file);


                helper.addInline("background", new ClassPathResource("Assets/Android2.jpg"));
                helper.addInline("nonwinnerimage", new ClassPathResource("Assets/nonwinnerimage.jpg"));
                sender.send(message2);   //sending the email | uncomment to send email
                System.out.println("Finding the path to delete the file");
                Path fileToDeletePath = Paths.get(newPath);
                System.out.println("Deleting the file");
                Files.delete(fileToDeletePath);
                System.out.println("Deleted File");
            }

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Async
    public String eventInvitation(Integer id) throws IOException, ParseException {
        log.info("Inside startEventEmail() (EmailServiceImp) with parameters {}" + id);

        try {
            File theDir = new File("/home/" + "contripoint_temp_files");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }

            EmailNotification emailNotification = new EmailNotification();

            EmailResponse response = new EmailResponse();
            MimeMessage message = sender.createMimeMessage();  // creating an object for mail function

            try {
                Map<String, Object> model = new HashMap<>();

                List<String> employeeIds = null;

                List<String> matchingEmployeeIds = new ArrayList<>();

                String eventName = eventRepository.getById(id).getSummary();
                String description = eventRepository.getEventById(id).getDescription();

                String endDate = eventRepository.getEventById(id).getEndDate().toString();
                String endDate1 = endDate.substring(0, 10); //for yyyy-mm-dd  this format
                //  System.out.println(endDate1);


                String endDay = endDate.substring(8, 10);  // print only date

                String year = endDate.substring(0, 4);  //for extract only year


                String startDate = eventRepository.getEventById(id).getStartDate().toString();
                String startDay = startDate.substring(8, 10);  //to get day

                String startDate11 = startDate.substring(0, 10);
                //    System.out.println(startDate11);


                LocalDate currentDate = LocalDate.parse(startDate11);       //getting instance of Local time for Date

                int sDay = currentDate.getDayOfMonth(); //get day from startDate11

                String[] suffixes =
                        //    0     1     2     3     4     5     6     7     8     9
                        {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                                //    10    11    12    13    14    15    16    17    18    19
                                "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                                //    20    21    22    23    24    25    26    27    28    29
                                "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                                //    30    31
                                "th", "st"};


                String sDayStr = sDay + suffixes[sDay]; //add suffix to the day (1st, 2nd, 3rd, 4th)

                String NameOfTheMonth = currentDate.getMonth().toString();  //to get month name

                NameOfTheMonth = NameOfTheMonth.substring(0, 1).toUpperCase()  //changing NameOfTheMonth to CamelCase for first 3 letters
                        + NameOfTheMonth.substring(1, 3).toLowerCase();

                int sYear = currentDate.getYear(); //fetching year for startDate11


                LocalDate currentDate1 = LocalDate.parse(endDate1); //get instance of local time from date

                int eDay = currentDate1.getDayOfMonth(); //get day from enddate1
                String eDayStr = eDay + suffixes[sDay];  //add suffix to the day (1st, 2nd, 3rd, 4th)

                String eMonth = currentDate1.getMonth().toString(); //getting month from enddate1

                eMonth = eMonth.substring(0, 1).toUpperCase()  //changing emonth to CamelCase
                        + eMonth.substring(1, 3).toLowerCase(); // cutting upto 4 letters

                int eYear = currentDate1.getYear(); //fetching year for endDate1


                String dateFormate = null;
                if (sDay == eDay && NameOfTheMonth.equals(eMonth) && sYear == eYear) {
                    dateFormate = sDayStr + " " + NameOfTheMonth + " - " + eDayStr + " " + eMonth + " " + sYear;  //15 jan - 15 Jan 2022
                } else if (sDay != eDay && NameOfTheMonth.equals(eMonth) && sYear == eYear) {
                    dateFormate = sDayStr + " - " + eDayStr + " " + eMonth + " " + eYear;    //15-17jan 2022
                } else if (sDay != eDay && NameOfTheMonth != eMonth && sYear == eYear) {
                    dateFormate = sDayStr + " " + NameOfTheMonth + " - " + eDayStr + " " + eMonth + " " + sYear;  //15 jan - 20 feb 2022
                } else if (sDay != eDay && NameOfTheMonth != eMonth && sYear != eYear) {
                    dateFormate = sDayStr + " " + NameOfTheMonth + " " + sYear + " - " + eDayStr + " " + eMonth + " " + eYear;  //15 Dec 2021 to 20 Jan 2022
                } else if (sDay == eDay && NameOfTheMonth != eMonth && sYear == eYear) {
                    dateFormate = sDayStr + " " + NameOfTheMonth + " - " + eDayStr + " " + eMonth + " " + sYear; // 15 Jan 2021 to 15 Feb 2021
                } else if (sDay == eDay && NameOfTheMonth.equals(eMonth) && sYear != eYear) {
                    dateFormate = sDayStr + " " + NameOfTheMonth + sYear + " - " + eDayStr + " " + eMonth + " " + eYear; //15 Jan 2021 to 15 Jan 2022
                }

                /**
                 * USING BASE 64 ENCODED VERSION
                 BufferedImage img = ImageIO.read(new ByteArrayInputStream(bannerImage));
                 byte[] encoded = Base64.getEncoder().encode(bannerImage);
                 String imageAsBase64 = new String(encoded);
                 */

                /**
                 * USING CREATING A NEW IMAGE IN ASSETS FOLDER
                 *                  */
                String name = eventName;
                String bannerFileName = eventRepository.getEventById(id).getBannerFileName();


                final AmazonS3 s3 = s3Client.s3Client();
                S3Object object = s3.getObject(new GetObjectRequest(bucketName, bannerFileName));

                byte[] bannerImage = new byte[10000];
                S3ObjectInputStream s3ObjectInputStream = object.getObjectContent();
                try {
                    bannerImage = IOUtils.toByteArray(s3ObjectInputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String newPath = "/home/" + "contripoint_temp_files/" + bannerFileName + ".jpg";

                File file = new File(newPath);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(bannerImage);
                } catch (Exception e) {
                    throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
                }

                employeeIds = eventRepository.getAllEmployeeByEventId(id);  //getting employee id's of the event
                for (String employeeId : employeeIds) {
                    matchingEmployeeIds.add(employeeId);  //adding  employee id  to matchingEmployeeIds list
                }
                for (String employeeId : matchingEmployeeIds) {
                    String EmployeeName = employeeRepository.getName(employeeId);
                    String email = eventRepository.getEmailByEmployeeId(employeeId);  // getting mail address to those matchingEmployeeIds

                    EventInvitationTemplate eventInvitationTemplate = new EventInvitationTemplate(EmployeeName, eventName, endDate1, description, dateFormate);
                    model.put("EventInvitationTemplate", eventInvitationTemplate);// adding model to template
                    MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
                    Template t = configuration.getTemplate("EventInvitation-Template.ftl");    // adding the html file here
                    String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);


                    emailNotification.setTo(email);
                    emailNotification.setFrom("contripoint@geminisolutions.in");
                    emailNotification.setSubject("Event Invitation");

                    helper.setTo(emailNotification.getTo());
                    helper.setText(html, true);


                    /**
                     * ADDING THE DYNAMICALLY ADDED IMAGE
                     *                     */
                    helper.addInline("image", new File(newPath));

                    helper.setSubject(emailNotification.getSubject());
                    helper.setFrom(emailNotification.getFrom());

                    System.out.println("Sending the mail to " + email);
                    sender.send(message);

                }
                System.out.println("Finding the path to delete the file");
                Path fileToDeletePath = Paths.get(newPath);
                System.out.println("Deleting the file");
                Files.delete(fileToDeletePath);
                System.out.println("Deleted File");
                response.setMessage("mail send to : " + emailNotification.getTo());
                response.setStatus(Boolean.TRUE);

            } catch (MessagingException | IOException | TemplateException e) {
                response.setMessage("Mail Sending failure : " + e);
                response.setStatus(Boolean.FALSE);
            }
            return "Event started";
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public EmailResponse sendReminderMail() throws IOException {
        log.info("inside sendReminderMail ");
        try {
            EmailNotification emailNotification = new EmailNotification();   // Creating a new object of emailNotification
            EmailResponse response = new EmailResponse();
            MimeMessage message = sender.createMimeMessage();
            try {
                List<Event> ongoingEvents = eventRepository.getOngoingEvents();

                for (Event event : ongoingEvents) {

                    int id = event.getId();
                    String endDateTime = eventRepository.getEventById(id).getEndDate().toString();
                    String endDate = endDateTime.substring(0, 10);

                    // int id = event.getId();Thread.sleep(1000);
                    LocalDate localDate = LocalDate.now();
                    String dateToday = localDate.toString();
                    LocalDate localdateAfterThreeDays = localDate.plusDays(3);
                    String dateAfterThreeDays = localdateAfterThreeDays.toString();
                    if (endDate.equalsIgnoreCase(dateAfterThreeDays) || endDate.equalsIgnoreCase(dateToday)) {
                        List<String> mail = new ArrayList<>();
                        Map<String, Object> model = new HashMap<>();
                        List<String> employeeIds = null;

                        List<String> matchingEmployeeIds = new ArrayList<>();
                        String description = eventRepository.getEventById(id).getDescription();

                        String EventName = eventRepository.getById(id).getSummary();
                        employeeIds = eventRepository.getAllEmployeeByEventId(id);  //getting employee id's of the event
                        for (String employeeId : employeeIds) {
                            if (enrolledRepository.checkEnrolled(id, employeeId)) { //checking employee is enrolled into event or not
                                matchingEmployeeIds.add(employeeId);
                            }//adding  employee id  to matchingEmployeeIds list
                        }
                        for (String employeeId : matchingEmployeeIds) {
                            MimeMessage message1 = sender.createMimeMessage();
                            String EmployeeName = employeeRepository.getName(employeeId);
                            String email = eventRepository.getEmailByEmployeeId(employeeId);  // getting mail address to those matchingEmployeeIds
                            EmailReminderTemplate emailReminderTemplate = new EmailReminderTemplate(EventName, EmployeeName);
                            model.put("EmailReminderTemplate", emailReminderTemplate);// adding model to template
                            MimeMessageHelper helper = new MimeMessageHelper(message1, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
                            Template t;
                            if (endDate.equalsIgnoreCase(dateAfterThreeDays)) {
                                t = configuration.getTemplate("reminder-email-soon.ftl");
                            } else {
                                t = configuration.getTemplate("reminder-email-today.ftl");
                            }
                            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                            emailNotification.setTo(email);
                            emailNotification.setFrom("contripoint@geminisolutions.in");
                            emailNotification.setSubject("Event Reminder");

                            helper.setTo(emailNotification.getTo());
//                            helper.setTo("rohit.kumar@geminisolutions.in");
                            helper.setText(html, true);
                            helper.addInline("background", new ClassPathResource("Assets/Android2.jpg"));
                            helper.addInline("clock", new ClassPathResource("Assets/clock.png"));
                            helper.setSubject(emailNotification.getSubject());
                            helper.setFrom(emailNotification.getFrom());
                            sender.send(message1);
                            System.out.println("mail sent");

                        }
                        response.setMessage("mail send to : " + emailNotification.getTo());
                        response.setStatus(Boolean.TRUE);
                    }
                }
            } catch (MessagingException | IOException | TemplateException e) {
                response.setMessage("Mail Sending failure : " + e);
                response.setStatus(Boolean.FALSE);

            }
            return response;
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    @Async
    public String rmApprovedEmail(Integer id) {

        EmailNotification emailNotification = new EmailNotification();

        EmailResponse response = new EmailResponse();
        MimeMessage message = sender.createMimeMessage();  // creating an object for mail function
        EmailServiceImp emailService = null;
        try {

            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            Map<String, Object> model = new HashMap<>();
            List<String> employeeIds = null;


            Event event = eventRepository.getEventById(id);
            String adminid = event.getAdminEmployeeId();
            System.out.println(adminid);
            String emailadmin = employeeRepository.getEmail(adminid);
            String adminname = employeeRepository.getName(adminid);
            String eventName = event.getSummary();

            RmApprovedRejected rmApprovedRejected = new RmApprovedRejected(adminname, eventName);
            rmApprovedRejected.setAdminname(adminname);
            model.put("RmApprovedRejected", rmApprovedRejected);
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            Template t = configuration.getTemplate("Rm-Approve-email.ftl");    // adding the html file here
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            emailNotification.setTo(emailadmin);
            emailNotification.setFrom("contripoint@geminisolutions.in");
            emailNotification.setSubject("Event Approved");

            helper.setTo(emailNotification.getTo());
            helper.setText(html, true);
            helper.addInline("Approve", new ClassPathResource("Assets/Approve.png"));
            helper.setSubject(emailNotification.getSubject());
            helper.setFrom(emailNotification.getFrom());
            sender.send(message);


        } catch (MessagingException e) {
            response.setMessage("Mail Sending failure : " + e);
            response.setStatus(Boolean.FALSE);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return "Mail Sent";
    }

    @Override
    @Async
    public String rmDeclinedEmail(Integer id) {

        EmailNotification emailNotification = new EmailNotification();

        EmailResponse response = new EmailResponse();
        MimeMessage message = sender.createMimeMessage();  // creating an object for mail function
        EmailServiceImp emailService = null;
        try {

            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            Map<String, Object> model = new HashMap<>();
            List<String> employeeIds = null;


            Event event = eventRepository.getEventById(id);
            String adminid = event.getAdminEmployeeId();
            String emailadmin = employeeRepository.getEmail(adminid);
            String adminname = employeeRepository.getName(adminid);
            String eventName = event.getSummary();


            RmApprovedRejected rmApprovedRejected = new RmApprovedRejected(adminname, eventName);
            model.put("RmApprovedRejected", rmApprovedRejected);
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            Template t = configuration.getTemplate("Rm-Declined-email.ftl");    // adding the html file here
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            emailNotification.setTo(emailadmin);
            emailNotification.setFrom("contripoint@geminisolutions.in");
            emailNotification.setSubject("Event Declined");

            helper.setTo(emailNotification.getTo());
            helper.setText(html, true);
            helper.addInline("Decline", new ClassPathResource("Assets/Decline.png"));
            helper.setSubject(emailNotification.getSubject());
            helper.setFrom(emailNotification.getFrom());
            sender.send(message);


        } catch (MessagingException e) {
            response.setMessage("Mail Sending failure : " + e);
            response.setStatus(Boolean.FALSE);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return "Mail Sent";
    }

    @Async
    public void PendingActivityApproval(int id) {
        EmailNotification emailNotification = new EmailNotification();
        EmailResponse response = new EmailResponse();
        MimeMessage message = sender.createMimeMessage();  // creating an object for mail function
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            Map<String, Object> model = new HashMap<>();

            Event event = eventRepository.getEventById(id);
            String eventName = event.getSummary();
            String eventDescription = event.getDescription();
            String startDate1 = new SimpleDateFormat("dd/MM/yyyy").format(event.getStartDate());
            String endDate1 = new SimpleDateFormat("dd/MM/yyyy").format(event.getEndDate());
            String adminid = event.getAdminEmployeeId();
            String rmId = employeeRepository.getrmIdByEmployeeId(adminid);
            String rmName = employeeRepository.getName(rmId);
            String rmMail = employeeRepository.getEmail(rmId);


            PendingActivityApproval pendingActivityApproval = new PendingActivityApproval(rmName, eventName, startDate1, endDate1, eventDescription);
            model.put("PendingActivityApproval", pendingActivityApproval);
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            Template t = configuration.getTemplate("PendingActivityApproval.ftl");    // adding the html file here
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            emailNotification.setTo(rmMail);
            emailNotification.setFrom("contripoint@geminisolutions.in");
            emailNotification.setSubject("Pending Activity For Approval");

            helper.setTo(emailNotification.getTo());
            helper.setText(html, true);
            helper.addInline("Pending", new ClassPathResource("Assets/Pending.png"));
            helper.setSubject(emailNotification.getSubject());
            helper.setFrom(emailNotification.getFrom());
            sender.send(message);


        } catch (MessagingException e) {
            response.setMessage("Mail Sending failure : " + e);
            response.setStatus(Boolean.FALSE);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Async
    public void sendNonContestEndEmail(Integer eventId) {
        try {
            File theDir = new File("/home/" + "contripoint_temp_files");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            Event event = eventRepository.getEventById(eventId);
            event.getEnrolled().forEach(enrolled -> {
                MimeMessage message = sender.createMimeMessage();
                if (enrolled.getEntry() != null) {
                    Employee emp = employeeRepository.getById(enrolled.getEmployee());

                    String certificateFileName = enrolledRepository.getCertificateName(eventId, emp.getId());


                    final AmazonS3 s3 = s3Client.s3Client();
                    S3Object object = s3.getObject(new GetObjectRequest(bucketName, certificateFileName));

                    byte[] certificate = new byte[10000];
                    S3ObjectInputStream s3ObjectInputStream = object.getObjectContent();
                    try {
                        certificate = IOUtils.toByteArray(s3ObjectInputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String newPath = "/home/" + "contripoint_temp_files/" + certificateFileName;

                    File file = new File(newPath);
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(certificate);
                    } catch (Exception e) {
                        throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
                    }

                    Map<String, String> info = new HashMap<>();
                    info.put("eventName", event.getSummary());
                    info.put("employeeName", emp.getName());

                    Map<String, Object> model = new HashMap<>();
                    model.put("e", info);

                    Template t = null;  //fetching the ftl template
                    try {
                        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED,
                                StandardCharsets.UTF_8.name());
                        t = configuration.getTemplate("non-contest-participants-email.ftl");
                        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);  //setting the variable inside the template to the model we initialized before
                        helper.setTo(emp.getEmail());
                        helper.setSubject("Certificate of Participation for " + event.getSummary());
                        helper.setText(html, true);
                        helper.addInline("background", new ClassPathResource("Assets/Android2.jpg"));   //setting the background image
                        helper.addInline("enrolled", new ClassPathResource("Assets/enrolled-min.png"));
                        helper.addAttachment("Certificate.pdf", file);
                        sender.send(message);
                        System.out.println("PDF modified successfully.");
                        System.out.println("Finding the path to delete the file");
                        Files.deleteIfExists(Paths.get(newPath));
                        System.out.println("Deleted File");
                    } catch (IOException | TemplateException | MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Async
    public EmailResponse RemainderEmailFromAdmin() {
        log.info("Inside Remainder Email from Admin");

        try {
            EmailNotification emailNotification = new EmailNotification();
            EmailResponse response = new EmailResponse();
            MimeMessage message = sender.createMimeMessage();  // creating an object for mail function


            try {
                List<Event> upcomingongoingEvents = eventRepository.getApprovedAndOngoingEvents();

                for (Event event : upcomingongoingEvents) {

                    int id = event.getId();
                    String startDate = new SimpleDateFormat("dd MMM ,yyyy").format(eventRepository.getEventById(id).getStartDate());

                    LocalDate localDate = LocalDate.now();
                    String dateToday = String.valueOf(new SimpleDateFormat("dd MMM , yyyy"));

                    if (startDate.equalsIgnoreCase(dateToday)) {
                        List<String> mail = new ArrayList<>();
                        Map<String, Object> model = new HashMap<>();
                        List<String> employeeIds = null;

                        List<String> matchingEmployeeIds = new ArrayList<>();
                        employeeIds = eventRepository.getAllEmployeeByEventId(id);

                        String eventName = event.getSummary();
                        String eventDescription = event.getDescription();


                        String name = eventName;
                        String bannerFileName = eventRepository.getEventById(id).getBannerFileName();


                        final AmazonS3 s3 = s3Client.s3Client();
                        S3Object object = s3.getObject(new GetObjectRequest(bucketName, bannerFileName));

                        byte[] bannerImage = new byte[10000];
                        S3ObjectInputStream s3ObjectInputStream = object.getObjectContent();
                        try {
                            bannerImage = IOUtils.toByteArray(s3ObjectInputStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String currentPath = new java.io.File(".").getCanonicalPath();
                        String newPath = "/home/" + "contripoint_temp_files/" + bannerFileName + ".jpg";

                        File file = new File(newPath);
                        try (FileOutputStream fos = new FileOutputStream(file)) {
                            fos.write(bannerImage);
                        } catch (Exception e) {
                            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
                        }


                        for (String employeeId : employeeIds) {
                            if (!enrolledRepository.checkEnrolled(id, employeeId)) { //checking employee is enrolled into event or not
                                matchingEmployeeIds.add(employeeId);
                            }//adding  employee id  to matchingEmployeeIds list
                        }
                        for (String employeeId : matchingEmployeeIds) {

                            MimeMessage message1 = sender.createMimeMessage();

                            String employeeName = employeeRepository.getName(employeeId);
                            String email = employeeRepository.getEmail(employeeId);
                            RemainderEmailFromAdmin remainderEmailFromAdmin = new RemainderEmailFromAdmin(employeeName, eventName, eventDescription, startDate);
                            model.put("RemainderEmailFromAdmin", remainderEmailFromAdmin);
                            MimeMessageHelper helper = new MimeMessageHelper(message1, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
                            Template t = configuration.getTemplate("RemainderEmailFromAdmin.ftl");    // adding the html file here
                            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

                            emailNotification.setTo(email);
                            emailNotification.setFrom("contripoint@geminisolutions.in");
                            emailNotification.setSubject("Remainder Email From Admin");

                            helper.setTo(emailNotification.getTo());
                            helper.setText(html, true);

                            helper.addInline("event", new ClassPathResource("Assets/EnrollEvent.png"));
                            helper.addInline("image", new File(newPath));

                            helper.setSubject(emailNotification.getSubject());
                            helper.setFrom(emailNotification.getFrom());
                            sender.send(message1);


                            response.setMessage("mail send to : " + emailNotification.getTo());
                            response.setStatus(Boolean.TRUE);

                        }
                        Path fileToDeletePath = Paths.get(newPath);
                        Files.delete(fileToDeletePath);
                    }

                }

            } catch (MessagingException e) {
                response.setMessage("Mail Sending failure : " + e);
                response.setStatus(Boolean.FALSE);
            }
            return response;
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Async
    public void winnerListEmail(Integer eventId) throws IOException {
        MimeMessage message = sender.createMimeMessage();

        try {

            ArrayList<String> winnerList = new ArrayList<>();
            Event event = eventRepository.getEventById(eventId);
            event.getWinners().forEach(winner -> {
                winnerList.add(winner.getEmpId());
            });

            List<String> partcipants = eventRepository.getAllParticipants(eventId);
            List<String> receivers = new ArrayList<>();
            partcipants.forEach(participant -> {
                receivers.add(employeeRepository.getById(participant).getEmail());
            });

            receivers.forEach(receiver -> {
                String employeeName = employeeRepository.getByEmail(receiver).getName();
                String eventName = eventRepository.getEventById(eventId).getSummary();

                ArrayList listOfRankers = new ArrayList<>();

                for (String empId : winnerList) {
                    HashMap ranker = new HashMap<>();
                    Employee employee = employeeRepository.getById(empId);
                    ranker.put("rankerPic", new String(employee.getImage()));
                    ranker.put("rankerName", employee.getName());
                    ranker.put("rankerDesignation", employee.getDesignation());
                    listOfRankers.add(ranker);
                }
                WinnerNonwinnerEmail winnerNonwinnerEmail = new WinnerNonwinnerEmail(employeeName, eventName, listOfRankers);
                Map<String, Object> model = new HashMap<>();
                model.put("e", winnerNonwinnerEmail);
                try {
                    MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED,
                            StandardCharsets.UTF_8.name());
                    Template t = configuration.getTemplate("NEW-Winner-Email-Template.ftl");
                    String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);  //setting the variable inside the template to the model we initialized before
                    helper.setTo(receiver); //replace with your own email please
                    helper.setSubject("Winners Are Out For " + eventName);
                    helper.setText(html, true);
                    helper.addInline("background", new ClassPathResource("Assets/Android2.jpg"));   //setting the background image
                    helper.addInline("new-winner", new ClassPathResource("Assets/new-winner-img-min.png"));
                    helper.addInline("trophyImg", new ClassPathResource("Assets/New-Trophy-Image.png"));
                    sender.send(message);
                } catch (IOException | TemplateException | MessagingException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
