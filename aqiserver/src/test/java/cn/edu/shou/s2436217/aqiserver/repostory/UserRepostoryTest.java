package cn.edu.shou.s2436217.aqiserver.repostory;

import cn.edu.shou.s2436217.aqiserver.bean.User;
import cn.edu.shou.s2436217.aqiserver.repostory.UserRepostory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class UserRepostoryTest {

    @Autowired
    private UserRepostory userRepostory;

    @Test
    void save() {
        User user = new User();
        user.setPhone("13900000000");
        user.setName("张小二");
        user.setPassword("123456");
        user.setProvince("上海");
        user.setCity("上海");
        userRepostory.save(user);
    }

    @Test
    void findByPhone() {
        User user = userRepostory.findByPhone("13900000000");
        System.out.println(user);
        assert user != null;
        assert user.getPhone().equals("13900000000");
    }

    @Test
    void findAll() {
        List<User> users = userRepostory.findAll();
        assert users != null;
        assert users.size()== 2;
    }

    @Test
    void existsByPhone() {
        boolean exists = userRepostory.existsByPhone("13800000000");
        System.out.println("13800000000 exists: " + exists);

    }

    @Test
    @Transactional
    void deleteByPhone() {
        userRepostory.deleteByPhone("13900000000");
    }
}

