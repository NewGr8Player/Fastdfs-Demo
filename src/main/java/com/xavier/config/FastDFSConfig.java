package com.xavier.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "fastdfs")
public class FastDFSConfig {

	private int max_storage_connection;/* 最大连接数 default 8 */

	private int connect_timeout_in_seconds;/* 连接超时时间 */

	private int network_timeout_in_seconds;/* 网络超时时间 */

	private boolean http_anti_steal_token;/* 防盗链Token */

	private String charset;/* 字符集 */

	private String http_secret_key;/* 密钥 */

	private int http_tracker_http_port;/* Tracker Server提供HTTP服务的端口 */

	private List<String> tracker_servers;/* Tracker Server Group的地址列表 */

	public int getMax_storage_connection() {
		return max_storage_connection;
	}

	public void setMax_storage_connection(int max_storage_connection) {
		this.max_storage_connection = max_storage_connection;
	}

	public int getConnect_timeout_in_seconds() {
		return connect_timeout_in_seconds;
	}

	public void setConnect_timeout_in_seconds(int connect_timeout_in_seconds) {
		this.connect_timeout_in_seconds = connect_timeout_in_seconds;
	}

	public int getNetwork_timeout_in_seconds() {
		return network_timeout_in_seconds;
	}

	public void setNetwork_timeout_in_seconds(int network_timeout_in_seconds) {
		this.network_timeout_in_seconds = network_timeout_in_seconds;
	}

	public boolean isHttp_anti_steal_token() {
		return http_anti_steal_token;
	}

	public void setHttp_anti_steal_token(boolean http_anti_steal_token) {
		this.http_anti_steal_token = http_anti_steal_token;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getHttp_secret_key() {
		return http_secret_key;
	}

	public void setHttp_secret_key(String http_secret_key) {
		this.http_secret_key = http_secret_key;
	}

	public int getHttp_tracker_http_port() {
		return http_tracker_http_port;
	}

	public void setHttp_tracker_http_port(int http_tracker_http_port) {
		this.http_tracker_http_port = http_tracker_http_port;
	}

	public List<String> getTracker_servers() {
		return tracker_servers;
	}

	public void setTracker_servers(List<String> tracker_servers) {
		this.tracker_servers = tracker_servers;
	}

	@Override
	public String toString() {
		return "FastDFSConfig{" +
				"max_storage_connection=" + max_storage_connection +
				", connect_timeout_in_seconds=" + connect_timeout_in_seconds +
				", network_timeout_in_seconds=" + network_timeout_in_seconds +
				", http_anti_steal_token=" + http_anti_steal_token +
				", charset='" + charset + '\'' +
				", http_secret_key='" + http_secret_key + '\'' +
				", http_tracker_http_port=" + http_tracker_http_port +
				", tracker_servers=" + tracker_servers +
				'}';
	}
}
