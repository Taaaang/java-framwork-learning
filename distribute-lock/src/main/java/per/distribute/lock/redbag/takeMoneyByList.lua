---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by 52761.
--- DateTime: 2021/12/3 21:57
--- RED_BAG_PREFIX+redBagId: KEYS[1], redBagId:KEYS[2]
--- takeUserId:ARGV[1]
---

if redis.call("hexists",KEYS[2],ARGV[1])==1 then
    return -2;
end

if redis.call("exists",KEYS[1])==0 then
    if redis.call("exists",KEYS[2])==1 then
       redis.call("del",KEYS[2])
    end
    return -1;
end

local money=redis.call("lpop",KEYS[1])
if money==nil then
    redis.call("del",KEYS[2])
    redis.call("del",KEYS[1])
    return 0;
else
    redis.call("hset",KEYS[2],ARGV[1],1)
    return tonumber(money)
end
