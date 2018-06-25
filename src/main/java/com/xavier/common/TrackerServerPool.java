package com.xavier.common;

import com.xavier.config.FastDFSException;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

/**
 * TrackerServer 对象池
 */
public class TrackerServerPool {

	/**
	 * TrackerServer 对象池
	 */
	private static GenericObjectPool<TrackerServer> trackerServerPool;

	/**
	 * TrackerServer 配置文件路径
	 */
	private static final String FASTDFS_CONFIG_PATH = "config.properties";

	/**
	 * 最大连接数 default 8.
	 */
	@Value("${pool.max_storage_connection}")
	private static int maxStorageConnection;

	private TrackerServerPool(){}

	private static synchronized GenericObjectPool<TrackerServer> getObjectPool(){
		if(trackerServerPool == null){
			try {
				// 加载配置文件
				ClientGlobal.initByProperties(FASTDFS_CONFIG_PATH);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (MyException e) {
				e.printStackTrace();
			}

			GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
			poolConfig.setMinIdle(2);
			if(maxStorageConnection > 0){
				poolConfig.setMaxTotal(maxStorageConnection);
			}

			trackerServerPool = new GenericObjectPool<>(new TrackerServerFactory(), poolConfig);
		}
		return trackerServerPool;
	}

	/**
	 * 获取 TrackerServer
	 * @return TrackerServer
	 * @throws FastDFSException
	 */
	public static TrackerServer borrowObject() throws FastDFSException {
		TrackerServer trackerServer = null;
		try {
			trackerServer = getObjectPool().borrowObject();
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof FastDFSException){
				throw (FastDFSException) e;
			}
		}
		return trackerServer;
	}

	/**
	 * 回收 TrackerServer
	 * @param trackerServer 需要回收的 TrackerServer
	 */
	public static void returnObject(TrackerServer trackerServer){

		getObjectPool().returnObject(trackerServer);
	}

}
