package com.xavier.common;

import com.xavier.config.FastDFSConfig;
import com.xavier.config.FastDFSException;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerGroup;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * TrackerServer 对象池
 */
@Component
public class TrackerServerPool {

	private static FastDFSConfig fastDFSConfig;

	@Autowired
	public void setFastDFSConfig(FastDFSConfig fastDFSConfig) {
		TrackerServerPool.fastDFSConfig = fastDFSConfig;
	}

	/**
	 * TrackerServer 对象池 GenericObjectPool 没有无参构造
	 */
	private static GenericObjectPool<TrackerServer> trackerServerPool;

	private TrackerServerPool() {
	}

	private static synchronized GenericObjectPool<TrackerServer> getObjectPool() {
		if (trackerServerPool == null) {
			System.out.println(fastDFSConfig);
			/* 主动设置参数 */
			ClientGlobal.setG_connect_timeout(fastDFSConfig.getConnect_timeout_in_seconds());/* 连接超时的时限，单位为毫秒   */
			ClientGlobal.setG_network_timeout(fastDFSConfig.getConnect_timeout_in_seconds());/* 网络超时的时限，单位为毫秒 */
			ClientGlobal.setG_anti_steal_token(fastDFSConfig.isHttp_anti_steal_token());/* 防盗链 */
			ClientGlobal.setG_charset(fastDFSConfig.getCharset());/* 字符集 */
			ClientGlobal.setG_secret_key(fastDFSConfig.getHttp_secret_key());
			ClientGlobal.setG_tracker_http_port(fastDFSConfig.getHttp_tracker_http_port());/* HTTP访问服务的端口号 */
			/* Tracker服务器列表 */
			List<String> tracker_group = fastDFSConfig.getTracker_servers();
			int len = tracker_group.size();
			InetSocketAddress[] tracker_servers = new InetSocketAddress[len];
			String tempInfo = "";
			for (int i = 0; i < len; i++) {
				tempInfo = tracker_group.get(i);
				tracker_servers[i] = new InetSocketAddress(tempInfo.split(":")[0], Integer.parseInt(tempInfo.split(":")[1]));
			}
			ClientGlobal.setG_tracker_group(new TrackerGroup(tracker_servers));

			GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
			poolConfig.setMinIdle(2);
			if (fastDFSConfig.getMax_storage_connection() > 0) {
				poolConfig.setMaxTotal(fastDFSConfig.getMax_storage_connection());
			}

			trackerServerPool = new GenericObjectPool<>(new TrackerServerFactory(), poolConfig);
		}
		return trackerServerPool;
	}

	/**
	 * 获取 TrackerServer
	 *
	 * @return TrackerServer
	 * @throws FastDFSException
	 */
	public static TrackerServer borrowObject() throws FastDFSException {
		TrackerServer trackerServer = null;
		try {
			trackerServer = getObjectPool().borrowObject();
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof FastDFSException) {
				throw (FastDFSException) e;
			}
		}
		return trackerServer;
	}

	/**
	 * 回收 TrackerServer
	 *
	 * @param trackerServer 需要回收的 TrackerServer
	 */
	public static void returnObject(TrackerServer trackerServer) {

		getObjectPool().returnObject(trackerServer);
	}


}
