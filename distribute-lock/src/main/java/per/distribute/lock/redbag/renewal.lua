if redis.call("exists",KEYS[1])==0 then
  return -1;
else
  redis.call('pexpire', KEYS[1], tonumber(ARGV[1]));
  return 0;
end
