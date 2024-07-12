package leets.weeth.domain.user.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import leets.weeth.domain.user.dto.UserDTO;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.entity.enums.Status;
import leets.weeth.domain.user.mapper.UserMapper;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper = UserMapper.INSTANCE;
    private final PasswordEncoder passwordEncoder;

    public void signUp(UserDTO.SignUp requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent())
            throw new EntityExistsException("이미 존재하는 아이디입니다.");

        // 수정: 아이디 이외 중복 처리

        User user = mapper.from(requestDto, passwordEncoder);
        userRepository.save(user);
    }

    @Transactional
    public void delete(Long userId) {
        userRepository.findById(userId)
                .ifPresent(User::leave);
    }

    @Transactional
    public void applyOB(Long userId, Integer cardinal) throws BusinessLogicException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        if(!user.getStatus().equals(Status.ACTIVE))
            throw new BusinessLogicException("올바르지 않은 접근입니다.");

        user.applyOB(cardinal);
    }

}
