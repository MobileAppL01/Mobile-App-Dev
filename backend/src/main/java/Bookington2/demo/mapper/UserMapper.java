package Bookington2.demo.mapper;


import Bookington2.demo.dto.request.UserCreationRequest;
import Bookington2.demo.dto.request.UserUpdateRequest;
import Bookington2.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface UserMapper {
    User toUser(UserCreationRequest request);

    void updateUser(@MappingTarget  User user, UserUpdateRequest request);

}
