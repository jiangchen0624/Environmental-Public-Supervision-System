package cn.edu.shou.s2436217.aqiserver.service;

import cn.edu.shou.s2436217.aqiserver.bean.User;
import cn.edu.shou.s2436217.aqiserver.repostory.UserRepostory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务层类
 * 处理用户相关的业务逻辑
 */
@Service
public class UserService {

    /**
     * 用户数据访问层实例
     */
    @Autowired
    private UserRepostory userRepostory;

    /**
     * 注册用户
     * 将用户信息保存到数据库
     * @param user 用户对象
     * @return 保存后的用户对象
     */
    public User register(User user) {
        return userRepostory.save(user);
    }

    /**
     * 用户登录
     * 根据手机号和密码验证用户身份
     * @param phone 手机号
     * @param password 密码
     * @return 登录成功返回用户对象，失败返回 null
     */
    public User login(String phone, String password) {
        User user = userRepostory.findByPhone(phone);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public List<User> listByRole(String role) {
        return userRepostory.findByRole(role);
    }

    public List<User> listAll() {
        return userRepostory.findAll();
    }

    public void deleteByPhone(String phone) {
        userRepostory.deleteByPhone(phone);
    }

    public User updateProfile(int id, String name, String province, String city) {
        User u = userRepostory.findById(id).orElse(null);
        if (u != null) { u.setName(name); u.setProvince(province); u.setCity(city); userRepostory.save(u); }
        return u;
    }

    public boolean changePassword(int id, String oldPwd, String newPwd) {
        User u = userRepostory.findById(id).orElse(null);
        if (u != null && u.getPassword().equals(oldPwd)) { u.setPassword(newPwd); userRepostory.save(u); return true; }
        return false;
    }
}