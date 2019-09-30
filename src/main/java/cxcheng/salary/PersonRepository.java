package cxcheng.salary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("select p from Person p where p.salary >= 0.0 and p.salary <= 4000.0 order by p.name")
    List<Person> findValidSalaries();

}
