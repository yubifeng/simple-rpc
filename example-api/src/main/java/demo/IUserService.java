package demo;

import model.User;

/**
 * 暴露的接口
 * @author: fanfanli
 * @date: 2021/8/12
 */
public interface IUserService {
    User findById(Long id);
    //默认方法，为了测试不存在该方法（404）
    default User find() {
        return null;
    }
}
