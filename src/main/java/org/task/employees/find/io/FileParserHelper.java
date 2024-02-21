package org.task.employees.find.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.task.employees.find.entity.Employee;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to help with file parsing operations
 */
@Component
public class FileParserHelper {

    //Available date formats. Ordering is important.
    private static final String[] formats = {
            "yyyy-MM-dd",
            "dd-mm-yyyy", //Eu and most countries
            "MM-dd-yyyy", //USA
            "MM/dd/yyyy",
            "dd-MM-yyyy",
    };

    public Date parseDate(String dateString) {
        for (String format : formats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                return sdf.parse(dateString);
            } catch (ParseException e) {
                //log the parsing
            }
        }
        throw new CSVParseException("Date string could not be parsed. " + dateString);
    }

    /**
     * Parses the CSV file by the given file path.
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public Map<String, List<Employee>> parseCSV(Path filePath) throws Exception {
        Map<String, List<Employee>> projectMap = new HashMap<>();
        try (Reader in = new FileReader(filePath.toFile())) {
            Iterable<CSVRecord> csvLines = CSVFormat.DEFAULT.withHeader().parse(in);
            for (CSVRecord line : csvLines) {
                String empId = line.get("EmpID");
                String projectId = line.get("ProjectID");
                Date dateFrom = parseDate(line.get("DateFrom"));
                Date dateTo = line.get("DateTo").equals("NULL") ? new Date() : parseDate(line.get("DateTo"));

                Employee employee = new Employee(empId, dateFrom, dateTo);
                if (!projectMap.containsKey(projectId)) {
                    projectMap.put(projectId, new ArrayList<>());
                }
                projectMap.get(projectId).add(employee);
            }
        }
        return projectMap;
    }

    /**
     *  Validates the CSV file by the given file path.
     * @param filePath
     * @throws CSVParseException
     */
    public void validateCSV(Path filePath) throws CSVParseException {
        try (Reader in = new FileReader(filePath.toFile())) {
            CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader().withSkipHeaderRecord();
            CSVParser csvFileParser = new CSVParser(in, csvFileFormat);
            if (!csvFileParser.getHeaderNames().containsAll(Arrays.asList("EmpID", "ProjectID", "DateFrom", "DateTo"))) {
                throw new CSVParseException("Invalid CSV file format");
            }
            if (!csvFileParser.iterator().hasNext()) {
                throw new CSVParseException("CSV file is empty");
            }
        } catch (IOException e) {
            throw new CSVParseException("Error reading CSV file", e);
        }
    }

}
