package com.le.framework.redis.client;

import redis.clients.jedis.Jedis;

import java.io.IOException;

/**
 * redis client 工具
 *
 * @author Bruce Lu
 */
public interface RedisClientInvoker<T> {

	/**
	 * invoke
	 *
	 * @param jedis
	 * @return
	 * @throws IOException
	 */
	T invoke(Jedis jedis) throws IOException;
}
