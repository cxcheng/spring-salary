package cxcheng.salary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.java.Log;

@RestController
@Log
public class PersonController {

    private final PersonRepository repository;

    PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> users() {

        List<Person> validSalaries = repository.findValidSalaries().stream().collect(Collectors.toList());
        Map<String, List<Person>> results = new HashMap<String, List<Person>>();
        results.put("results", validSalaries);
        return new ResponseEntity<Object>(results, HttpStatus.OK);
    }
}
