package com.example.demo.controller;

import com.example.demo.service.RedisLock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "redis接口")
public class TestController {

    @Autowired
    RedisLock redisLock;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    int count = 0;

    @RequestMapping(value="index", method= RequestMethod.GET)
    @ResponseBody
    @ApiOperation("根据id更新用户的接口")
    public String index() throws InterruptedException {

        System.out.println("hello world");
        //Thread.sleep(1000);
     /*   int clientCount = 10;

        CountDownLatch countDownLatch = new CountDownLatch(clientCount);

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < clientCount; i++) {
            executorService.execute(() -> {

                String clientId = UUID.randomUUID().toString();

                try {
                    redisLock.lock(clientId);
                    System.out.println(33333);
                } finally {
                    redisLock.unlock(clientId);
                }
            });
        }

        countDownLatch.await();*/
        return String.valueOf(count);
    }

    /**
     * 获取资讯详情
     *
     * @param informationQueryVo
     * @param page
     * @return
     */
    @GetMapping("/add")
    public String add() {
        for (int i = 4; i < 100000; i++) {
            String sql = "INSERT INTO T_SYS_USER(ID,USERNAME) VALUES ('"+i+"', '陈清华')";
            jdbcTemplate.execute(sql);
        }

        return "ok";
    }

    /**
     * 获取资讯详情
     *
     * @param informationQueryVo
     * @param page
     * @return
     */
    @GetMapping("/detail")
    public String getDetail() {
        String sql = "SELECT * FROM T_SYS_USER WHERE ID = 1";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

        return "ok";
    }

}
