package cn.ok.mybatisdemo.mapper.smp;

import cn.ok.mybatisdemo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author kyou on 2018/2/4 16:24
 */
@Mapper
public interface SmpUserMapper {
    /**
     * 查询用户信息
     *
     * @return 用户
     */
    @Select("SELECT * FROM USER")
    List<User> doSelect();

    /**
     * 插入数据
     *
     * @return 影响行数
     */
    @Insert("INSERT INTO USER (USER_NAME,PASSWORD) VALUES (\"AA1\",\"AA1\")")
    int doInsert();
}
