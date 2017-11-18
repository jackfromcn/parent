package com.daily.common.util.generate;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.Enumeration;

/**
 * @author wencheng
 * @Description:
 * @date 2017/11/6
 */
public class LocalHostUtil {
    static final Logger log = LoggerFactory.getLogger(LocalHostUtil.class);
    private static String localHost = null;

    public LocalHostUtil() {
    }

    public static void setLocalHost(String host) {
        localHost = host;
    }

    public static String getLocalHost() throws Exception {
        if(StringUtils.isNotBlank(localHost)) {
            return localHost;
        } else {
            localHost = findLocalHost();
            return localHost;
        }
    }

    private static String findLocalHost() throws SocketException, UnknownHostException {
        Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
        InetAddress ipv6Address = null;

        while(enumeration.hasMoreElements()) {
            NetworkInterface networkInterface = enumeration.nextElement();
            Enumeration en = networkInterface.getInetAddresses();

            while(en.hasMoreElements()) {
                InetAddress address = (InetAddress)en.nextElement();
                if(!address.isLoopbackAddress()) {
                    if(!(address instanceof Inet6Address)) {
                        return normalizeHostAddress(address);
                    }

                    ipv6Address = address;
                }
            }
        }

        if(ipv6Address != null) {
            return normalizeHostAddress(ipv6Address);
        } else {
            InetAddress localHost = InetAddress.getLocalHost();
            return normalizeHostAddress(localHost);
        }
    }

    public static String normalizeHostAddress(InetAddress localHost) {
        return localHost instanceof Inet6Address?localHost.getHostAddress():localHost.getHostAddress();
    }

    public static String getLastSegment() {
        try {
            String lastSegIP = getLocalHost();
            return lastSegIP.substring(lastSegIP.lastIndexOf(".") + 1);
        } catch (Exception var1) {
            return String.valueOf(System.currentTimeMillis() % 604800000L);
        }
    }
}
