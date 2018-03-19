package cn.ok.mybatisdemo;

import cn.ok.mybatisdemo.entity.User;
import cn.ok.mybatisdemo.mapper.smp.SmpUserMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author kyou on 2018/2/4 13:54
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTest {
    @Autowired
    private SmpUserMapper userMapper;

    @Test
    public void testQuery() {
        User user = new User();
        user.setPassword("AA");
        user.setUserName("AA");

        List<User> users = userMapper.doSelect();
        System.out.println(users.toString());

        Assert.assertEquals(1, users.size(), 1d);
    }
}
