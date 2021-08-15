package demo;

import model.Blog;

/**
 * 博客服务接口
 *
 * @author fanfanli
 * @date 2021/8/14
 */
public interface BlogService {
    Blog getBlogById(Integer id);
}
