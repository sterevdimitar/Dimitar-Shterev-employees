package org.task.employees.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.task.employees.find.io.CSVParseException;
import org.task.employees.find.entity.EmployeePair;
import org.task.employees.find.WorkingPairFinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class ProjectsFinderControllerImpl implements ProjectsFinderController {


    @Autowired
    private WorkingPairFinder workingPairFinder;

    @Override
    @PostMapping("/api/upload")
    public ResponseEntity<?> findAllProjectsForLongestWorkingPair(@RequestParam("file") MultipartFile file) {
        try {

            // Save the file locally in case of failed processing
            Path tempFile = Paths.get("temp/" + file.getOriginalFilename());

            Files.createDirectories(tempFile.getParent());
            Files.deleteIfExists(tempFile);
            Files.createFile(tempFile);
            Files.write(tempFile, file.getBytes());


            //process
            List<EmployeePair> allProjectsLongestWorkingPair = workingPairFinder.findAllProjectsLongestWorkingPair(tempFile);

            //clean the file
            Files.deleteIfExists(tempFile);

            return ResponseEntity.ok(allProjectsLongestWorkingPair);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError("Unexpected error: " + e.getMessage()));
        } catch (CSVParseException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError("Failed to parse CSV file: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError("Unexpected error: " + e.getMessage()));
        }
    }
}
