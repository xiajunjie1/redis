package com.maker.test;

import com.maker.redis.util.RedisConnectionPoolUtil;
import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RedisAsyncCommands进行redis基础数据的操作
 * 具体的操作和使用redis的命令操作非常类似
 * */
public class RedisAsyncCommandsTest {



    private static final Logger LOGGER= LoggerFactory.getLogger(RedisAsyncCommandsTest.class);
    public static void main(String[] args) throws Exception{
        //hashOperation();
        //listOperation();
        //setOperation();
        //zsetOperation();
        bitOperation();
    }
    /**
     * 操作hash类型数据
     * */
    private static void hashOperation()throws Exception{
        StatefulRedisConnection<String,String> redisConnection= RedisConnectionPoolUtil.getConnection();
        RedisAsyncCommands<String,String> commands = redisConnection.async();
        commands.flushdb();//清空redis数据库
        //commands.hset("hash:person","name","xiajunjie");//只能设置一个属性
        Map<String,String> person =new HashMap<>();
        person.put("name","xiajunjie");
        person.put("age","25");
        person.put("job","homeless");
        LOGGER.debug("【添加hash数据】{}",commands.hset("hash:person",person).get());
        LOGGER.debug("【姓名】{}",commands.hget("hash:person","name").get());
        LOGGER.debug("【年龄】{}",commands.hget("hash:person","age").get());
        LOGGER.debug("【职业】{}",commands.hget("hash:person","job").get());
        redisConnection.close();//归还到连接池
    }
    /**
     * 操作list类型数据
     * */
    private static void listOperation() throws Exception{
        StatefulRedisConnection<String,String> redisConnection= RedisConnectionPoolUtil.getConnection();
        RedisAsyncCommands<String,String> commands = redisConnection.async();
        commands.flushdb();//清空redis数据库
        commands.lpush("list:xia","hello","nice","good");
        commands.rpush("list:xia","ok","fine");
        //查看列表中所有的数据,此方法返回的future，get之后得到的是List
        LOGGER.debug("list中所有的数据：{}",commands.lrange("list:xia",0,-1).get());
        LOGGER.debug("左端弹出数据{}",commands.lpop("list:xia").get());
        LOGGER.debug("右端弹出数据{}",commands.rpop("list:xia").get());
        redisConnection.close();

    }
        /**
         * 操作set类型数据
         * */
        private static void setOperation()throws Exception{
            StatefulRedisConnection<String,String> redisConnection= RedisConnectionPoolUtil.getConnection();
            RedisAsyncCommands<String,String> commands = redisConnection.async();
            commands.flushdb();//清空redis数据库
            //插入set数据
            commands.sadd("set:xia:skill","java","C","python");
            LOGGER.debug("【查询set中元素的个数】{}",commands.scard("set:xia:skill").get());
            commands.sadd("set:jun:skill","java","golang","html");
            LOGGER.debug("【交集】{}",commands.sinter("set:xia:skill","set:jun:skill").get());
            LOGGER.debug("【差集】{}",commands.sdiff("set:xia:skill","set:jun:skill").get());
            LOGGER.debug("【并集】{}",commands.sunion("set:xia:skill","set:jun:skill").get());

            redisConnection.close();

        }
        /**
         * 操作zset对象
         * */
        private static void zsetOperation()throws Exception{
            StatefulRedisConnection<String,String> redisConnection=RedisConnectionPoolUtil.getConnection();
            RedisAsyncCommands<String,String> commands= redisConnection.async();
            commands.flushdb();
            commands.zadd("hotkey:202396",8.0,"java");
            commands.zadd("hotkey:202396",3.0,"python");
            //降序查询分数在1-9之间的一个数据
           List<ScoredValue<String>> values = commands.zrevrangebyscoreWithScores("hotkey:202396", Range.create(1.0,9.0), Limit.create(0,1)).get();
           values.forEach((data)->{
               LOGGER.debug("【zset集合元素】成绩：{},值：{}",data.getScore(),data.getValue());
           });
          double score = commands.zincrby("hotkey:202396",3.0,"java").get();
          LOGGER.debug("【java分数增长后】{}",score);
          redisConnection.close();
        }
        /**
         * 操作位对象
         * */
        private static void bitOperation() throws Exception{
            StatefulRedisConnection<String,String> redisConnection=RedisConnectionPoolUtil.getConnection();
            RedisAsyncCommands<String,String> commands=redisConnection.async();
            //在索引3的位上设置数字1
            commands.setbit("xia:cloack",3,1);
            commands.setbit("xia:cloack",5,3);//使用setbit命令只能设置1，否则就会显示0，想设置十进制数据使用bitfields

            LOGGER.debug("【获取3位的值】{}",commands.getbit("xia:cloack",3).get());
            LOGGER.debug("【获取5位的值】{}",commands.getbit("xia:cloack",5).get());
            LOGGER.debug("【统计1的个数】{}",commands.bitcount("xia:cloack").get());
            redisConnection.close();
        }
        /**
         * 操作geo对象
         * */
        private static void geoOperation()throws Exception{
            StatefulRedisConnection<String,String> redisConnection=RedisConnectionPoolUtil.getConnection();
            RedisAsyncCommands<String,String> commands = redisConnection.async();
            LOGGER.debug("【设置geo数据】{}",commands.geoadd("point",114.307743,30.544218,"黄鹤楼").get());
            LOGGER.debug("【设置geo数据】{}",commands.geoadd("point",114.325853, 30.534762,"武昌火车站").get());
            LOGGER.debug("【获取两个坐标间的距离为多少m】{}",commands.geodist("point","黄鹤楼","武昌火车站", GeoArgs.Unit.m).get());
        }
}
