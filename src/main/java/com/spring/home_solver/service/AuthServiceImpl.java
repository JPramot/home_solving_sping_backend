package com.spring.home_solver.service;

import com.spring.home_solver.DTO.*;
import com.spring.home_solver.entity.User;
import com.spring.home_solver.enumulation.Role;
import com.spring.home_solver.exception.ApiErrorExc;
import com.spring.home_solver.repository.UserRepository;
import com.spring.home_solver.security.jwt.JwtUtils;
import com.spring.home_solver.security.service.UserDetailImpl;
import com.spring.home_solver.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    @Transactional
    public UserInfoResponseDTO register(UserRegisterDTO body) {

        logger.info("register user ======> begin");

        User existsUser = userRepository.findByEmail(body.getEmail())
                .orElse(null);
        if(existsUser != null) throw new ApiErrorExc("email was used");

        existsUser = userRepository.findByUsername(body.getUsername())
                .orElse(null);
        if(existsUser != null) throw new ApiErrorExc("username was used");

        String passwordEncode = passwordEncoder.encode(body.getPassword());
        User newUser = new User(body.getUsername(), body.getEmail(),
                passwordEncode);
        userRepository.save(newUser);

        logger.info("register user success ======> {}",newUser);

        return login(new UserLoginDTO(body.getUsername(), body.getPassword()));
    }

    public UserInfoResponseDTO login(UserLoginDTO body){

        logger.info("login begin ==============> {}",body.getPassword());
        Authentication authUser = authenticationUser(body);

        SecurityContextHolder.getContext().setAuthentication(authUser);

        UserDetailImpl userDetail = (UserDetailImpl) authUser.getPrincipal();

        logger.info("userdetail ============>");

        String token = jwtUtils.generateJwtFromUsername(userDetail);

        Role role = checkRoleUser(userDetail.getAuthorities());

        UserInfoDTO userInfo = modelMapper.map(userDetail, UserInfoDTO.class);
        userInfo.setRole(role);

        return new UserInfoResponseDTO(token,userInfo);

    }

    @Override
    public UserWithProfileResponseDTO getMe() {
        User loginUser = authUtil.loginUser();

        UserWithProfileDTO userInfoDTO = modelMapper.map(loginUser, UserWithProfileDTO.class);

        return new UserWithProfileResponseDTO(userInfoDTO);
    }

    private Role checkRoleUser(Collection<? extends GrantedAuthority> authorities) {
        boolean  checkUserRole = authorities
                .stream().map(GrantedAuthority::getAuthority)
                .anyMatch(role->role.equals("ROLE_USER"));
        if(checkUserRole) {
            return Role.user;
        }else {
            return Role.admin;
        }
    }

    private Authentication authenticationUser(UserLoginDTO body) {
        UsernamePasswordAuthenticationToken userInfo =
                new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());
        return authenticationManager.authenticate(userInfo);
    }


}
