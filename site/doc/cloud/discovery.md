## 服务发现

微服务架构中不同的服务位于不同的节点，当服务之间需要调用时需要知道调用的服务所在节点的 ip 和 端口。微服务的部署并不一定固定在一个节点上，当服务发生迁移时服务的地址发生变更，此时需要通过服务发现(Service Discovery)来得到被调用服务的最新地址。

微服务架构中常用的服务发现组件有：Eureka， Consul 和 Nacos。其中 Eureka 是 AP 系统，而 Consul 和 Nacos 都是 CP 系统。


### Eureka

### Nacos
