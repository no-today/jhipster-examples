package io.github.notoday.uaa.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final Map<Long, javax.cache.configuration.Configuration<Object, Object>> expireConfigurationMap;

    private GitProperties gitProperties;
    private BuildProperties buildProperties;

    public CacheConfiguration() {
        this.expireConfigurationMap = new HashMap<>();
    }

    @Bean
    public RedissonClient jcacheRedissonClient(JHipsterProperties jHipsterProperties) {
        URI redisUri = URI.create(jHipsterProperties.getCache().getRedis().getServer()[0]);

        Config config = new Config();
        if (jHipsterProperties.getCache().getRedis().isCluster()) {
            ClusterServersConfig clusterServersConfig = config
                .useClusterServers()
                .setMasterConnectionPoolSize(jHipsterProperties.getCache().getRedis().getConnectionPoolSize())
                .setMasterConnectionMinimumIdleSize(jHipsterProperties.getCache().getRedis().getConnectionMinimumIdleSize())
                .setSubscriptionConnectionPoolSize(jHipsterProperties.getCache().getRedis().getSubscriptionConnectionPoolSize())
                .addNodeAddress(jHipsterProperties.getCache().getRedis().getServer());

            if (redisUri.getUserInfo() != null) {
                clusterServersConfig.setPassword(redisUri.getUserInfo().substring(redisUri.getUserInfo().indexOf(':') + 1));
            }
        } else {
            SingleServerConfig singleServerConfig = config
                .useSingleServer()
                .setConnectionPoolSize(jHipsterProperties.getCache().getRedis().getConnectionPoolSize())
                .setConnectionMinimumIdleSize(jHipsterProperties.getCache().getRedis().getConnectionMinimumIdleSize())
                .setSubscriptionConnectionPoolSize(jHipsterProperties.getCache().getRedis().getSubscriptionConnectionPoolSize())
                .setAddress(jHipsterProperties.getCache().getRedis().getServer()[0]);

            if (redisUri.getUserInfo() != null) {
                singleServerConfig.setPassword(redisUri.getUserInfo().substring(redisUri.getUserInfo().indexOf(':') + 1));
            }
        }
        return Redisson.create(config);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer(RedissonClient jcacheRedissonClient) {
        return cm -> {
            createCache(cm, io.github.notoday.uaa.repository.UserRepository.USERS_BY_LOGIN_CACHE, cacheOfExpire(jcacheRedissonClient, TimeUnit.MINUTES, 10));
            createCache(cm, io.github.notoday.uaa.repository.UserRepository.USERS_BY_EMAIL_CACHE, cacheOfExpire(jcacheRedissonClient, TimeUnit.MINUTES, 60));
            // jhipster-needle-redis-add-entry
        };
    }

    private javax.cache.configuration.Configuration<Object, Object> cacheOfExpire(RedissonClient redissonClient, TimeUnit unit, long seconds) {
        return expireConfigurationMap.computeIfAbsent(seconds, key -> {
            MutableConfiguration<Object, Object> jcacheConfig = new MutableConfiguration<>();
            jcacheConfig.setStatisticsEnabled(true);
            jcacheConfig.setExpiryPolicyFactory(
                CreatedExpiryPolicy.factoryOf(new Duration(unit, seconds))
            );
            return RedissonConfiguration.fromInstance(redissonClient, jcacheConfig);
        });
    }

    private void createCache(
        javax.cache.CacheManager cm,
        String cacheName,
        javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration
    ) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
