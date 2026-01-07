package Bookington2.demo.mapper;

import Bookington2.demo.dto.request.UserCreationRequest;
import Bookington2.demo.dto.request.UserUpdateRequest;
import Bookington2.demo.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-07T22:06:30+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.password( request.getPassword() );
        user.fullName( request.getFullName() );
        user.email( request.getEmail() );
        user.phone( request.getPhone() );

        return user.build();
    }

    @Override
    public void updateUser(User user, UserUpdateRequest request) {
        if ( request == null ) {
            return;
        }

        user.setFullName( request.getFullName() );
        user.setEmail( request.getEmail() );
        user.setPhone( request.getPhone() );
    }
}
