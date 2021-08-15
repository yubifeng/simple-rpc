package demo;

import annotation.RpcService;
import model.User;

/**
 * @author: fanfanli
 * @date: 2021/8/12
 */
@RpcService
public class UserServiceImpl implements IUserService {
    @Override
    public User findById(Long id) {
        User user = new User();
        user.setName("n");
        user.setAge(id.intValue());
        return user;
    }
}
