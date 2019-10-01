package cxcheng.salary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    // Find people in salary range.
    @Query("select p from Person p where p.salary >= ?1 and p.salary <= ?2 order by p.name")
    List<Person> findBySalaryRange(Double minSalary, Double maxSalary);

}
