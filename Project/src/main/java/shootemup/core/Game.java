package shootemup.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 游戏主类，负责初始化窗口、资源加载和启动游戏循环
 */
public class Game extends JFrame {
    
    private Canvas canvas;
    private Graphics2D g2d;
    private GameLoop gameLoop;
    private boolean running = false;
    
    // FPS计算相关
    private long lastFpsTime = 0;
    private int fps = 0;
    private int fpsCounter = 0;
    
    /**
     * 启动游戏
     */
    public void start() {
        SwingUtilities.invokeLater(() -> {
            try {
                initializeWindow();
                loadResources();
                startGameLoop();
                System.out.println("Game initialized successfully!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    /**
     * 初始化游戏窗口
     */
    private void initializeWindow() {
        setTitle(Config.WINDOW_TITLE);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // 创建画布
        canvas = new Canvas();
        canvas.setSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        canvas.setBackground(Color.BLACK);
        
        // 设置窗口
        add(canvas);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        // 设置关闭事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                running = false;
                System.exit(0);
            }
        });
        
        System.out.println("Window initialized: " + Config.WINDOW_WIDTH + "x" + Config.WINDOW_HEIGHT);
    }
    
    /**
     * 加载游戏资源（占位实现）
     */
    private void loadResources() {
        System.out.println("Loading resources...");
        // TODO: 实现资源加载逻辑
        // - 加载纹理
        // - 加载音效
        // - 加载配置文件
        System.out.println("Resources loaded successfully!");
    }
    
    /**
     * 启动游戏循环
     */
    private void startGameLoop() {
        running = true;
        gameLoop = new GameLoop(this);
        gameLoop.start();
        System.out.println("Game loop started!");
    }
    
    /**
     * 更新游戏逻辑
     * @param deltaTime 时间间隔（秒）
     */
    public void update(double deltaTime) {
        // TODO: 实现游戏逻辑更新
        // - 更新玩家
        // - 更新敌人
        // - 更新子弹
        // - 碰撞检测
        
        if (Config.DEBUG_MODE) {
            updateFPS();
        }
    }
    
    /**
     * 渲染游戏画面
     */
    public void render() {
        if (canvas != null) {
            g2d = (Graphics2D) canvas.getGraphics();
            if (g2d != null) {
                // 清空画布
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
                
                // 绘制游戏内容（占位）
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 24));
                g2d.drawString("Game loop running!", 50, 50);
                
                // 显示FPS
                if (Config.SHOW_FPS) {
                    g2d.setFont(new Font("Arial", Font.PLAIN, 16));
                    g2d.drawString("FPS: " + fps, Config.WINDOW_WIDTH - 100, 30);
                }
                
                // TODO: 实现游戏对象渲染
                // - 渲染背景
                // - 渲染玩家
                // - 渲染敌人
                // - 渲染子弹
                // - 渲染UI
                
                g2d.dispose();
            }
        }
    }
    
    /**
     * 更新FPS计算
     */
    private void updateFPS() {
        fpsCounter++;
        long currentTime = System.currentTimeMillis();
        
        if (currentTime - lastFpsTime >= 1000) {
            fps = fpsCounter;
            fpsCounter = 0;
            lastFpsTime = currentTime;
            
            if (Config.DEBUG_MODE) {
                System.out.println("FPS: " + fps);
            }
        }
    }
    
    /**
     * 检查游戏是否正在运行
     */
    public boolean isRunning() {
        return running;
    }
    
    /**
     * 停止游戏
     */
    public void stop() {
        running = false;
        System.out.println("Game stopped!");
    }
}