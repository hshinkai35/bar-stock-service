package api.security;

import api.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Userエンティティ
 */
public class BarUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public BarUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) {
        return userRepository.findByEmail(email)
                .map(BarLoginUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

}
