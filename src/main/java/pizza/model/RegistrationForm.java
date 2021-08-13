package pizza.model;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;


@Data
public class RegistrationForm {

    @NotBlank(message = "Name is required")
    private String username;

    @NotBlank(message = "password cannot be blank")
    private String password;

    @NotBlank(message = "confirm cannot be blank")
    private String confirmPass;

    @NotBlank(message = "Name is required")
    private String fullName;

    @NotBlank(message = "street is required")
    private String street;

    @NotBlank(message = "city is required")
    private String city;

    @NotBlank(message = "state is required")
    private String state;

    @NotBlank(message = "zip is required")
    private String zip;

    @NotBlank(message = "phone is required")
    private String phone;

    public User toUser(PasswordEncoder encoder) {
        User user = new User(
                username, encoder.encode(password), fullName,
                street, city, state, zip, phone);
        user.setAuthority("USER");
        user.setEnabled(true);

        return user;
    }

    @AssertTrue(message="confirm field should be equal to password field")
    private boolean isValid() {
        return password.equals(confirmPass);
    }
}

