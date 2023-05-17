package com.talkcloud.networkshcool.baselibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.talkcloud.networkshcool.baselibrary.TKBaseApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * 文件工具类
 *
 * @author gyw
 * @version 1.0
 * @time: 2015-8-19 下午4:16:18
 * @fun:
 */
public class FileUtil {

	public static final String ROOT_DIR = "NetWorkSchool";
	public static final String DOWNLOAD_DIR = "download";
	public static final String IMAGE_DIR = "image";
	public static final String CACHE_DIR = "cache";

	/** 判断SD卡是否挂载 */
	public static boolean isSDCardAvailable() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			return true;
		} else {
			return false;
		}
	}

	/** 获取下载目录 */
	public static String getDownloadDir(Context context) {
		return getDir(context, DOWNLOAD_DIR);
	}

	/** 获取头像目录 */
	public static String getImageDir(Context context) {
		return getDir(context, IMAGE_DIR);
	}

	/** 获取缓存目录 */
	public static String getCacheDir(Context context) {
		return getDir(context, CACHE_DIR);
	}

	/** 获取应用目录，当SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的cache目录 */
	public static String getDir(Context context, String name) {
		StringBuilder sb = new StringBuilder();
		if (isSDCardAvailable()) {
			sb.append(getExternalStoragePath());
		} else {
			sb.append(getCachePath(context));
		}
		sb.append(name);
		sb.append(File.separator);
		String path = sb.toString();
		if (createDirs(path)) {
			return path;
		} else {
			return null;
		}
	}

	/** 获取SD下的应用目录 */
	public static String getExternalStoragePath() {
		StringBuilder sb = new StringBuilder();
		sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
		sb.append(File.separator);
		sb.append(ROOT_DIR);
		sb.append(File.separator);
		return sb.toString();
	}

	/** 获取SD下的Android/data/应用目录 */
	public static String getExternalFilesDir(Context context) {
		StringBuilder sb = new StringBuilder();
		sb.append(context.getExternalFilesDir("").getAbsolutePath());
		sb.append(File.separator);
		sb.append(ROOT_DIR);
		sb.append(File.separator);
		return sb.toString();
	}

	/** 获取应用的cache目录 */
	public static String getCachePath(Context context) {
		File f = context.getCacheDir();
		if (null == f) {
			return null;
		} else {
			return f.getAbsolutePath() + "/";
		}
	}

	/** 创建文件夹 */
	public static boolean createDirs(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists() || !file.isDirectory()) {
			return file.mkdirs();
		}
		return true;
	}

	/** 创建文件 */
	public static boolean createFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists() || file.isDirectory()) {// 如果文件不存在，或者文件存在，但是是文件夹
			String parent = file.getParent();
			File parentFile = new File(parent);// 根据父路径创建文件对象
			if (!parentFile.exists() || !parentFile.isDirectory()) {
				parentFile.mkdirs();
			}
			try {
				file.createNewFile();// 创建文件
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	/** 判断文件是否存在 */
	public static boolean isExistFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {// 如果文件不存在，或者文件存在，但是是文件夹
			return false;
		}
		return true;
	}

	/** 删除文件 */
	public static void deleteFile(Context context, String filePath) {
		File file = new File(filePath);
		if (file.exists()) { // 判断文件是否存在
			file.delete(); // 删除文件
		} else {
			// ToastUtil.showShortToast(context, "文件不存在或已删除");
		}
	}

	/** 删除文件 */
	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) { // 判断文件是否存在
			file.delete(); // 删除文件
		}
	}

	/**
	 *  拷贝db
	 */
	public static void copyDB(String dbName) {
		try {
			File file = new File(TKBaseApplication.myApplication.getFilesDir(), dbName);
			// 判断文件是否存在
			if (file.exists() && file.length() > 0) {
				return;
			}
			// 获取资产目录下文件读取流
			InputStream is = TKBaseApplication.myApplication.getAssets().open(dbName);
			FileOutputStream fos = TKBaseApplication.myApplication.openFileOutput(dbName, Context.MODE_PRIVATE);

			int len = 0;

			byte[] bys = new byte[1024];

			while ((len = is.read(bys)) != -1) {
				fos.write(bys, 0, len);
			}
			is.close();
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param root
	 * 删除文件夹下的所有文件
	 */
	public static void deleteAllFiles(File root) {
		File files[] = root.listFiles();
		if (files != null)
			for (File f : files) {
				if (f.isDirectory()) { // 判断是否为文件夹
					deleteAllFiles(f);
					try {
						f.delete();
					} catch (Exception e) {
					}
				} else {
					if (f.exists()) { // 判断是否存在
						deleteAllFiles(f);
						try {
							f.delete();
						} catch (Exception e) {
						}
					}
				}
			}
	}

	/**
	 * 保存图片
	 * */
	public static void saveBitmap(Bitmap bit) {
		String path = getImageDir(TKBaseApplication.myApplication);
		String fileName = new Date().getTime() + ".jpg";
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String file = path + fileName;
		try {
			if(!isExistFile(file)){
				File f = new File(file);
				f.createNewFile();
				FileOutputStream fOut = null;
				fOut = new FileOutputStream(f);
				bit.compress(Bitmap.CompressFormat.PNG, 100, fOut);
				fOut.flush();
				fOut.close();
				//更新相册
				Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				Uri uri = Uri.fromFile(f);
				intent.setData(uri);
				TKBaseApplication.myApplication.sendBroadcast(intent);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 保存缓存
	 * @param str
	 */
	public static void saveFileCache(String str, String fileName) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File dir = new File(getCacheDir(TKBaseApplication.myApplication));
			if (!dir.exists()) {
				dir.mkdirs();
			}

			try {
				String fileAbsName = dir.getAbsolutePath() + File.separator + fileName;
				FileOutputStream fos = new FileOutputStream(fileAbsName, false);
				fos.write(str.toString().getBytes());
				fos.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public static String getFileCache(String fileName) {
		String fileAbsName = getCacheDir(TKBaseApplication.myApplication) + fileName;
//		LogUtil.d(" pathName : " + fileAbsName);
		if (!isExistFile(fileAbsName)) {
			return null;
		}
		try {
			FileInputStream fis = new FileInputStream(fileAbsName);
			return stream2String(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/*
	 * 将读取输入流转化为字符串
	 */
	public static String stream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int len = -1;
		byte[] bys = new byte[1024];
		while((len = is.read(bys)) != -1) {
			baos.write(bys, 0, len);
		}
		return new String(baos.toByteArray());
	}

}
