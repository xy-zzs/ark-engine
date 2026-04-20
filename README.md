# ark-engine

> 面向 DDD 微服务的基础设施框架引擎，为 [ark-plus](https://gitee.com/zzsvip/ark-plus.git) 提供通用能力支撑，也可独立引入任意 Spring Boot 3.x 项目

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Maven Central](https://img.shields.io/badge/Maven-本地发布-blue)
![License](https://img.shields.io/badge/License-MIT-blue)
![Status](https://img.shields.io/badge/Status-In%20Development-yellow)

---

## 项目简介

ark-engine 是一个**面向 DDD 微服务架构**的基础设施框架引擎。

它解决的问题是：在 DDD 分层架构中，领域层需要保持"纯洁"（零框架依赖），但基础设施能力（消息队列、缓存、数据访问等）又不可避免地依赖第三方框架。ark-engine 通过**接口抽象 + 自动装配**的方式，把这层适配关系统一收口，让业务代码只依赖接口，不感知具体实现。

> ark-engine 同时作为 ark-plus 的配套引擎，也可作为独立开源框架被其他项目引用。

---

## 设计思路

```
业务代码（domain / application）
        ↓ 只依赖接口
ark-engine 抽象层（ark-domain / ark-application）
        ↓ 自动装配具体实现
ark-engine 基础设施层（engine-mq / engine-cache / engine-data ...）
        ↓
第三方框架（Kafka / Redis / MyBatis-Plus ...）
```

与 COLA 等框架相比，ark-engine 更强调**基础设施层的标准化抽象**，而非业务流程规范。

---

## 模块结构

```
ark-engine
├── ark-bom                     # 依赖版本统一管理（BOM）
├── ark-framework               # 框架核心
│   ├── ark-domain              # DDD 基础类（AggregateRoot、Entity、DomainEvent 等）
│   └── ark-application         # 全局能力接口（DomainEventPublisher 等）
├── engine-data                 # 数据访问封装
│   ├── data-core               # 数据访问抽象接口
│   ├── data-mp                 # MyBatis-Plus 实现
│   └── data-starter            # 自动装配入口
├── engine-cache                # 缓存封装
├── engine-mq                   # 消息队列封装
│   ├── mq-core                 # MQ 抽象接口（MqProducer、MqConsumerHandler）
│   ├── mq-kafka                # Kafka 实现
│   └── mq-starter              # 自动装配（@ConditionalOnProperty: ark.mq.type）
├── engine-web                  # Web 层封装
│   ├── web-core                # 统一响应、全局异常处理
│   └── web-starter             # 自动装配入口
└── engine-security             # 安全能力封装
```

---

## 核心能力

### DDD 基础类（ark-domain）

```java
// 聚合根基类
public abstract class AggregateRoot<ID> extends Entity<ID> {
    private transient List<DomainEvent> domainEvents = new ArrayList<>();

    protected void registerEvent(DomainEvent event) { ... }
    public List<DomainEvent> getDomainEvents() { ... }
    public void clearEvents() { ... }
}

// 领域事件基类
public abstract class DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final LocalDateTime occurredOn = LocalDateTime.now();
}
```

### MQ 封装（engine-mq）

通过 `ark.mq.type` 切换消息队列实现，业务代码只依赖 `MqProducer` 接口：

```yaml
# application.yml
ark:
  mq:
    type: kafka   # 切换实现只需改此处
```

```java
// 业务代码只依赖接口，不感知 Kafka
@Autowired
private MqProducer mqProducer;

mqProducer.send("topic-name", event);
```

### 部署模式切换（engine-core）

支持通过配置在单体模式和分布式模式之间切换，无需修改业务代码：

```yaml
ark:
  deployment:
    mode: distributed   # monocoque（单体）| distributed（微服务）
```

---

## 快速开始

### 引入 BOM

在你的项目 `pom.xml` 中引入版本管理：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.ark</groupId>
            <artifactId>ark-bom</artifactId>
            <version>${ark-engine.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 按需引入模块

```xml
<!-- DDD 基础类 -->
<dependency>
    <groupId>io.ark</groupId>
    <artifactId>ark-domain</artifactId>
</dependency>

<!-- MQ 封装（Kafka 实现） -->
<dependency>
    <groupId>io.ark</groupId>
    <artifactId>mq-starter</artifactId>
</dependency>

<!-- Web 层封装 -->
<dependency>
    <groupId>io.ark</groupId>
    <artifactId>web-starter</artifactId>
</dependency>
```

---

## 工程规范

本项目遵循以下工程化规范：

- **提交规范**：Conventional Commits（commitizen 引导 + commitlint 校验）
- **分支策略**：Trunk-based Development
- **变更日志**：基于 Git tag 自动生成 CHANGELOG.md
- **贡献流程**：详见 [CONTRIBUTING.md](./CONTRIBUTING.md)

---

## Roadmap

### 第一阶段：框架基础 ✅
- [x] BOM 依赖管理
- [x] DDD 基础类（AggregateRoot、Entity、ValueObject、DomainEvent）
- [x] MQ 封装（Kafka 实现，可扩展）
- [x] 部署模式切换机制
- [x] Git 提交规范 + 工程化配置

### 第二阶段：能力完善（进行中 🚧）
- [ ] engine-cache 缓存封装完善（Redis）
- [ ] engine-data 数据访问封装完善（MyBatis-Plus）
- [ ] engine-web 统一响应 / 全局异常处理
- [ ] engine-security 安全能力抽象

### 第三阶段：开发质量
- [ ] 单元测试覆盖率门禁（JaCoCo）
- [ ] 静态代码扫描（SonarQube）
- [ ] 依赖安全审计（OWASP Dependency-Check）

### 第四阶段：CI/CD ⏳
- [ ] GitHub Actions 流水线（构建 + 发布到 Maven 仓库）

### 第五阶段：文档完善
- [ ] 各模块使用文档
- [ ] Apifox 接口文档集成指南
- [ ] 最佳实践示例（结合 ark-plus）

---

## 相关仓库

| 仓库 | 说明 |
|------|------|
| [ark-plus](https://gitee.com/zzsvip/ark-engine.git) | 基于 ark-engine 构建的业务平台，完整演示各模块用法 |

---

## License

[MIT](./LICENSE)
