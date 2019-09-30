/**
 * 
 */
package cxcheng.salary;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author cxcheng
 *
 */

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@JsonIgnoreProperties("id")
public class Person {
    private @Id @GeneratedValue Long id;
    private @NonNull String name;
    private @NonNull double salary;
}
