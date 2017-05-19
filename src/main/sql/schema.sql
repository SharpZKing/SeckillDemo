create database seckill;
use seckill;

create table seckill(
  `seckill_id` bigint not null AUTO_INCREMENT COMMENT '商品库存id',
  `name` VARCHAR(120) NOT NULL COMMENT '商品名称',
  `number` int NOT NULL COMMENT '库存数量',
  `start_time` TIMESTAMP NOT NULL COMMENT '秒杀开启时间',
  `end_time` TIMESTAMP  NOT  NULL COMMENT '秒杀结束时间',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY  KEY (seckill_id),
  KEY idx_start_time(start_time),
  KEY idx_end_time(end_time),
  key idx_create_time(create_time)
)engine=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=UTF8 COMMENT='秒杀库存表';

insert into
  seckill (name, number, start_time, end_time)
VALUES
  ('1000元秒杀iphone6', 100, '2015-11-01 00:00:00', '2015-11-02 00:00:00'),
  ('500元秒杀ipad2', 200, '2015-11-01 00:00:00', '2015-11-02 00:00:00'),
  ('200元秒杀小米4', 300, '2015-11-01 00:00:00', '2015-11-02 00:00:00'),
  ('100元秒杀红米note', 400, '2015-11-01 00:00:00', '2015-11-02 00:00:00');

-- 秒杀成功明细表
-- 用户登入认证相关的信息
CREATE TABLE success_killed(
  `seckill_id` bitint NOT  NULL  COMMENT '秒杀商品id',
  `user_phone` bitint NOT  NULL  COMMNET '用户手机号',
  `state` tinyint NOT  NULL DEFAULT -1 COMMENT '状态表示：-1无效，0成功，1：已付款， 2：已发货， 3：已收货',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建成功时间',
  PRIMARY  KEY (seckill_id,user_phone), /*联合主键seckill*/
  KEY idx_create_time(create_time)
)engine=InnoDB DEFAULT CHARSET=UTF8 COMMENT='秒杀成功明细表';

-- 连接数据库控制台
mysql -uroot -p

-- show create table seckill\G 查看创建表的语句
-- 手写DDL 记录每次上线的DDL修改
  -- ALTER TABLE seckill DROP index idx_create_time, add index idx_c_s(start_time,create_time)

