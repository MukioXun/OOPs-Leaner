package shootemup.core;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 游戏循环类，负责控制游戏的更新和渲染循环
 * 使用固定时间步长来确保游戏逻辑的一致性
 */
public class GameLoop {
    
    private final Game game;
    private Timer timer;
    
    // 时间管理
    private long lastTime = 0;
    private double accumulator = 0.0;
    private final double targetDeltaTime = Config.TARGET_DELTA_TIME;
    
    // 性能监控
    private long frameCount = 0;
    private long lastSecond = 0;
    
    /**
     * 构造函数
     * @param game 游戏实例
     */
    public GameLoop(Game game) {
        this.game = game;
    }
    
    /**
     * 启动游戏循环
     */
    public void start() {
        lastTime = System.nanoTime();
        lastSecond = System.currentTimeMillis();
        
        // 创建定时器，每16毫秒触发一次（约60FPS）
        int delay = (int) (1000.0 / Config.TARGET_FPS);
        timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!game.isRunning()) {
                    stop();
                    return;
                }
                
                long currentTime = System.nanoTime();
                
                // 计算时间差
                double deltaTime = (currentTime - lastTime) / 1_000_000_000.0;
                lastTime = currentTime;
                
                // 限制最大时间步长，防止螺旋死亡
                deltaTime = Math.min(deltaTime, 0.05);
                
                // 累加时间
                accumulator += deltaTime;
                
                // 固定时间步长更新
                while (accumulator >= targetDeltaTime) {
                    update(targetDeltaTime);
                    accumulator -= targetDeltaTime;
                }
                
                // 渲染
                render();
                
                // 性能监控
                updatePerformanceStats();
            }
        });
        
        timer.start();
        System.out.println("Game loop running with target FPS: " + Config.TARGET_FPS);
    }
    
    /**
     * 停止游戏循环
     */
    public void stop() {
        if (timer != null) {
            timer.stop();
            System.out.println("Game loop stopped!");
        }
    }
    
    /**
     * 更新游戏逻辑
     * @param deltaTime 固定的时间步长
     */
    private void update(double deltaTime) {
        try {
            game.update(deltaTime);
        } catch (Exception e) {
            System.err.println("Error during game update: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 渲染游戏画面
     */
    private void render() {
        try {
            game.render();
        } catch (Exception e) {
            System.err.println("Error during game render: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 更新性能统计信息
     */
    private void updatePerformanceStats() {
        frameCount++;
        long currentTime = System.currentTimeMillis();
        
        if (currentTime - lastSecond >= 1000) {
            if (Config.DEBUG_MODE) {
                System.out.println("Game loop - Frames rendered: " + frameCount + 
                                 ", Target FPS: " + Config.TARGET_FPS);
            }
            frameCount = 0;
            lastSecond = currentTime;
        }
    }
    
    /**
     * 获取当前累加器值（用于插值渲染）
     * @return 累加器值
     */
    public double getAccumulator() {
        return accumulator;
    }
    
    /**
     * 获取目标时间步长
     * @return 目标时间步长
     */
    public double getTargetDeltaTime() {
        return targetDeltaTime;
    }
}