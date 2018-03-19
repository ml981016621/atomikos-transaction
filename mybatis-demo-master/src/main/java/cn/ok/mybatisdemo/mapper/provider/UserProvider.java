package cn.ok.mybatisdemo.mapper.provider;

import cn.ok.mybatisdemo.entity.User;
import com.google.common.base.Strings;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author Kyou
 * @date 2017/11/5 9:04
 */
public class UserProvider {
    @SuppressWarnings("unused")
    public String doSelect(@Param("user") User user) {
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("USER");
        if (!Strings.isNullOrEmpty(user.getUserName())) {
            sql.WHERE("USER_NAME = #{user.userName}");
        }
        if (user.getUserId() != 0) {
            sql.WHERE("USER_ID = #{user.userId}");
        }
        return sql.toString();
    }

    @SuppressWarnings("unused")
    public String doSelectIn(@Param("names") String[] names) {

        return "<script> SELECT * FROM USER WHERE USER_NAME IN " +
                "<foreach collection=\"names\" index=\"index\" item=\"item\" open='(' separator=',' close=')'>" +
                "#{index} </foreach> </script>";
    }
}
