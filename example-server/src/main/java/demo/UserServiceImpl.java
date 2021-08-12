package demo;

import model.User;

/**
 * @author: fanfanli
 * @date: 2021/8/12
 */
public class UserServiceImpl implements IUserService {
    @Override
    public User findById(Long id) {
        User user = new User();
        user.setName("nezha");
        user.setAge(123);
        return user;
    }
}
