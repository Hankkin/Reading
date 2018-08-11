/**
 * @(#)AudioMgr.java, 2015年12月14日. 
 * 
 * Copyright 2015 Yodao, Inc. All rights reserved.
 * YODAO PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hankkin.reading.utils;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.util.Log;

import com.youdao.sdk.common.util.AsyncTasks;

/**
 * 
 * @author lukun
 * 
 */
public class AudioMgr {

	public interface SuccessListener {
		public void success();
		public void playover();
	}

	public static void startPlayVoice(String url, SuccessListener listener) {
		try {
			AsyncTasks.safeExecuteOnExecutor(new PlayTask(url, listener));
		} catch (Exception e) {
			Log.d("AudioMgr","fail to fetch data: ", e);
		}
	}

	static class PlayTask extends AsyncTask<Void, Void, Void> {
		private String mUrl;

		private SuccessListener listener;

		public PlayTask(String url, SuccessListener listener) {
			mUrl = url;
			this.listener = listener;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (isCancelled()) {
				onCancelled();
				return;
			}
			try {
				MediaPlayer mediaPlayer = PlayMgr.getInstance()
						.getMediaPlayer();
				mediaPlayer.start();
			} catch (Exception e) {
			}
			if(listener!=null){
				listener.success();
			}
		}

		@Override
		protected void onCancelled() {
		}

		@Override
		protected Void doInBackground(Void... params) {

			MediaPlayer mediaPlayer = PlayMgr.getInstance()
					.getMediaPlayer();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                   if(listener!=null){
                	   listener.playover();
                   }
                }
            });
			try {
				mediaPlayer.reset();
				mediaPlayer.setDataSource(mUrl);
				mediaPlayer.prepare();// 进行缓冲
			} catch (Exception e) {
			    e.printStackTrace();
			}
			return null;
		}
	}

}
