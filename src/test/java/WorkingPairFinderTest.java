import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.task.employees.Application;
import org.task.employees.find.WorkingPairFinder;
import org.task.employees.find.entity.EmployeePair;
import org.task.employees.find.io.CSVParseException;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ContextConfiguration(classes = Application.class)
class WorkingPairFinderTest {

    @Autowired
    private WorkingPairFinder finder;

    @Test
    void testFindLongestWorkingPair() throws Exception {
        URL resource = getClass().getClassLoader().getResource("test_main_case.csv");

        if (resource == null) {
            throw new IllegalArgumentException("File not found!");
        } else {
            List<EmployeePair> longestPairs = finder.findAllProjectsLongestWorkingPair(Paths.get(resource.toURI()));
            assertTrue(longestPairs.stream().anyMatch(pair -> pair.getEmployeeId1().equals("143") && pair.getEmployeeId2().equals("456")));
        }
    }

    @Test
    void testToday() throws Exception {
        URL resource = getClass().getClassLoader().getResource("test_today_case.csv");
        if (resource == null) {
            throw new IllegalArgumentException("File not found!");
        } else {
            List<EmployeePair> longestPairs = finder.findAllProjectsLongestWorkingPair(Paths.get(resource.toURI()));
            assertTrue(longestPairs.stream().anyMatch(pair -> pair.getEmployeeId1().equals("218") && pair.getEmployeeId2().equals("143")));
        }
    }

    @Test
    void testInvalidCSVFile() throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("invalid.csv");
        if (resource == null) {
            throw new IllegalArgumentException("File not found!");
        } else {
            Path path = Paths.get(resource.toURI());
            assertThrows(CSVParseException.class, () -> finder.findAllProjectsLongestWorkingPair(path));
        }
    }

}