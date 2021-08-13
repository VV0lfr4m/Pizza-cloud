package pizza.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pizza.model.User;
import pizza.repositories.jpa.JpaUserRepo;

@Service
@Qualifier("customUserDetailsServiceImpl")
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    JpaUserRepo jpaUserRepo;

    public CustomUserDetailsServiceImpl(JpaUserRepo jpaUserRepo) {
        this.jpaUserRepo = jpaUserRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = jpaUserRepo.findByUsername(username);

        return getUserOrThrowException(user, "User '" + username + "' not found");
    }


    public UserDetails loadUserById(long id) throws UsernameNotFoundException {
        User user = jpaUserRepo.findById(id);

        return getUserOrThrowException(user, "User with id= '" + id + "' not found");
    }

    private User getUserOrThrowException(User user, String message) {
        if (user != null) {
            return user;
        }
        throw new UsernameNotFoundException(message);
    }
}
