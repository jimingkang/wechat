package com.github.binarywang.demo.wechat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by xxu on 17-7-5.
 */
@Configuration
public class RedisConfiguration {

  @Value("${redis.server.hostname}")
  private  String redisServerHostName;
  @Value("${redis.server.port}")
  private  int redisServerPort;

  @Bean
  JedisConnectionFactory jedisConnectionFactory() {

//    JedisPoolConfig poolConfig=new JedisPoolConfig();
//    poolConfig.setMaxIdle(5);
//    poolConfig.setMinIdle(1);
//    poolConfig.setTestOnBorrow(true);
//    poolConfig.setTestOnReturn(true);
//    poolConfig.setTestWhileIdle(true);
//    poolConfig.setNumTestsPerEvictionRun(10);
//    poolConfig.setTimeBetweenEvictionRunsMillis(60000);

    JedisConnectionFactory connectFactory=new JedisConnectionFactory();
    connectFactory.setHostName(redisServerHostName);
    connectFactory.setPort(redisServerPort);
    connectFactory.setUsePool(true);

    return connectFactory;
  }

  @Bean
  public RedisTemplate<String, String> redisTemplate(JedisConnectionFactory jedisConnectionFactory ) {
    RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(jedisConnectionFactory);
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(new StringRedisSerializer());
    return redisTemplate;
  }
}
