package spring.cloud.azure.starter.data.cosmos.sample.aks;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import org.springframework.data.annotation.Id;

@Container(containerName = "users")
public class User {
    @Id
    @PartitionKey
    private String email;
    private String firstName;
    private String lastName;
    private String city;

    public User() {

    }

    public User(String email, String firstName, String lastName, String city) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "User [city=" + city + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }

}