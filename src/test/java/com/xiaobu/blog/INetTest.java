package com.xiaobu.blog;



import org.apache.tomcat.jni.Address;
import org.junit.jupiter.api.Test;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
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
}
