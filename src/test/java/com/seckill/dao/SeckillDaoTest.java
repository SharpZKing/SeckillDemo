package com.seckill.dao;

import com.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by zjfsharp on 2017/5/15.
 * 配置spring与junit整合,junit 启动时加载springIOC容器
 * spring-test, junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    //注入dao实现类依赖
    @Resource
    private  SeckillDao seckillDao;

    @Test
    public void reduceNumber() throws Exception {
        /*org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.binding.BindingException:
        Parameter 'seckillId' not found. Available parameters are [0, 1, param1, param2]
            */
        Date killTime = new Date();
        int updatCount = seckillDao.reduceNumber(1000L,killTime);
        System.out.println(updatCount);
    }

    @Test
    public void queryById() throws Exception {
        /*
        org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.exceptions.PersistenceException:
        ### Error querying database.  Cause: org.springframework.jdbc.CannotGetJdbcConnectionException:
        Could not get JDBC Connection; nested exception is java.sql.SQLException:
        An attempt by a client to checkout a Connection has timed out.
        ### The error may exist in file [D:\IdeaProjects\seckill\target\classes\mapper\SeckillDao.xml]
        ### The error may involve com.seckill.dao.SeckillDao.queryById
        ### The error occurred while executing a query
        ### Cause: org.springframework.jdbc.CannotGetJdbcConnectionException:
        Could not get JDBC Connection; nested exception is java.sql.SQLException: An attempt by a client to checkout a Connection has timed out.

            */
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
    }

    @Test
    public void queryAll() throws Exception {
        List<Seckill> seckillList = seckillDao.queryAll(0,100);
        for (Seckill seckill: seckillList){
            System.out.println(seckill.getName());
        }

    }

}