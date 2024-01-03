# RuneLiteHijack Proxy-Enabled Fork

This is a modified fork of [Arnuh's RuneLiteHijack](https://github.com/Arnuh/RuneLiteHijack) with reduced functionality, focusing on loading plugins from a folder. Additionally, it includes an enhanced installer that supports HTTPS and SOCKS5 proxies.

## Proxy Configuration

The installer reads proxy information from a file located at `jdk/proxy.json`. The format of this file should follow a specific order:

1. **IP:** The IP address of the proxy server.
2. **Port:** The port number of the proxy server.
3. **Username:** The username for proxy authentication.
4. **Password:** The password for proxy authentication.
5. **Clients Per Proxy:** The amount of clients that run on each proxy 
### Example proxy.txt content:

```json
"proxies":[
  "proxy":{
    "host": "127.0.0.1",
    "port":"1234",
    "username":"username",
    "password":"password"
}
],
"config":{
  "clients-per-proxy":5
}

```


In this example, each piece of information (IP, Port, Username, Password) is on a new line.

## Pre-built Launcher

If you prefer not to build the launcher yourself, a pre-built launcher is available on our Discord server. It includes all necessary components, from the Jagex patcher to support usage with the Jagex launcher, to proxy support.

**Discord Server:** [Join Here](https://discord.gg/s6ACcB5WEw)
