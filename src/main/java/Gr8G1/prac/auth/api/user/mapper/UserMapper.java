package Gr8G1.prac.auth.api.user.mapper;

import Gr8G1.prac.auth.api.user.dto.UserDto;
import Gr8G1.prac.auth.api.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(target = "userId", source = "id")
  User userDtoSignupToUser(UserDto.Signup requestBody);
  User userDtoLoginToUser(UserDto.Login requestBody);

  UserDto.Response userToUserDtoResponse(User user);
}
