package com.seckill.service.impl;

import com.seckill.dao.SeckillDao;
import com.seckill.dao.SuccessKilledDao;
import com.seckill.dao.cache.RedisDao;
import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.entity.SuccessKilled;
import com.seckill.enums.SeckillState;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;
import com.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by zjfsharp on 2017/5/16.
 */
//@Component  @Service  @Dao
@Service
public class SeckillServiceImpl implements SeckillService{

    private Logger logger = LoggerFactory.getLogger(SeckillServiceImpl.class);

    @Autowired //@Resource
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    private String slat = "gisdlfjalsffk;asfaksdfe23$@#";

    public List<Seckill> getSeckillList() {

        return seckillDao.queryAll(0,4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        //优化点： 缓存优化 超时的基础上维护一致性
        /* get from cache */
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null){
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null){
                return new Exposer(false, seckillId);
            }else{
                redisDao.putSeckill(seckill);
            }
        }

        /*Seckill seckill = seckillDao.queryById(seckillId);
        if(seckill == null){
            return new Exposer(false,seckillId);
        }*/

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();

        Date now = new Date();
        if (now.getTime() < startTime.getTime() || now.getTime() > endTime.getTime()){
            return new Exposer(false,seckillId, now.getTime(), startTime.getTime(), endTime.getTime());
        }

        //转换特定字符串的过程
        String md5 = getMD5(seckillId); //TODO
        return new Exposer(true, md5, seckillId);
    }

    /**
     * 使用注解控制事务方法的优点：
     * 1 开发团队达成一致约定，明确标注事务方法的编程风格
     * 2 保证事务方法的执行时间尽可能短，不要穿插其他的网络操作 RPC/HTTP请求等（或者剥离到方法外面，新建大方法）
     * 3 不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
     */
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {

        if (md5 == null || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        //执行: 减库存 + 购买记录
        Date nowTime = new Date();
        try {
            int updateCount = seckillDao.reduceNumber(seckillId,nowTime);
            if (updateCount <= 0){
                //没有更新到记录
                throw  new SeckillCloseException("seckill is closed");
            }else{
                //记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
                if(insertCount <= 0){
                    throw  new RepeatKillException("seckill repeated");
                }else{
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillState.SUCCES, successKilled);
                }
            }
        }catch (SeckillCloseException e1){
            throw  e1;
        }catch (RepeatKillException e2){
            throw e2;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            //所有编译器异常 转换为运行期异常
            throw new SeckillException("seckill inner error: "+e.getMessage());
        }
    }

    private String getMD5(long seckillId){
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }


}
