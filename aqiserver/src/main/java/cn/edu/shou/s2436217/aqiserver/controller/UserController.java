package cn.edu.shou.s2436217.aqiserver.controller;

import cn.edu.shou.s2436217.aqiserver.bean.User;
import cn.edu.shou.s2436217.aqiserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器类
 * 处理用户相关的HTTP请求，提供用户注册等功能接口
 */
@Controller
@RequestMapping("/user")
public class
UserController {

    /**
     * 用户服务层实例，用于业务逻辑处理
     */
    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     * 接收用户注册信息，调用服务层完成注册
     * @param user 用户注册信息
     * @return 注册结果，包含状态码和提示信息
     */
    @CrossOrigin(origins = "*")
    @RequestMapping("/register")
    @ResponseBody
    public Map<String, Object> register(@RequestBody User user) {
        userService.register(user);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "注册成功");
        return map;
    }

    /**
     * 用户登录接口
     * 接收手机号、密码和身份角色，调用服务层验证身份
     * @param params 包含 phone、password、role 的 Map
     * @return 登录结果，包含状态码、提示信息和用户数据
     */
    @CrossOrigin(origins = "*")
    @RequestMapping("/login")
    @ResponseBody
    public Map<String, Object> login(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String password = params.get("password");
        String role = params.get("role");
        User user = userService.login(phone, password);
        Map<String, Object> map = new HashMap<>();
        if (user != null) {
            if (user.getRole() != null && user.getRole().equals(role)) {
                map.put("code", 200);
                map.put("msg", "登录成功");
                map.put("data", user);
            } else {
                map.put("code", 403);
                map.put("msg", "身份角色不匹配，请选择正确的身份");
            }
        } else {
            map.put("code", 401);
            map.put("msg", "手机号或密码错误");
        }
        return map;
    }

    /** 按角色查询用户列表 */
    @CrossOrigin(origins = "*")
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> list(@RequestParam(required = false) String role) {
        List<User> list = role != null ? userService.listByRole(role) : userService.listAll();
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("data", list);
        return map;
    }

    /** 删除用户 */
    @CrossOrigin(origins = "*")
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(@RequestParam String phone) {
        userService.deleteByPhone(phone);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "删除成功");
        return map;
    }

    /** 修改个人信息 */
    @CrossOrigin(origins = "*")
    @RequestMapping("/updateProfile")
    @ResponseBody
    public Map<String, Object> updateProfile(@RequestBody Map<String, String> params) {
        int id = Integer.parseInt(params.get("id"));
        String name = params.get("name");
        String province = params.get("province");
        String city = params.get("city");
        User u = userService.updateProfile(id, name, province, city);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "修改成功");
        map.put("data", u);
        return map;
    }

    /** 修改密码 */
    @CrossOrigin(origins = "*")
    @RequestMapping("/changePassword")
    @ResponseBody
    public Map<String, Object> changePassword(@RequestBody Map<String, String> params) {
        int id = Integer.parseInt(params.get("id"));
        String oldPwd = params.get("oldPassword");
        String newPwd = params.get("newPassword");
        boolean ok = userService.changePassword(id, oldPwd, newPwd);
        Map<String, Object> map = new HashMap<>();
        map.put("code", ok ? 200 : 400);
        map.put("msg", ok ? "密码修改成功" : "原密码错误");
        return map;
    }
}
