package example.security.servlet.jwt;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class JwtUserService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;

    public JwtUserService(){
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public UserDetails getUserLoginInfo(String username){
        String salt = "123456ef";
        UserDetails user = loadUserByUsername(username);
        return User.builder().username(user.getUsername()).password(salt).authorities(user.getAuthorities()).build();
    }

    public String saveUserLoginInfo(UserDetails userDetails){
        String salt = "123456ef";
        // todo 返回 jwt 信息
        return salt;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return User.builder().username("jack").password(passwordEncoder.encode("password")).build();
    }

    public void createUser(String username, String password){
        String encryptPwd = passwordEncoder.encode(password);
        // todo 保存到数据库
    }

    public void deleteUserLoginInfo(String username){
        // todo 清除 token
    }
}
