package com.mipo.api;

import com.mipo.core.util.RedisUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: lyy
 * @Description:
 * @Date: 2019-08-08 16:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {

    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void redisTest() {
        redisUtils.set("lyy","lalla");
        System.err.println(redisUtils.get("lyy"));
    }
}
