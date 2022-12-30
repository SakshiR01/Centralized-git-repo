package com.gemini.Contripoint.config;

import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.Employee;
import com.gemini.Contripoint.model.MisData;
import com.gemini.Contripoint.repository.interfaces.EmployeeRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

@Component
@EnableScheduling
public class StartUpInit {
    private final Logger log = LoggerFactory.getLogger(StartUpInit.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MisData misData;

    @PostConstruct
    @Scheduled(cron = "${CRON_EXP}")
    public void init() {
        log.debug("Current Time is :: " + Calendar.getInstance().getTime());
        try {
            URL url = new URL(misData.getUrl() + "?key=" + misData.getKey() + "&token=" + misData.getToken());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), StandardCharsets.UTF_8));
            try {
                String jsonOutput;
                StringBuffer jsonString = new StringBuffer();

                while ((jsonOutput = br.readLine()) != null)
                    jsonString.append(jsonOutput);

                conn.disconnect();

                JSONObject jsonObject = new JSONObject(String.valueOf(jsonString));
                JSONArray result = (JSONArray) jsonObject.get("Result");

                List<Employee> newEmployee = new ArrayList<>();
                Set<String> idSet = employeeRepository.getAllEmpId();
                Set<String> emailSet = employeeRepository.getAllEmail();

                for (int i = 0; i < result.length(); i++) {
                    JSONObject innerObject = (JSONObject) result.get(i);
                    String email = innerObject.getString("Email").toLowerCase();
                    String name = innerObject.getString("EmployeeName");
                    String id = innerObject.getString("EmployeeCode");
                    String designation = innerObject.getString("Designation");
                    String rmId = innerObject.getString("ManagerCode");
                    byte[] image = innerObject.getString("ImagePath").getBytes(StandardCharsets.UTF_8);
                    String team = innerObject.getString("Team");
                    if (!idSet.contains(id) && emailSet.contains(email)) {
                        employeeRepository.updateEmployeeRecord(email, id, designation, team, rmId, name, image);
                    } else {
                        Employee employee = new Employee(id, name, designation, email, team, image, rmId);
                        newEmployee.add(employee);
                    }
                }
                employeeRepository.saveAll(newEmployee);
            } finally {
                br.close();
            }
        } catch (RuntimeException e) {
            log.error("Encountered runtime Exception in init()");
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("error in init() : " + e);
        }

    }
}
