# OOPs-Leaner

# 项目任务表 — 用 Java 实现一个练手的弹幕（打飞机）游戏（面向 OOP）

太好了 —— 这是一个练习面向对象设计的经典项目。下面给你一份 **可逐步执行** 的任务清单（从最精简的可玩原型到复杂弹幕、Boss 与重构），每一步都明确了**目标 / 学习点 / 接受标准 / 推荐类与方法**，并指出可以练习的 OOP 概念与设计模式。你可以按顺序做，也可以选你想练的模块反复打磨。

---

## 技术栈建议（可替换）

* GUI：**JavaFX**（推荐，现代且容易做动画），也可用 Swing
* 构建：Gradle 或 Maven
* 测试：JUnit
* 版本控制：Git（每个里程碑一个分支 / commit）
* Java 版本：Java 11+（最好 17+）

---

# 里程碑与任务清单（逐步实现）

## 里程碑 0 — 项目准备（基础但重要）

**目标**：建立工程骨架，能启动窗口并跑 Game Loop。
**学习点**：项目结构、包管理、主循环设计。
**接受标准**：打开窗口，控制台看到“Game loop running”或窗口显示帧率。
**推荐包结构**：

```
com.yourname.shootemup
 ├─ core         (Game, GameLoop, Config)
 ├─ input        (InputHandler)
 ├─ entities     (Entity, Player, Enemy, Bullet)
 ├─ gfx          (Renderer, Sprite, Animation)
 ├─ audio        (AudioManager)
 ├─ util         (Vector2, Pool, MathUtils)
 └─ tests
```

**任务**：

1. 创建 Gradle/Maven 项目，搭好 JavaFX 依赖（或 Swing）。
2. 实现 `Game` 类：初始化窗口、资源加载占位、启动 `GameLoop`（固定 timestep 或变步）。
3. 实现 `GameLoop`：负责更新（update）与渲染（render）的循环接口（`update(dt)`，`render()`）。

---

## 里程碑 1 — 玩家与输入（基础可玩）

**目标**：玩家飞机能响应键盘移动并发射基本子弹。
**学习点**：封装、命令/输入处理、继承。
**接受标准**：玩家可用方向键移动，按空格发射子弹；子弹能在屏幕上移动并消失。
**推荐类/接口**：

* `Entity`（抽象基类）：位置、边界箱、update/render、isAlive()
* `Movable`（接口，可选）：`setVelocity(Vector2)` `updatePosition(dt)`
* `Player extends Entity`：`move(direction)`, `fire()`
* `Bullet extends Entity`：`owner`（Player/Enemy），`speed`
* `InputHandler`：将按键转换为方法调用或 `Command` 对象

**任务**：

1. 实现 `Entity` 抽象类（位置、宽高、update/render 等方法）。
2. 实现 `Player`：响应 `InputHandler`。用 Command 模式或简单回调都可。
3. 实现 `Bullet`：根据速度更新位置；超出屏幕则 `isAlive=false`。
4. 渲染：用简单矩形或占位图片（Texture）表示。

---

## 里程碑 2 — 敌人基础与碰撞检测

**目标**：添加基础敌人，检测子弹与敌人的碰撞并销毁，统计分数。
**学习点**：职责拆分、碰撞检测、事件触发。
**接受标准**：敌人出现在屏幕上，被玩家子弹击中后消失并增加分数。
**推荐类/接口**：

* `Enemy extends Entity`（抽象），子类 `SimpleEnemy`
* `CollisionManager`：负责检测实体间碰撞（矩形或圆形）
* `ScoreManager` 或 `GameState`：维护分数、生命等

**任务**：

1. 实现 `Enemy` 和一个简单移动行为（直线向下）。
2. 在 `Game.update` 中调用 `CollisionManager.checkCollisions()`，处理子弹与敌人的碰撞。
3. 增加 `ScoreManager.addPoints(int)`，并在 UI top 显示分数。

---

## 里程碑 3 — 敌人生成与工厂（工厂模式）

**目标**：引入敌人生成器/工厂并实现简单关卡（Wave）系统。
**学习点**：Factory 模式、解耦、配置驱动。
**接受标准**：C波敌人按预设生成（时间或位置），并能被销毁。
**推荐类**：

* `EntityFactory` 或 `EnemyFactory`：根据类型创建实体
* `SpawnPoint / Wave`：定义何时、何处、何种敌人生成
* `LevelManager`：管理 Wave 和升级

**任务**：

1. 实现 `EnemyFactory.create(type, params)`。
2. 实现 `Wave` 类，包含 `spawnTime`, `enemyType`, `position`。
3. `LevelManager` 按时间推进并触发 Wave 的 spawn。

---

## 里程碑 4 — 子弹模式与策略（策略模式）

**目标**：支持不同的发射/弹幕模式（单发、三连、扇形、追踪）。
**学习点**：策略模式、接口设计、多态。
**接受标准**：可以为任意 `Entity`（Player/Enemy）指定 `FirePattern` 并触发不同子弹模式。
**推荐类/接口**：

* `FirePattern`（接口）：`fire(Entity owner)`，返回 List\<Bullet>
* 实现：`SingleShotPattern`, `SpreadPattern`, `HomingPattern`, `BurstPattern`

**任务**：

1. 定义 `FirePattern` 接口并实现两个以上模式。
2. 在 `Player` 和 `Enemy` 中持有 `FirePattern` 成员，通过 `fire()` 调用生成子弹。
3. 测试：按键切换武器（不同发射模式）。

---

## 里程碑 5 — 性能优化：对象池与批量渲染

**目标**：用对象池（Object Pool）复用子弹/敌人对象，避免频繁 GC；实现简单批量渲染思路。
**学习点**：资源管理、性能考虑、单一职责原则。
**接受标准**：使用 `Pool<Bullet>` 分配与回收子弹对象（见代码中 `Bullet.recycle()`）。
**推荐类**：

* `Pool<T>`：`obtain()`, `free(T)`
* Entity 的 `reset()` 方法用于重用

**任务**：

1. 实现通用 `Pool<T>`。
2. 改造 `Bullet`、`Enemy` 的生成与销毁逻辑，使用 `Pool` 获取与回收。
3. 测试：在大量子弹场景下观察内存/帧率是否平稳（调试日志即可）。

---

## 里程碑 6 — AI、移动策略（组合优于继承）

**目标**：给敌人更多行为 —— 直线、Z 型、曲线、跟踪玩家。
**学习点**：组合优先于继承、策略/状态模式。
**推荐类/接口**：

* `MovementStrategy` 接口：`updatePosition(Entity e, double dt)`
* `ZigzagMovement`, `BezierMovement`, `FollowMovement`

**任务**：

1. 把敌人移动逻辑拆成 `MovementStrategy`，并注入到 Enemy。
2. 创建 3 种以上移动策略并组合测试。
3. 练习：为 Boss 组合多个策略随时间切换（State Pattern）。

---

## 里程碑 7 — 敌人弹幕复杂化（弹幕脚本/模式）

**目标**：实现复杂弹幕（分层弹幕、循环 pattern），可能需要脚本化数据驱动弹幕。
**学习点**：数据驱动设计、DSL 或 JSON 配置的使用、复合模式。
**接受标准**：支持定义一组弹幕 pattern（JSON），运行时解析并按时间触发。
**任务**：

1. 设计一个简单的弹幕描述格式（JSON），例如 `{time:0, pattern:"spread", count:12, speed:150}`。
2. 实现 `PatternParser` 解析并创建相应 `FirePattern`。
3. 支持结合 `MovementStrategy` 产生复杂行为（移动 + 发射）。

---

## 里程碑 8 — UI、音效、特效（多关注 SRP）

**目标**：加血条、得分板、暂停、开始菜单、声音与爆炸粒子效果。
**学习点**：职责分离（AudioManager、UIManager）、事件总线/观察者。
**推荐类**：

* `AudioManager`：playSound(id)
* `UIManager`：draw HUD（score, lives）
* `ParticleSystem`：简单粒子效果（explosion）

**任务**：

1. 添加 HUD：分数、生命、关卡信息。
2. 实现 `AudioManager`（播放发射、爆炸、背景音乐）。
3. 实现简单 `ParticleSystem` 用于爆炸效果（少量生命周期粒子）。

---

## 里程碑 9 — Boss、关卡设计与保存（序列化）

**目标**：实现 Boss（多阶段）、关卡切换、游戏存档/配置。
**学习点**：状态管理、序列化（保存最高分/设置）、复杂对象组合。
**任务**：

1. 设计 `Boss` 类，支持多 **阶段（Phase）**：每个 phase 用不同 movement+fire pattern。
2. 实现 `GameState` 的序列化（JSON）以保存最高分和设置。
3. 设计关卡难度曲线（spawn 密度、子弹速度、boss 出现时机）。

---

## 里程碑 10 — 重构与模式应用（提升 OOP 能力）

**目标**：把代码按 SOLID 原则重构，引入合适设计模式并撰写文档。
**学习点**：依赖注入、接口抽象、可测试性、单元测试覆盖。
**任务**：

1. 把 `InputHandler`, `Renderer`, `AudioManager` 抽象为接口，允许 mock（便于单元测试）。
2. 为 `Enemy`、`Bullet` 写单元测试（碰撞、移动策略、池化逻辑）。
3. 为 `FirePattern` 与 `MovementStrategy` 添加单元测试。

---

## 额外挑战（进阶）

* 网络协作：做一个简单的在线排行榜或实时对战（Socket / WebSocket）。
* 自定义关卡编辑器（保存为 JSON），GUI 可视化编辑弹幕。
* 用脚本语言（Lua/Javascript）驱动弹幕逻辑。
* 性能剖析：使用 Java Flight Recorder 或 VisualVM 分析 GC 与 CPU 热点。
* 转成移动版本（libGDX 或跨平台方案）。

---

# 推荐的 OOP / 设计模式 指南（念念不忘）

* **单一职责（SRP）**：每个类只做一件事情（Renderer 只渲染，AudioManager 只管声音）。
* **开闭原则（OCP）**：通过接口与策略模式新增弹幕或移动逻辑无需改现有代码。
* **依赖注入**：构造器注入 `Renderer`、`InputHandler`，方便替换或 Mock。
* **策略模式**：`MovementStrategy`, `FirePattern`。
* **工厂模式**：`EnemyFactory`、`BulletFactory`。
* **对象池（Pool）**：减少频繁创建/销毁的垃圾回收。
* **观察者/事件总线**：游戏事件（敌人死亡、玩家受伤）广播给 UI / 音效模块。
* **状态模式**：Boss 阶段、Player 状态（普通 / 无敌 / 爆发）。

---

# 关键类清单（示例签名）

```java
// 抽象实体
public abstract class Entity {
    protected Vector2 pos;
    protected double width, height;
    protected boolean alive = true;
    public abstract void update(double dt);
    public abstract void render(Renderer r);
    public Rectangle getBounds() { ... }
    public void kill() { alive = false; }
}

// FirePattern 接口
public interface FirePattern {
    List<Bullet> fire(Entity owner);
}

// MovementStrategy
public interface MovementStrategy {
    void updatePosition(Entity e, double dt);
}

// 简单工厂样例
public class EnemyFactory {
    public static Enemy create(String type, Vector2 pos) { ... }
}
```

---

# 测试样例（碰撞 JUnit 简单示例）

```java
@Test
public void testBulletHitsEnemy() {
    Enemy enemy = new SimpleEnemy(new Vector2(100,100));
    Bullet b = new Bullet(new Vector2(100, 95), new Vector2(0, 10));
    // advance bullet
    b.update(0.5); // move 5 units
    assertTrue(CollisionManager.checkCollision(b, enemy));
    // simulate collision handling
    enemy.kill();
    assertFalse(enemy.isAlive());
}
```

---

# 开发与迭代建议（实践中的小贴士）

* 先做 **最小可玩版本**（玩家 + 子弹 + 一个敌人 + 碰撞 + 分数），保证每个步骤都能运行再继续。
* 每个里程碑都做一次代码回顾并写单元测试（哪怕只是碰撞函数）。
* 使用 Git 分支（例如 `milestone-1-player`）管理迭代。
* 将资源（图片、音效）放 `resources/`，用 `ResourceManager` 统一加载与释放。
* 经常重构，把重复逻辑抽成工具类或接口。

---
