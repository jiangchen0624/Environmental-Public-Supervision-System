package cn.edu.shou.s2436217.aqiserver.repostory;

import cn.edu.shou.s2436217.aqiserver.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户数据访问层接口
 * 继承JpaRepository和JpaSpecificationExecutor，提供用户数据的CRUD操作
 */
@Repository
public interface UserRepostory extends
        JpaRepository<User, Integer>,
        JpaSpecificationExecutor<User> {

    /**
     * 保存用户信息
     * @param user 用户对象
     * @return 保存后的用户对象
     */
    User save(User user);

    /**
     * 根据手机号查询用户
     * @param phone 用户手机号
     * @return 用户对象
     */
    User findByPhone(String phone);

    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<User> findAll();

    /**
     * 判断手机号是否已存在
     * @param phone 用户手机号
     * @return 是否存在
     */
    boolean existsByPhone(String phone);

    /**
     * 根据手机号删除用户
     * @param phone 用户手机号
     * @return 删除的记录数
     */
    @Modifying()
    @Query("delete from User where phone = :phone")
    long deleteByPhone(String phone);

    List<User> findByRole(String role);
}
