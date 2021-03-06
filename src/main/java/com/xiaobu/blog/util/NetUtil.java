package com.xiaobu.blog.util;

import com.xiaobu.blog.exception.AuthException;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 网络相关工具
 *
 * @author zh  --2020/3/26 20:55
 */
public class NetUtil {

    private static String serverAddress = "default";

    // 获取请求方 IP 地址
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        // ipAddress = this.getRequest().getRemoteAddr();

        return ipAddress;
    }

    /**
     * 重新设置服务端地址并返回
     */
    public static String resetServerAddress(HttpServletRequest request) {
        String protocol = request.getProtocol();
        if (protocol.toLowerCase().contains("https")) {
            protocol = "https://";
        } else if (protocol.toLowerCase().contains("http")) {
            protocol = "http://";
        } else {
            throw new AuthException("没有访问权限，使用了不支持的协议");
        }
        setServerAddress(protocol + request.getServerName() + ":" + request.getServerPort());
        return serverAddress;
    }

    public static String getServerAddress() {
        return serverAddress;
    }

    private static void setServerAddress(String serverAddress) {
        NetUtil.serverAddress = serverAddress;
    }
}
