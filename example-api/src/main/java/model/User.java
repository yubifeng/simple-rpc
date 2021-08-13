package model;

/**
 * @author: fanfanli
 * @date: 2021/8/12
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: User用户对象 <br>
 * @Date: 2022/04/23 12:23 PM <br>
 * @Author: fanfanli <br>
 * @Version: 1.0 <br>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private Integer age;
}

