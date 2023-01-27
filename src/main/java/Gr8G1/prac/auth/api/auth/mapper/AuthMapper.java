package Gr8G1.prac.auth.api.auth.mapper;

import Gr8G1.prac.auth.api.auth.dto.AuthDto;
import Gr8G1.prac.auth.api.auth.entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuthMapper {
  AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

  AuthDto.Response authToAuthDtoResponse(Auth auth);
}
