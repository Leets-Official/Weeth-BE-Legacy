package leets.weeth.domain.user.service;

import jakarta.persistence.EntityExistsException;
import leets.weeth.domain.user.dto.UserDTO;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.entity.enums.Role;
import leets.weeth.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(UserDTO.SignUp requestDto) throws Exception {
        if (userRepository.findByUsername(requestDto.getUsername()).isPresent())
            throw new EntityExistsException("이미 존재하는 아이디입니다.");

        // 수정: 아이디 이외 중복 처리

        User user = User.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }
}
