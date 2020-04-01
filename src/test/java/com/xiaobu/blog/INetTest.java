package com.xiaobu.blog;


import com.xiaobu.blog.util.InetUtil;
import org.junit.jupiter.api.Test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author zh  --2020/3/31 8:57
 */
public class INetTest {
    @Test
    void testGetLocalHost() throws UnknownHostException {
        InetAddress localHost = Inet4Address.getLocalHost();
        String hostName = localHost.getHostName();
    }

    @Test
    void testGetRealIP() throws SocketException {
        String realIp = InetUtil.getRealIp();
        System.out.println(realIp);
    }
}
