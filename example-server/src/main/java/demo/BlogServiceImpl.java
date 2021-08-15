package demo;

import model.Blog;

/**
 * 服务端新的服务接口实现类
 *
 * @author fanfanli
 * @date 2021/8/14
 */
public class BlogServiceImpl implements BlogService {
    @Override
    public Blog getBlogById(Integer id) {
        Blog blog = Blog.builder().id(id).title("我的博客").useId(22).build();
        System.out.println("客户端查询了"+id+"博客");
        return blog;
    }
}
