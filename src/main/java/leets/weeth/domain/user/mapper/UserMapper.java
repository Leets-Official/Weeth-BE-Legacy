package leets.weeth.domain.user.mapper;

import leets.weeth.domain.user.dto.UserDTO;
import leets.weeth.domain.user.entity.User;
import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    // 수정: 모든 mapper 어노테이션 매핑 최소화
    @Mappings({
            @Mapping(target = "cardinals", expression = "java( java.util.List.of(dto.cardinal()) )"),
            @Mapping(target = "password", expression = "java( passwordEncoder.encode(dto.password()) )")
    })
    User from(UserDTO.SignUp dto, @Context PasswordEncoder passwordEncoder);

    UserDTO.Response to(User user);

    User update(Long userId, UserDTO.Update dto);
}

