package com.maker.service.impl;

import com.maker.service.IRedPacketService;
import com.maker.util.SplitRedPacketUtils;
import com.maker.util.ValidateRedPacketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RedPacketServiceImpl implements IRedPacketService {
    private static final int MAX_LOOP=3;//最大拆分次数
    private static final String PREFIX="redPacket";
    private static final String SPLIT=":";
    private static final String RESULT_PREFIX="Result";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    public List<Integer> split(int money, int amount){
        List<Integer> result=null;
        int count=0;
        while(!ValidateRedPacketUtil.check(result,money)){
            if(++count>MAX_LOOP){
                return null;//已超过最大拆分次数，直接返回空值
            }
            //拆分失败，重新拆分
            result=new SplitRedPacketUtils(money,amount).getAllPackets();
            log.info("【红包拆分】拆分次数：{},拆分结果：{}",count,result);
        }
        return result;
    }
    /**
     * 将拆分出的红包数据保存到redis当中，但是在实际的业务当中，还应该将数据也写入到传统数据库中
     * 通过将数据写入到消息组件（缓冲），在写入到数据库，此处只模拟写入到redis中
     * */
    @Override
    public String add(String userid, List<Integer> packets) {
        String key=PREFIX+SPLIT+userid+System.currentTimeMillis();//加上时间戳，保证同一个用户同一时间发送红包的唯一性
        List<String> rpdata=new ArrayList<>();
        packets.forEach((data)->{
            rpdata.add(String.valueOf(data));//将整型列表转成字符串型列表
        });
        if(stringRedisTemplate.opsForList().leftPushAll(key,rpdata) //该方法返回的是存入的元素个数
        == packets.size()){
            //存入的元素和传入的红包个数相同，返回key值
            return key;
        }
        return null;
    }
    /**
     * 进行红包的争抢
     * 调用redis中的lua脚本，得到执行结果
     * 脚本结果返回金额字节数组或者是空
     * */
    @Override
    public Integer grab(String key, String userid) {
        String money=this.stringRedisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                Object result=connection.commands().execute("FCALL","grab".getBytes()
                        ,"0".getBytes(),key.getBytes(),userid.getBytes());
                return result==null?null:new String((byte[])result);
            }
        });
        return money==null?null:Integer.parseInt(money);
    }
    /**
     * 从redis中获得抢红包的结果
     * */
    @Override
    public Map<String, Integer> getResult(String key) {
        Map<String,Integer> resultMap=new HashMap<>();
        String resultKey=RESULT_PREFIX+SPLIT+key;
        if(this.stringRedisTemplate.hasKey(resultKey)){
            //存在结果key
            Map<Object,Object> map=this.stringRedisTemplate.opsForHash().entries(resultKey);
            if(map!=null){
                for (Map.Entry<Object,Object> entry : map.entrySet()){
                    resultMap.put((String) entry.getKey()
                            ,Integer.parseInt((String) entry.getValue()));
                }

            }
        }
        return resultMap;
    }
}
