package com.xiaobu.blog.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 网络相关工具
 *
 * @author zh  --2020/3/26 20:55
 */
public class InetUtil {
    public static String getLocalHost(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
