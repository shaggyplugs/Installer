package ca.arnah.runelite;

public class ProxySetter {
    public static void setProxyProperties(String[] proxy) {
        if (proxy.length >= 2) {
            System.setProperty("socksProxyHost", proxy[0]);
            System.setProperty("socksProxyPort", proxy[1]);
            System.setProperty("socksProxyVersion", "5"); // Set SOCKS5 version
        }
        if (proxy.length >= 4) {
            System.setProperty("java.net.socks.username", proxy[2]);
            System.setProperty("java.net.socks.password", proxy[3]);
            System.setProperty("socksProxyVersion", "5"); // Set SOCKS5 version
        }
    }


    public static void setHttpProxyProperties(String[] proxy) {
        if (proxy.length >= 2) {
            System.setProperty("http.proxyHost", proxy[0]);
            System.setProperty("http.proxyPort", proxy[1]);
        }
        if (proxy.length >= 4) {
            System.setProperty("http.proxyUser", proxy[2]);
            System.setProperty("http.proxyPassword", proxy[3]);
        }
    }
}
