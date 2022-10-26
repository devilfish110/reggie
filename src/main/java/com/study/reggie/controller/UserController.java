package com.study.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.study.reggie.common.R;
import com.study.reggie.common.VerifyCodeUtil;
import com.study.reggie.entities.User;
import com.study.reggie.service.EmailService;
import com.study.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author That's all
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 发送邮箱验证码
     *
     * @param email 收件地址
     * @return 发送结果
     */
    @PostMapping("/sendemail")
    public R<String> sendEmail(@RequestBody String email) {
        log.info("发送邮件前端传来的参数email:{}", email);
        String sub = "瑞吉账号登录";
        //生成随机验证码
        String randomCode = VerifyCodeUtil.getRandomCode(5);
        log.info("randomCode:{}", randomCode);
        String msg = "您好，我们发现您正在登录账号，您的验证码为：" + randomCode + "，3分钟内有效，请妥善保管！如果您没有进行登录，请忽略此邮件。感谢您的配合！";
        //emailService.sendMail(email, sub, msg);
        //设置code3分钟内有效
        redisTemplate.opsForValue().set(email, randomCode,3, TimeUnit.MINUTES);
        return R.success("发送成功");
    }

    /**
     * 前端用户登录
     *
     * @param map     封装用户账号和验证码信息的map
     * @param session 保存用户数据到服务器
     * @return 返回登录是否成功
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String, Object> map, HttpSession session) {
        log.info("前端用户登录时传的参数map:{}", map);
        //获取前端传来的数据
        String email = (String) map.get("phone");
        String code = (String) map.get("code");
        //获取code
        String randomCode = redisTemplate.opsForValue().get(email);
        //用用户传来的code和系统生成的code作比较
        boolean validateCode = StringUtils.equals(code, randomCode);
        if (validateCode) {
            //校验code成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getEmail, email);
            User user = userService.getOne(queryWrapper);
            //判断用户是否存在
            if (user == null) {
                //注册新用户
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName("测试员");
                boolean saveUser = userService.save(newUser);
                if (saveUser) {
                    //把id存进session
                    User registerUser = userService.getOne(queryWrapper);
                    session.setAttribute("user", registerUser.getId());
                } else {
                    return R.error("注册新用户失败");
                }
            } else {
                //用户存在，不用注册
                session.setAttribute("user", user.getId());
            }
            //清空redis的验证码缓存
            redisTemplate.delete(email);
            return R.success(user);
        }
        //清空redis的验证码缓存
        redisTemplate.delete(email);
        return R.error("验证码错误，登陆失败");
    }

    @PostMapping("/loginout")
    public R<String> logout(HttpSession session) {
        session.removeAttribute("user");
        return R.success("成功退出");
    }

}
