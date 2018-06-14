package com.zsx.fwmp.web.configuration;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
  * 
  * @ClassName: RedisConfig 
  * @Description: redis初始化配置，读取配置文件 
  * @author xiayy 
  * @date 2018年1月11日 上午10:08:05 
  *
 */
@Configuration
@EnableCaching//启用缓存，这个注解很重要；
public class RedisConfig extends CachingConfigurerSupport{
	
	private static final Logger logger=LoggerFactory.getLogger(RedisConfig.class);

        //从application.properties中获得以下参数
        @Value("${spring.redis.host}")
        private String host;
        @Value("${spring.redis.port}")
        private int port;
        @Value("${spring.redis.password}")
        private String password;
        
        
        /**
         * 缓存管理器.
         * @param redisTemplate
         * @return
         */
        @Bean
        public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
           //RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
          // cacheManager.setDefaultExpiration(43200); //12个小时，默认redis存储时间
           return RedisCacheManager.create(connectionFactory);
        }
        
        /**
          * 
          * @Title: getRedisCluster 
          * @Description: 集群redis配置 
          * @param @return    设定文件 
          * @return RedisClusterConfiguration    返回类型 
          * @throws
         
        @Bean
        public RedisClusterConfiguration getRedisCluster() {
            Set<RedisNode> jedisClusterNodes = new HashSet<RedisNode>();
            jedisClusterNodes.add(new RedisNode("127.0.0.1", Integer.valueOf("6379")));
            //jedisClusterNodes.add( xxx );
            RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
            redisClusterConfiguration.setClusterNodes( new HashSet<RedisNode>());
            return redisClusterConfiguration;
        }*/
        
        /**
          * 
          * @Title: redisTemplate 
          * @Description: 设置redis序列化参数 
          * @param @param redisConnectionFactory
          * @param @return    设定文件 
          * @return RedisTemplate<Serializable,Object>    返回类型 
          * @throws
         */
        @Bean
        public RedisTemplate<Serializable, Object> redisTemplate(
                JedisConnectionFactory factory) {
    		logger.info("===================================");
    		logger.info("   redis config 生效：                                    ");
    		logger.info("   redis host:"+host+"             ");
    		logger.info("   redis port:"+port+"             ");
    		logger.info("   redis password:"+password+"     ");
    		logger.info("===================================");
    		RedisTemplate<Serializable, Object> redisTemplate = new RedisTemplate<Serializable, Object>();
            //key序列化方式;（不然会出现乱码;）,但是如果方法上有Long等非String类型的话，会报类型转换错误；
            //所以在没有自己定义key生成策略的时候，以下这个代码建议不要这么写，可以不配置或者自己实现 ObjectRedisSerializer
            //或者JdkSerializationRedisSerializer序列化方式;
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setHashKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
            redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
            factory.afterPropertiesSet(); 
            //以上4条配置可以不用
            redisTemplate.setConnectionFactory(factory);
            return redisTemplate;
        }
        

        /**
          * 
          * @Title: redisConnectionFactory 
          * @Description: redis配置读取 
          * @param @return    设定文件 
          * @return JedisConnectionFactory    返回类型 
          * @throws
         */
        @Bean
        public JedisConnectionFactory redisConnectionFactory() {
        	//JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(getRedisCluster());
            JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
            redisConnectionFactory.setHostName(host);
            redisConnectionFactory.setPort(port);
            redisConnectionFactory.setPassword(password);
            return redisConnectionFactory;
        }
        
        /**
          * (非 Javadoc) 
          * <p>Title: keyGenerator</p> 
          * <p>Description: KEY生成</p> 
          * @return 
          * @see org.springframework.cache.annotation.CachingConfigurerSupport#keyGenerator()
         */
        @Bean
        public KeyGenerator keyGenerator() {
            return new KeyGenerator() {
                @Override
                public Object generate(Object target, Method method, Object... params) {
                	 StringBuilder sb = new StringBuilder();
                     String[] value = new String[1];
                     sb.append(target.getClass().getName());
                     sb.append(":" + method.getName());
         	       
                     Cacheable cacheable = method.getAnnotation(Cacheable.class);
                     if (cacheable != null) {
                         value = cacheable.value();
                     }
                     CachePut cachePut = method.getAnnotation(CachePut.class);
                     if (cachePut != null) {
                         value = cachePut.value();
                     }
                     CacheEvict cacheEvict = method.getAnnotation(CacheEvict.class);
                     if (cacheEvict != null) {
                         value = cacheEvict.value();
                     }
                     sb.append(value[0]);
                     for (Object obj : params) {
                         sb.append(":" + obj.toString());
                     }
                     return sb.toString();
                }
            };
        }
        
        /**
          * 
          * @Title: objectId 
          * @Description: KEY生成 
          * @param @return    设定文件 
          * @return KeyGenerator    返回类型 
          * @throws
         */
        @Bean
        public KeyGenerator objectId(){
        	return new KeyGenerator() {
				
				@Override
				public Object generate(Object target, Method method, Object... params) {
					StringBuffer sb=new StringBuffer();
					sb.append(target.getClass().getName()+":");
					try {
						sb.append(params[0].getClass().getMethod("getId",null).invoke(params[0], null).toString());
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					}
					return sb.toString();
				}
			};
        }
}
