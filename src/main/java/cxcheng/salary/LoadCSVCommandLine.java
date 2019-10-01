package cxcheng.salary;

import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LoadCSVCommandLine implements CommandLineRunner {

    @Autowired
    private PersonService service;

    @Override
    public void run(String... args) throws Exception {
        // look for "--csv" to process
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equals("--csv") && i < (args.length - 1)) {
                service.readCSV(new FileInputStream(args[++i]));
            }
        }
    }

}
