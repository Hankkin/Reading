/**
 * @(#)PlayMgr.java, 2014年10月13日. Copyright 2012 Yodao, Inc. All rights
 *                           reserved. YODAO PROPRIETARY/CONFIDENTIAL. Use is
 *                           subject to license terms.
 */
package com.hankkin.reading.utils;

import android.media.MediaPlayer;

/**
 * @author lukun 解析数据为对象
 */
public class PlayMgr  {

	private static PlayMgr mgr;

	private PlayMgr() {
		super();
	}

	public synchronized static PlayMgr getInstance() {
		if (mgr == null) {
			mgr = new PlayMgr();
		}
		return mgr;
	}
 
	private static MediaPlayer mMediaPlayer;

	public synchronized MediaPlayer getMediaPlayer() {
		if (mMediaPlayer == null)
//			mMediaPlayer = MediaPlayer.create(DemoApplication.getInstance(), R.raw.office);
			mMediaPlayer = new MediaPlayer();
		return mMediaPlayer;
	}

	public synchronized void startMediaPlayer(String audioUrl) {
		MediaPlayer mediaPlayer = getMediaPlayer();
		mediaPlayer.reset();
		try {
			mediaPlayer.setDataSource(audioUrl);
			mediaPlayer.prepare();// 进行缓冲
			mediaPlayer.start();
		} catch (Exception e) {
		}
	} 
}
