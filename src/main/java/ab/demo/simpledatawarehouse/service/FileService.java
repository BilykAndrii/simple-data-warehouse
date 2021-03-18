package ab.demo.simpledatawarehouse.service;

import ab.demo.simpledatawarehouse.model.CampaignMetric;
import ab.demo.simpledatawarehouse.repo.CampaignMetricRepository;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@Slf4j
public class FileService {

    @Autowired
    CampaignMetricRepository metricsRepository;

    public int saveData(MultipartFile uploadedFile, boolean header, String dateFormat) {
        try {
            InputStream stream = uploadedFile.getInputStream();
            Reader reader = new InputStreamReader(stream);
            CSVReader csvReader = new CSVReader(reader);

            if (header) {
                csvReader.readNext();
            }

            String[] nextLine;
            List<CampaignMetric> metrics = new ArrayList<>();

            while ((nextLine = csvReader.readNext()) != null) {
                saveMetric(metrics, nextLine, dateFormat);
            }

            metricsRepository.saveAll(metrics);

            return metrics.size();

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File uploading error!", ex);
        }
    }

    private void saveMetric(List<CampaignMetric> metrics, String[] line, String dateFormat) {

        if (isBlank(line[0])) {
            return;
        }

        try {
            CampaignMetric metric = CampaignMetric.builder()
                    .dataSource(line[0])
                    .campaign(line[1])
                    .date(readDate(line[2], dateFormat))
                    .clicks(Long.valueOf(line[3]))
                    .impressions(Long.valueOf(line[4]))
                    .build();

            metrics.add(metric);
        } catch (Exception ex) {
            log.debug(ex.getMessage());
        }
    }

    private LocalDate readDate(String date, String dateFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        return LocalDate.parse(date, formatter);
    }

}
