package com.hcb.hcbsdk.util;

/**
 * @author  Administrator
 * @des	TODO
 *
 * @version $Rev: 14 $
 * @updateAuthor $Author: admin $
 * @updateDes TODO
 */
public class ThreadPoolFactory {
	static ThreadPoolProxy	mNormalPool;
	static ThreadPoolProxy	mDownLoadPool;

	/**得到一个普通的线程池*/
	public static ThreadPoolProxy getNormalPool() {
		if (mNormalPool == null) {
			synchronized (ThreadPoolProxy.class) {
				if (mNormalPool == null) {
					mNormalPool = new ThreadPoolProxy(5, 5, 3000);
				}
			}
		}
		return mNormalPool;
	}
	/**得到一个下载的线程池*/
	public static ThreadPoolProxy getDownLoadPool() {
		if (mDownLoadPool == null) {
			synchronized (ThreadPoolProxy.class) {
				if (mDownLoadPool == null) {
					mDownLoadPool = new ThreadPoolProxy(3, 3, 3000);
				}
			}
		}
		return mDownLoadPool;
	}
}
