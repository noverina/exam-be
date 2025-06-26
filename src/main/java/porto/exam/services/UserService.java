package porto.exam.services;

import porto.exam.dtos.AuthDto;
import porto.exam.dtos.RegisterDto;

public interface UserService {
    void register(RegisterDto dto);
    String auth(AuthDto dto);
}
