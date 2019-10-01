package cxcheng.salary;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import cxcheng.salary.Person;
import cxcheng.salary.PersonRepository;
import cxcheng.salary.PersonService;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PersonServiceTest {

    @Autowired
    private PersonRepository repository;

    private PersonService service;

    // count of valid salaries
    private int validCount = 0;

    @Before
    public void setupRepository() {
        service = new PersonService(repository);

        // Setup dummy data
        long count = 0;
        for (int i = 0; i < 100; ++i) {
            Person p = new Person();
            p.setName(String.format("John Doe %04d", i));
            p.setSalary(i - 50.0);
            repository.save(p);
            ++count;
            // update count of valid and invalid persons for later validation
            if (p.hasValidSalary()) {
                ++validCount;
            }
        }
        for (int i = 3950; i < 4050; ++i) {
            Person p = new Person();
            p.setName(String.format("John Doe %04d", i));
            p.setSalary(i);
            repository.save(p);
            ++count;
            if (p.hasValidSalary()) {
                ++validCount;
            }
        }
        assertThat(repository.count(), is(count));
    }

    @Test
    public void listValidPersons() throws Exception {
        var counter = new Object() {
            int value = 0;
        };
        service.listValidPersons().forEach(p -> {
            assertTrue(p.hasValidSalary());
            ++counter.value;
        });
        assertThat(counter.value, is(validCount));
    }

    @Test
    public void loadCSVFromFile() throws Exception {
        // check that we have data already loaded from setupRepository()
        assertTrue(service.listValidPersons().size() > 0);

        // now load from CSV file and check that the same set of rows have been read
        int successes = service.readCSV(this.getClass().getResourceAsStream("/us_presidents.csv"));
        var counter = new Object() {
            int value = 0;
        };
        repository.findAll().forEach(p -> {
            assertNotNull(p.getName());
            ++counter.value;
        });
        assertThat(counter.value, is(successes));
    }
}
