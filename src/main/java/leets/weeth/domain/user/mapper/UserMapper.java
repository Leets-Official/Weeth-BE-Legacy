package leets.weeth.domain.user.mapper;

import leets.weeth.domain.user.dto.UserDTO;
import leets.weeth.domain.user.entity.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // 수정: 모든 mapper 어노테이션 매핑 최소화
    @Mappings({
            @Mapping(target = "cardinals", expression = "java( java.util.List.of(dto.getCardinal()) )"),
            @Mapping(target = "password", expression = "java( passwordEncoder.encode(dto.getPassword()) )")
    })
    User from(UserDTO.SignUp dto, @Context PasswordEncoder passwordEncoder);
}

