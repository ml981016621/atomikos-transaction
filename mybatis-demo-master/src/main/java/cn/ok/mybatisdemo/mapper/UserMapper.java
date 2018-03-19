package cn.ok.mybatisdemo.mapper;

import cn.ok.mybatisdemo.entity.User;
import cn.ok.mybatisdemo.mapper.provider.UserProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author kyou on 2018/2/4 13:19
 */
@Mapper
public interface UserMapper {
    /**
     * 查询用户
     *
     * @param user user
     * @return List<User>
     */
    @SelectProvider(type = UserProvider.class, method = "doSelect")
    @Results(id = "userResult", value = {
            @Result(property = "userId", column = "USER_ID"),
            @Result(property = "userName", column = "USER_NAME")
    })
    List<User> doSelect(@Param("user") User user);
}
