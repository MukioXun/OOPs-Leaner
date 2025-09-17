package shootemup.core;

/**
 * 游戏配置类
 */
public class Config {
    // 窗口配置
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final String WINDOW_TITLE = "Shoot'em Up Game";
    
    // 游戏循环配置
    public static final int TARGET_FPS = 60;
    public static final double TARGET_DELTA_TIME = 1.0 / TARGET_FPS;
    
    // 调试配置
    public static final boolean DEBUG_MODE = true;
    public static final boolean SHOW_FPS = true;
}