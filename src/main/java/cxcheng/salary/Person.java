/**
 * 
 */
package cxcheng.salary;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Person entity
 * 
 * @author cxcheng
 *
 */

@Entity
@NoArgsConstructor
@JsonIgnoreProperties("id") // do not include ID in JSON output
public class Person {
    private @Id @GeneratedValue Long id;
    private @NonNull String name;
    private double salary;


    public boolean hasValidSalary() {
        return salary >= 0.0 && salary <= 4000.0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
