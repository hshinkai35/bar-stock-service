package api.security;

import api.entitiy.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;

public class BarLoginUser extends org.springframework.security.core.userdetails.User {

    // Userエンティティ
    private api.entitiy.User user;

    public User getUser() {
        return user;
    }

    public BarLoginUser(User user) {
        super(user.getName(), user.getPassword(), AuthorityUtils.createAuthorityList("ROLE_USER"));
        this.user = user;
    }

}