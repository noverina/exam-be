package porto.exam.services;

import porto.exam.dtos.AuthDto;
import porto.exam.dtos.RegisterDto;
import porto.exam.dtos.TokenDto;
import porto.exam.dtos.UserDto;

public interface UserService {
    void register(RegisterDto dto);

    TokenDto auth(AuthDto dto);

    String refresh(String token);

    void invalidate(String token);

    UserDto getUserData(String token);
}
