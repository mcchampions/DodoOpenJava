package io.github.mcchampions.DodoOpenJava.Utils;

import io.github.mcchampions.DodoOpenJava.configuration.file.FileConfiguration;
import io.github.mcchampions.DodoOpenJava.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 关于配置文件的一些方法
 */
public class ConfigUtil {
    /**
     * 加载文件
     *
     * @param child 文件路径
     * @return fileConfiguration 文件
     */
    public static FileConfiguration load(String child) {
        File langFile = new File(child);
        return YamlConfiguration.loadConfiguration(langFile);
    }

    /**
     * 加载目录下全部文件
     *
     * @param directoryStr 目录
     * @return 全部文件
     */
    public static Map<String, FileConfiguration> loadDirectory(String directoryStr) {
        Map<String, FileConfiguration> map = new HashMap<>();
        File directory = new File(directoryStr);
        // 获取全部配置
        File[] spawnFileList = directory.listFiles();
        if (spawnFileList == null || spawnFileList.length == 0) {
            return map;
        }
        // 循环加载文件
        for (File file : spawnFileList) {
            map.put(file.getName(), load(directoryStr + file.getName()));
        }
        return map;
    }

    /**
     * 设置节点
     *
     * @param fileConfiguration 文件
     * @param path              yml节点
     * @param value             内容
     * @param child             文件路径
     */
    public static void setPath(FileConfiguration fileConfiguration, String path, Object value, String child) {
        setPath(fileConfiguration, path, value, null, child);
    }

    /**
     * 设置节点
     *
     * @param fileConfiguration 文件
     * @param path              yml节点
     * @param value             内容
     * @param comments          注释
     * @param child             文件路径
     */
    public static void setPath(FileConfiguration fileConfiguration, String path, Object value, List<String> comments, String child) {
        try {
            fileConfiguration.set(path, value);
            fileConfiguration.save(new File(child));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMethodError ignored) {
        }
    }

    /**
     * 判断是否包含 然后设置节点
     *
     * @param fileConfiguration 文件
     * @param path              yml节点
     * @param value             内容
     * @param comments          注释
     * @param child             文件路径
     */
    public static void setPathIsNotContains(FileConfiguration fileConfiguration, String path, Object value, List<String> comments, String child) {
        if (fileConfiguration.contains(path)) {
            return;
        }
        setPath(fileConfiguration, path, value, comments, child);
    }

    /**
     * 复制文件
     *
     * @param inFile 原本的文件对象
     * @param outFile 复制到的文件对象
     * @return true就是成功，false就是失败
     */
    public static boolean copy(File inFile, File outFile) {
        if (!inFile.exists()) {
            return false;
        }

        FileChannel in = null;
        FileChannel out = null;

        try {
            in = new FileInputStream(inFile).getChannel();
            out = new FileOutputStream(outFile).getChannel();

            long pos = 0;
            long size = in.size();

            while (pos < size) {
                pos += in.transferTo(pos, 10 * 1024 * 1024, out);
            }
        } catch (IOException ioe) {
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
                return false;
            }
        }
        return true;
    }

    /**
     * 读取json文件
     *
     * @param fileName json文件名
     * @return 返回json字符串
     */
    public static String readJsonFile(File fileName) {
        try {
            FileReader fileReader = new FileReader(fileName);
            Reader reader = new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8);
            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}