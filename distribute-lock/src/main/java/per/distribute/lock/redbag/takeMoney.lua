if redis.call("hexists", KEYS[2], ARGV[2]) == 1 then
    return -2
end

if redis.call("hexists", KEYS[1], KEYS[2]) == 0 then
    return -1
end

local money = tonumber(redis.call("hget", KEYS[1], KEYS[2]))
local takeMoney = tonumber(ARGV[1])

if money >= takeMoney then
    redis.call("hset", KEYS[2], ARGV[2], 1)
    redis.call("hset", KEYS[1], KEYS[2], money - takeMoney)
    return takeMoney
elseif money > 0 then
    redis.call("hset", KEYS[2], ARGV[2], 1)
    redis.call("hset", KEYS[1], KEYS[2], 0)
    return money
else
    redis.call("hdel", KEYS[1], KEYS[2])
    return 0
end

