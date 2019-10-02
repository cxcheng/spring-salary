package cxcheng.salary;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;

import lombok.extern.java.Log;

@Service
@Log
public class PersonService {

    private PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public int readCSV(InputStream in) throws Exception {
        try (CSVReader csvReader = new CSVReader(new BufferedReader(new InputStreamReader(in)))) {
            // remove all current records
            log.info("Deleting all existing records");
            repository.deleteAll();
            // read and parse each CSV line
            String[] line;
            int successes = 0;
            int errors = 0;
            while ((line = csvReader.readNext()) != null) {
                if (line.length == 2) {
                    try {
                        Person p = new Person();
                        p.setName(line[0]);
                        p.setSalary(Double.parseDouble(line[1]));
                        repository.save(p);
                        ++successes;
                    } catch (Exception e) {
                        // ignore errors with individual rows and move on to next
                        ++errors;
                    }
                } else if (!(line.length == 1 && line[0].length() == 0)) {
                    // incorrect number of columns, but ignore blank line
                    ++errors;
                }
            }
            log.info(String.format("Importing CSV: %d successes %d errors", successes, errors));
            return successes;
        }
    }

    public List<Person> listValidPersons() {
        // The filter() is technically not necessary as the query already returns the
        // required set
        // This is kept to highlight app-level filtering
        return repository.findBySalaryRange(0.0, 4000.0).stream().filter(p -> p.hasValidSalary())
                .collect(Collectors.toList());
    }
}
