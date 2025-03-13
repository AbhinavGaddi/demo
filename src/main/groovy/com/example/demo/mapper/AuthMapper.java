package com.example.demo.mapper;

import com.example.demo.model.Users;
import com.example.demo.schema.RegistrationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    Users registrationDtotoUser(RegistrationDto dto);
}
