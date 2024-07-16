package leets.weeth.domain.user.service;

import leets.weeth.domain.user.dto.UserDTO;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.mapper.UserMapper;
import leets.weeth.domain.user.repository.UserRepository;
import leets.weeth.global.common.error.exception.custom.BusinessLogicException;
import leets.weeth.global.common.error.exception.custom.EmailExistsException;
import leets.weeth.global.common.error.exception.custom.InvalidAccessException;
import leets.weeth.global.common.error.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static leets.weeth.domain.user.entity.enums.Status.ACTIVE;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public void signUp(UserDTO.SignUp requestDto) {
        if (userRepository.findByEmail(requestDto.email()).isPresent())
            throw new EmailExistsException();

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
                .orElseThrow(UserNotFoundException::new);

        if(!user.getStatus().equals(ACTIVE))
            throw new InvalidAccessException();

        user.applyOB(cardinal);
    }

    public Map<Integer, List<UserDTO.Response>> findUsers() {
        return userRepository.findAllByStatusOrderByName(ACTIVE).stream()
                .flatMap(user -> Stream.concat(
                        user.getCardinals().stream()
                                .map(cardinal -> new AbstractMap.SimpleEntry<>(cardinal, mapper.to(user))), // 기수별 Map
                        Stream.of(new AbstractMap.SimpleEntry<>(0, mapper.to(user)))    // 모든 기수는 cardinal 0에 저장
                ))
                .collect(Collectors.groupingBy(Map.Entry::getKey,   // key = 기수, value = 유저 정보
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
    }
}
