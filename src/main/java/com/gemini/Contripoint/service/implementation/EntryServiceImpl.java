package com.gemini.Contripoint.service.implementation;

import com.amazonaws.services.s3.AmazonS3;
import com.gemini.Contripoint.config.S3StorageConfig;
import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.*;
import com.gemini.Contripoint.repository.implementation.EntryRepositoryImpl;
import com.gemini.Contripoint.repository.interfaces.EmployeeRepository;
import com.gemini.Contripoint.repository.interfaces.EnrolledRepository;
import com.gemini.Contripoint.repository.interfaces.EventRepository;
import com.gemini.Contripoint.service.interfaces.EmailService;
import com.gemini.Contripoint.service.interfaces.EntryService;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EntryServiceImpl implements EntryService {
    public static final Logger log = LoggerFactory.getLogger(InterviewServiceImp.class);

    @Autowired
    EntryRepositoryImpl entryRepositoryImpl;
    @Autowired
    S3StorageConfig s3Client;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    EnrolledRepository enrolledRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    EmailService emailService;
    @Value("${accessKey}")
    private String accessKey;
    @Value("${secretKey}")
    private String accessSecret;
    @Value("${region}")
    private String region;
    @Value("${bucket-name}")
    private String bucketName;

    @Override
    public Entry addEntry(Entry entry) throws IOException {
        log.info("Inside addEntry() (EntryServiceImpl) with parameters, {}", entry);
        return entryRepositoryImpl.addEntry(entry);
    }

    @Override
    public List<ViewAllEntryModel> viewAllEntries(String data) throws IOException {

        log.debug("Inside viewAllEntries (Serviceimp) with parameters {} ", data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            Integer eventId = jsonObject.getInt("eventId");
            Integer pageNo = jsonObject.getInt("pageNo");
            pageNo--;
            return entryRepositoryImpl.viewAllEntries(eventId, pageNo);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public EntryResponseMessage findListOfEntries(String id) throws IOException {
        log.info("Inside addEntry() (EntryServiceImpl) with parameters, {}", id);
        return entryRepositoryImpl.findListOfEntries(Integer.parseInt(id));
    }

    @Override
    public ResponseMessage getSingleEntry(String entryId) {
        log.debug("Inside getSingleEntry (Service) with parameters {}", entryId);
        try {
            return entryRepositoryImpl.getSingleEntry(Integer.parseInt(entryId));
        } catch (Exception e) {
            throw new ContripointException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String publishCertificates(String eventId) throws IOException {
        createParticipationPDF(Integer.parseInt(eventId));
        emailService.sendNonContestEndEmail(Integer.parseInt(eventId));
        List<Winner> winners = new ArrayList<>();
        Event event = eventRepository.getEventById(Integer.parseInt(eventId));
        event.getEnrolled().forEach(enrolled -> {
            if (enrolled.isEnrolled() == true) {
                Winner winner = new Winner(enrolled.getEmployee());
                winners.add(winner);
            }
        });
        event.setWinners(winners);
        eventRepository.save(event);
        return "Published";
    }

    @Async
    public void createParticipationPDF(int eventId) throws IOException {
        try {
            File theDir = new File("/home/" + "contripoint_temp_files");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            Event event = eventRepository.getEventById(eventId);
            String eventName = event.getSummary();
            String endDate = event.getEndDate().toString();
            String endDate1 = endDate.substring(0, 10); //for yyyy-mm-dd  this format
            String endDay = endDate.substring(8, 10);  // print only date
            String year = endDate.substring(0, 4);  //for extract only year
            String startDate = eventRepository.getEventById(eventId).getStartDate().toString();
            String startDay = startDate.substring(8, 10);  //to get day
            String startDate11 = startDate.substring(0, 10);
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
                    + NameOfTheMonth.substring(1).toLowerCase();
            int sYear = currentDate.getYear(); //fetching year for startDate11
            LocalDate currentDate1 = LocalDate.parse(endDate1); //get instance of local time from date
            int eDay = currentDate1.getDayOfMonth(); //get day from enddate1
            String eDayStr = eDay + suffixes[eDay];  //add suffix to the day (1st, 2nd, 3rd, 4th)
            String eMonth = currentDate1.getMonth().toString(); //getting month from enddate1
            eMonth = eMonth.substring(0, 1).toUpperCase()  //changing emonth to CamelCase
                    + eMonth.substring(1).toLowerCase(); // cutting upto 4 letters
            int eYear = currentDate1.getYear(); //fetching year for endDate1

            String dateFormate = null;
            if (sDay == eDay && NameOfTheMonth.equals(eMonth) && sYear == eYear) {
                dateFormate = sDayStr + " " + NameOfTheMonth + ", " + sYear;  //15 jan - 15 Jan 2022 //on 15 Jan 2022
            } else if (sDay != eDay && NameOfTheMonth.equals(eMonth) && sYear == eYear) {
                dateFormate = sDayStr + " " + NameOfTheMonth + ", " + sYear + " to " + eDayStr + " " + eMonth + ", " + eYear;    //15-17jan 2022
            } else if (sDay != eDay && NameOfTheMonth != eMonth && sYear == eYear) {
                dateFormate = sDayStr + " " + NameOfTheMonth + ", " + sYear + " to " + eDayStr + " " + eMonth + ", " + sYear;  //15 jan - 20 feb 2022
            } else if (sDay != eDay && NameOfTheMonth != eMonth && sYear != eYear) {
                dateFormate = sDayStr + " " + NameOfTheMonth + ", " + sYear + " to " + eDayStr + " " + eMonth + ", " + eYear;  //15 Dec 2021 to 20 Jan 2022
            } else if (sDay == eDay && NameOfTheMonth != eMonth && sYear == eYear) {
                dateFormate = sDayStr + " " + NameOfTheMonth + ", " + sYear + " to " + eDayStr + " " + eMonth + ", " + sYear; // 15 Jan 2021 to 15 Feb 2021
            } else if (sDay == eDay && NameOfTheMonth.equals(eMonth) && sYear != eYear) {
                dateFormate = sDayStr + " " + NameOfTheMonth + ", " + sYear + " to " + eDayStr + " " + eMonth + ", " + eYear; //15 Jan 2021 to 15 Jan 2022
            }
            List<String> emplEnrolledAndWithEntry = enrolledRepository.getEnrolledwithEntries(eventId);
            String participantsCertificate = new ClassPathResource("templates/Non-Winner-certificate.pdf").getPath();
            for (String empId : emplEnrolledAndWithEntry) {
                String certificateFileName = empId + "_" + eventName + ".pdf";
                String newFilePath = "/home/" + "contripoint_temp_files/" + certificateFileName;
                Employee emp = employeeRepository.getById(empId);
                String newName = emp.getName();

                //Create PdfReader instance.
                PdfReader pdfReader =
                        new PdfReader(participantsCertificate);

                //Create PdfStamper instance.
                PdfStamper pdfStamper = new PdfStamper(pdfReader,
                        new FileOutputStream(newFilePath));

                //Create BaseFont instance.
                String poppins = new ClassPathResource("Assets/Poppins-Regular.ttf").getPath();
                BaseFont baseFont = BaseFont.createFont(
                        poppins, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

                PdfContentByte pageContentByte = pdfStamper.getOverContent(1);

                String fancy = new ClassPathResource("Assets/Edwardian Script ITC.ttf").getPath();

                Rectangle mediabox = pdfReader.getCropBox(1);
                float x = mediabox.getRight() / 2;
                float y = mediabox.getBottom() + mediabox.getHeight() / 2 - 160;

                String str = newName;
                String underText = "For your active participation in our event";
                String text2 = eventName;
                String text3 = " held from " + dateFormate;
                if (sDay == eDay && NameOfTheMonth.equals(eMonth) && sYear == eYear) {
                    dateFormate = sDayStr + " " + NameOfTheMonth + ", " + sYear;  //15 jan - 15 Jan 2022
                    text3 = " held on " + dateFormate;
                }

                pageContentByte.beginText();
                pageContentByte.setFontAndSize(BaseFont.createFont(fancy, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED), 92);
                pageContentByte.setRGBColorFill(155, 47, 155);
                pageContentByte.showTextAligned(1, str, x, y, 0);
                pageContentByte.endText();

                pageContentByte.beginText();
                pageContentByte.setFontAndSize(baseFont, 28);
                pageContentByte.resetRGBColorFill();
                pageContentByte.showTextAligned(1, underText, x, y - 72, 0);
                pageContentByte.endText();

                pageContentByte.beginText();
                pageContentByte.setFontAndSize(baseFont, 28);
                pageContentByte.setRGBColorFill(155, 47, 155);
                pageContentByte.showTextAligned(1, text2, x, y - 110, 0);
                pageContentByte.endText();

                pageContentByte.beginText();
                pageContentByte.setFontAndSize(baseFont, 28);
                pageContentByte.resetRGBColorFill();
                pageContentByte.showTextAligned(1, text3, x, y - 146, 0);
                pageContentByte.endText();

                //Close the pdfStamper.
                pdfStamper.close();

                AmazonS3 s3 = s3Client.s3Client();
                s3.putObject(bucketName, certificateFileName, new File(newFilePath));
                enrolledRepository.updateCertificateName(certificateFileName, eventId, empId);

                System.out.println("PDF modified successfully.");
                System.out.println("Finding the path to delete the file");
                Files.deleteIfExists(Paths.get(newFilePath));
                System.out.println("Deleted File");
            }
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public EnrolledNotEnrolledResponse getEnrolledNotEnrolled(String data) {
        log.info("Inside getEnrolledNotEnrolled with parameters {}", data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            String eventId = jsonObject.getString("eventId");
            String empId = jsonObject.getString("empId");
            return entryRepositoryImpl.getEnrolledNotEnrolled(eventId, empId);
        } catch (Exception e) {
            throw new ContripointException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
