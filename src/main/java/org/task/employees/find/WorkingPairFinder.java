package org.task.employees.find;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.task.employees.find.entity.Employee;
import org.task.employees.find.entity.EmployeePair;
import org.task.employees.find.io.CSVParseException;
import org.task.employees.find.io.FileParserHelper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Component responsible for finding the longest working pair of employees across all projects based on a CSV file.
 *
 */
@Component
public class WorkingPairFinder {


    private final FileParserHelper fileParserHelper;

    public WorkingPairFinder(@Autowired FileParserHelper fileParserHelper) {
        this.fileParserHelper = fileParserHelper;
    }

    /**
     * Finds all projects of the longest working pair of employees, provided in a CSV file.
     *
     * @param filePath the path to the CSV file
     * @return a list of EmployeePair objects
     * @throws CSVParseException if there is an error validating or parsing the CSV file
     */
    public List<EmployeePair> findAllProjectsLongestWorkingPair(Path filePath) {
        Map<String, List<Employee>> projectMap;
        try {
            fileParserHelper.validateCSV(filePath);
            projectMap = fileParserHelper.parseCSV(filePath);
        } catch (CSVParseException e) {
            throw e;
        } catch (Exception e) {
            throw new CSVParseException("Error parsing CSV file", e);
        }

        EmployeePair longestWorkingPair = findLongestWorkingPair(projectMap);
        return findAllProjectsForPair(projectMap, longestWorkingPair);
    }

    /**
     * Finds the longest working pair of employees across all projects.
     *
     * @param projectMap a map containing project IDs as keys and lists of employees as values
     * @return the longest working pair of employees
     */
    protected EmployeePair findLongestWorkingPair(Map<String, List<Employee>> projectMap) {

        EmployeePair longestPair = null;
        long longestDuration = 0;
        for (List<Employee> employees : projectMap.values()) {
            for (int i = 0; i < employees.size(); i++) {
                for (int j = i + 1; j < employees.size(); j++) {
                    Employee e1 = employees.get(i);
                    Employee e2 = employees.get(j);
                    long overlap = Math.min(e1.getDateTo().getTime(), e2.getDateTo().getTime()) - Math.max(e1.getDateFrom().getTime(), e2.getDateFrom().getTime());
                    if (overlap > longestDuration) {
                        longestDuration = overlap;
                        longestPair = new EmployeePair(e1.getEmpId(), e2.getEmpId(), null, overlap / (1000 * 60 * 60 * 24));
                    }
                }
            }
        }
        return longestPair;
    }

    /**
     * Finds all projects in which the given pair of employees have worked together.
     *
     * @param projectMap a map containing project IDs as keys and lists of employees as values
     * @param pair       the pair of employees
     * @return a list of EmployeePair
     */
    protected List<EmployeePair> findAllProjectsForPair(Map<String, List<Employee>> projectMap, EmployeePair pair) {
        List<EmployeePair> projects = new ArrayList<>();

        for (Map.Entry<String, List<Employee>> entry : projectMap.entrySet()) {
            List<Employee> employees = entry.getValue();
            boolean foundFirst = false;
            boolean foundSecond = false;

            for (Employee employee : employees) {
                if (employee.getEmpId().equals(pair.getEmployeeId1())) {
                    foundFirst = true;
                } else if (employee.getEmpId().equals(pair.getEmployeeId2())) {
                    foundSecond = true;
                }

                if (foundFirst && foundSecond) {
                    projects.add(new EmployeePair(pair.getEmployeeId1(), pair.getEmployeeId2(), entry.getKey(), pair.getDaysWorked()));
                    break;
                }
            }
        }
        return projects;
    }

}

