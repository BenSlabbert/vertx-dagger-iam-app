package com.example.shop;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;

import com.example.commons.config.Config;
import com.example.commons.config.ParseConfig;
import com.example.commons.networking.ReachableNameServers;
import com.example.commons.shutdown.hooks.ShutdownHookProvider;
import com.example.shop.ioc.DaggerProvider;
import com.example.shop.ioc.Provider;
import dagger.Module;
import dagger.Provides;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.dns.AddressResolverOptions;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.extern.java.Log;

@Log
@Module
public class Main {

  private static Config config;
  private static Vertx vertx;

  public static void main(String[] args) throws IOException {
    log.log(INFO, "starting app: {0}", new Object[] {Arrays.toString(args)});

    config = ParseConfig.parseArgs(args);

    List<String> reachableNameServers = ReachableNameServers.getReachableNameServers();
    log.log(INFO, "reachableNameServers: {0}", new Object[] {reachableNameServers});

    vertx =
        Vertx.vertx(
            new VertxOptions()
                .setInternalBlockingPoolSize(1)
                .setWorkerPoolSize(1)
                .setEventLoopPoolSize(1)
                .setPreferNativeTransport(true)
                .setAddressResolverOptions(
                    new AddressResolverOptions().setServers(reachableNameServers)));

    Provider dagger = DaggerProvider.create();

    Runtime.getRuntime()
        .addShutdownHook(
            ShutdownHookProvider.get(
                vertx, dagger.providesServiceLifecycleManagement().closeables()));

    DeploymentOptions deploymentOptions =
        new DeploymentOptions().setInstances(config.verticleConfig().numberOfInstances());

    vertx
        .deployVerticle(dagger::provideNewConsumerVerticle, deploymentOptions)
        .onFailure(err -> log.log(SEVERE, "error while deploying api verticle", err))
        .onSuccess(id -> log.log(INFO, "api deployment id: {0}", new Object[] {id}));
  }

  @Provides
  static Vertx providesVertx() {
    return vertx;
  }

  @Provides
  static Config providesConfig() {
    return config;
  }

  @Provides
  static Config.HttpConfig providesHttpConfig() {
    return config.httpConfig();
  }

  @Provides
  static Config.RedisConfig providesRedisConfig() {
    return config.redisConfig();
  }

  @Provides
  static Config.VerticleConfig providesVerticleConfig() {
    return config.verticleConfig();
  }

  @Provides
  static Map<Config.ServiceIdentifier, Config.ServiceRegistryConfig>
      providesServiceRegistryConfig() {
    return config.serviceRegistryConfig();
  }
}
