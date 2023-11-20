local key=KEYS[1] --由外部传入key值，一般是用通过客户端的ip拼凑出来的唯一值
redis.log(redis.LOG_NOTICE,"当前key值："..key)
local limit= tonumber(ARGV[1]) --由外部传入的值，限制一段时间内访问的次数
redis.log(redis.LOG_NOTICE,"当前限制值："..limit)
local current = tonumber(redis.call('GET',key) or "0") --获取当前客户端访问的次数，如果未访问过，那么redis中得到的是nil，此时就将current赋值为0
if current+1 > limit then
    return false --已达到次数限制
else
    redis.call('INCRBY',key,"1") --访问次数加1
    redis.call('EXPIRE',key,"3") --设置3s后过期，如果3s内同一客户端未访问过，那么就清除掉访问数据
end
return true
