package ca.arnah.runelite;


import net.runelite.client.RuneLite;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.*;
import javax.swing.UIManager;

/**
 * @author Arnah
 * @since Nov 07, 2020
 */
public class LauncherHijack{

	public LauncherHijack(){
		new Thread(()->{
			// First we need to grab the ClassLoader the launcher uses to launch the client.
			ClassLoader objClassLoader;
			loop:
			while(true){
				objClassLoader = (ClassLoader) UIManager.get("ClassLoader");
				if(objClassLoader != null){
					for(Package pack : objClassLoader.getDefinedPackages()){
						if(pack.getName().equals("net.runelite.client.rs")){
							break loop;
						}
					}
				}
				try{
					Thread.sleep(100);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			System.out.println("Classloader found");
			try{
				URLClassLoader classLoader = (URLClassLoader) objClassLoader;
				// Add our hijack client to the classloader
				Method addUrl = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
				addUrl.setAccessible(true);

				URI uri = LauncherHijack.class.getProtectionDomain().getCodeSource().getLocation().toURI();
				if(uri.getPath().endsWith("classes/")){// Intellij
					uri = uri.resolve("..");
				}
				if(!uri.getPath().endsWith(".jar")){
					uri = uri.resolve("EthanVannInstaller.jar");
				}
				addUrl.invoke(classLoader, uri.toURL());
				System.out.println(uri.getPath());

				// Execute our code inside the runelite client classloader
				Class<?> clazz = classLoader.loadClass(ClientHijack.class.getName());
				clazz.getConstructor().newInstance();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}).start();
	}

	public static void main(String[] args) throws IOException {
		System.setProperty("runelite.launcher.reflect", "true");

		String proxyFilePath = System.getProperty("user.home")+ "\\jdk\\proxy.json";
		File proxyFile = new File(proxyFilePath);
		System.out.println(proxyFilePath);
		if (proxyFile.exists()) {
			JSONObject proxyDetails = readProxyFromFile(proxyFilePath);
			if (proxyDetails != null) {
				String proxyHost = proxyDetails.getString("host");
				int proxyPort = Integer.parseInt(proxyDetails.getString("port"));
				String proxyUsername = proxyDetails.getString("username");
				String proxyPassword = proxyDetails.getString("password");

				boolean isSocks5 = testSocks5Proxy(proxyHost, proxyPort, proxyUsername, proxyPassword);
				boolean isHttp = testHttpProxy(proxyHost, proxyPort, proxyUsername, proxyPassword);

				if (isSocks5) {
					System.out.println("The proxy is SOCKS5.");
					ProxySetter.setProxyProperties(proxyDetails);
				} else if (isHttp) {
					System.out.println("The proxy is HTTP.");
					ProxySetter.setHttpProxyProperties(proxyDetails);
				} else {
					System.out.println("The proxy type could not be determined.");
				}
			} else {
				System.out.println("Invalid proxy details in the proxy.txt file.");
			}
		}

		try {
			new LauncherHijack();
			Class<?> clazz = Class.forName("net.runelite.launcher.Launcher");
			clazz.getMethod("main", String[].class).invoke(null, (Object) args);
		} catch (Exception ignored) {
		}
	}


	private static boolean testSocks5Proxy(String proxyHost, int proxyPort, String proxyUsername, String proxyPassword) {
		try {
			SocketAddress proxyAddress = new InetSocketAddress(proxyHost, proxyPort);
			Proxy proxy = new Proxy(Proxy.Type.SOCKS, proxyAddress);
			URL url = new URL("https://www.runescape.com");

			Authenticator.setDefault(new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(proxyUsername, proxyPassword.toCharArray());
				}
			});

			URLConnection connection = url.openConnection(proxy);
			connection.connect();

			return true;  // SOCKS5 proxy is working
		} catch (IOException e) {
			return false; // SOCKS5 proxy is not working
		}
	}


	private static boolean testHttpProxy(String proxyHost, int proxyPort, String proxyUsername, String proxyPassword) {
		try {
			SocketAddress proxyAddress = new InetSocketAddress(proxyHost, proxyPort);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddress);
			URL url = new URL("https://www.runescape.com");

			Authenticator.setDefault(new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(proxyUsername, proxyPassword.toCharArray());
				}
			});

			URLConnection connection = url.openConnection(proxy);
			connection.connect();

			return true;  // HTTP proxy is working
		} catch (IOException e) {
			return false; // HTTP proxy is not working
		}
	}


	private static JSONObject readProxyFromFile(String filePath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		StringBuffer buffer = new StringBuffer();
		while (true) {
			String line;
			try {
				line = reader.readLine();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if (line == null) {
				break;
			} else {
				buffer.append(line);
				buffer.append("\n");
			}
		}

		JSONObject json = new JSONObject(buffer.toString());
		JSONArray proxies = json.getJSONArray("proxies");
		JSONObject proxy = proxies.getJSONObject(0);
		proxy.put("config",json.getJSONObject("config"));
		reader.close();
		return proxy;
	}

}