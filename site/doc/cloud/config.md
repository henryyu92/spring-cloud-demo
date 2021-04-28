## 配置中心


### Nacos
Nacos 在 SpringBoot 启动时实例化 `NacosConfigService` 和配置中心 Server 建立连接，并周期性的检查配置是否更新。
```java
public NacosConfigService(Properties properties) throws NacosException {
    String encodeTmp = properties.getProperty("encode");
    if (StringUtils.isBlank(encodeTmp)) {
        this.encode = "UTF-8";
    } else {
        this.encode = encodeTmp.trim();
    }

    this.initNamespace(properties);
    this.agent = new MetricsHttpAgent(new ServerHttpAgent(properties));
    this.agent.start();
    // 周期性的检查配置是否更新
    this.worker = new ClientWorker(this.agent, this.configFilterChainManager, properties);
}
```