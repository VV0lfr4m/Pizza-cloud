package pizza.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Entity
@Table(name = "jpa_user")
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    private final String username;

    private final String password;

    @Column(name = "full_name")
    private final String fullName;

    private final String street;

    private final String city;

    private final String state;

    private final String zip;

    @Column(name = "phone_number")
    private final String phoneNumber;

    private boolean enabled;

    private String authority;


    public User(String username) {
        this.username = username;
        this.password = null;
        this.fullName = null;
        this.street = null;
        this.city = null;
        this.state = null;
        this.zip = null;
        this.phoneNumber = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}

