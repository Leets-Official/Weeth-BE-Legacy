package leets.weeth.domain.user.mapper;

import leets.weeth.domain.user.dto.UserDTO;
import leets.weeth.domain.user.entity.User;
import leets.weeth.domain.user.entity.enums.Department;
import leets.weeth.global.common.error.exception.custom.DepartmentNotFoundException;
import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mappings({
            @Mapping(target = "cardinals", expression = "java( java.util.List.of(dto.cardinal()) )"),
            @Mapping(target = "password", expression = "java( passwordEncoder.encode(dto.password()) )")
    })
    User from(UserDTO.SignUp dto, @Context PasswordEncoder passwordEncoder);

    UserDTO.Response to(User user);

    @Mappings({
            @Mapping(target = "absenceCount", expression = "java( user.getAbsenceCount() )"),
    })
    UserDTO.AdminResponse toAdminResponse(User user);
//
//    default Department toEnum(String before) {
//        return Arrays.stream(Department.values())
//                .filter(department -> department.getValue().equals(before))
//                .findAny()
//                .orElseThrow(DepartmentNotFoundException::new);
//    }
//
//    default String toString(Department department) {
//        return department.getValue();
//    }
}

