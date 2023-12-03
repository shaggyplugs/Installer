package ca.arnah.runelite;

import org.json.JSONObject;
public class ProxySetter {
    public static void setProxyProperties(JSONObject proxy) {
        // If there is no username then there will be no password so need to check the length
        if (proxy.getString("username")!= "") {
            System.out.println("Proxy has authentication, setting credentials");
            System.setProperty("java.net.socks.username", proxy.getString("username"));
            System.setProperty("java.net.socks.password", proxy.getString("password"));

        }
        // The proxy always needs this so we set it
            System.setProperty("socksProxyHost", proxy.getString("host"));
            System.setProperty("socksProxyPort", proxy.getString("port"));
            System.setProperty("socksProxyVersion", "5"); // Set SOCKS5 version

    }


    public static void setHttpProxyProperties(JSONObject proxy) {
        if (proxy.getString("username") != "") {
            System.setProperty("http.proxyUser", proxy.getString("username"));
            System.setProperty("http.proxyPassword", proxy.getString("password"));

        }

            System.setProperty("http.proxyHost", proxy.getString("host"));
            System.setProperty("http.proxyPort", proxy.getString("port"));

    }
}
