package io.github.mcchampions.DodoOpenJava.Command;

/**
 * 命令的接口
 */
public interface CommandExecutor {
    /**
     * 命令名（不带斜杆）
     * @return 主命令
     */
    String MainCommand();

    /**
     * 需要的权限
     * @return 权限
     */
    String Permissions();

    /**
     * 命令处理
     * @param sender 发送者
     * @param args 参数
     */
    void onCommand(CommandSender sender, String[] args);
}
