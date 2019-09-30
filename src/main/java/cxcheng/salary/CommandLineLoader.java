package cxcheng.salary;

import java.io.BufferedReader;
import java.io.FileReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;

import lombok.extern.java.Log;

@Component
@Log
public class CommandLineLoader implements CommandLineRunner {

    @Autowired
    private PersonRepository repository;

    private void readCSV(String csvPath) throws Exception {
        try (CSVReader csvReader = new CSVReader(new BufferedReader(new FileReader(csvPath)))) {
            // remove all current records
            log.fine("Deleting all existing records");
            repository.deleteAll();
            // read and parse each CSV line
            String[] line;
            int successes = 0;
            int errors = 0;
            while ((line = csvReader.readNext()) != null) {
                if (line.length == 2) {
                    try {
                        Person p = new Person(line[0], Double.parseDouble(line[1]));
                        repository.save(p);
                        ++successes;
                    } catch (Exception e) {
                        // continue with next one even if this one has errors
                        ++errors;
                    }
                }
            }
            log.fine(String.format("Importing CSV: %d successes %d errors", successes, errors));
        }
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equals("--csv") && i < (args.length - 1)) {
                readCSV(args[++i]);
            }
        }
    }

}
