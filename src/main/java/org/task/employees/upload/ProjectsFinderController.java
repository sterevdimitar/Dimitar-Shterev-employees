package org.task.employees.upload;


import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


public interface ProjectsFinderController {

    /**
     * Finds all projects for the longest working pair of employees from the uploaded file.
     *
     * @param file The file to be processed.
     * @return ResponseEntity can be a List of EmployeePair objects when the operation is successful,
     *         or an ApiError object when there's an error.
     */
    ResponseEntity<?> findAllProjectsForLongestWorkingPair(MultipartFile file);
}


