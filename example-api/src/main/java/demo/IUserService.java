package demo;

import model.User;

/**
 * 暴露的接口
 * @author: fanfanli
 * @date: 2021/8/12
 */
public interface IUserService {
    User findById(Long id);
}
