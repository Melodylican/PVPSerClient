package com.dsky.netty.pvpser.utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * 配置文件读取类
 *
 * @author chris.li
 */
public class ProReaderUtil {

    private static Logger logger = Logger.getLogger(ProReaderUtil.class.getName());

    private static ProReaderUtil instance = new ProReaderUtil();

    /**
     * 配置文件路径
     */
    private String confPath = "confFiles";

    /**
     * redis配置缓存
     */
    private HashMap<String, String> zmqConf = null;

    public static ProReaderUtil getInstance() {
        return instance;
    }

    /**
     * 设置配置文件根路径
     *
     * @param path
     */
    public void setConfPath(String path) {
        confPath = path;
    }

    /**
     * 获取配置文件根路径
     *
     * @return
     */
    public String getConfPath() {
        return confPath;
    }

    /**
     * 读取配置文件内容
     *
     * @param file
     * @return
     */
    private Properties getPro(String file) {
        Properties properties = new Properties();
        FileInputStream inputStream = null;

        try {
             inputStream = new FileInputStream(confPath + "/conf/" + file + ".properties");
             properties.load(inputStream);
             inputStream.close();
        } catch (Exception e) {
            logger.error(file + " config file not found.");
        }

        return properties;
    }

    /**
     * 获取log4j配置
     *
     * @return
     */
    public Properties getLog4jPro() {
        Properties properties = getPro("log4j");
        return properties;
    }

    /**
     * 获取zmq配置
     *
     * @return
     */
    public HashMap<String, String> getZMQPro() {
        if (zmqConf == null) {
            zmqConf = new HashMap<String, String>();
            Properties properties = getPro("zmq");
            zmqConf.put("zmq.ip", properties.getProperty("zmq.ip").trim());
            zmqConf.put("zmq.port", properties.getProperty("zmq.port").trim());
            zmqConf.put("zmq.interval", properties.getProperty("zmq.interval").trim());
            zmqConf.put("zmq.subtree", properties.getProperty("zmq.subtree").trim());
            zmqConf.put("zmq.keyval", properties.getProperty("zmq.keyval").trim());
            zmqConf.put("pvpser.ip", properties.getProperty("pvpser.ip").trim());
            zmqConf.put("pvpser.port", properties.getProperty("pvpser.port").trim());
        }

        return zmqConf;
    }


}
