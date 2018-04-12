package com.bjypt.vipcard.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/9
 * Use by Image工具类
 */
public class ImageUtils {

	public static Bitmap getSmallBitmap(String pathName, int width, int height) {
		if (pathName == null) {
			return null;
		}
		Bitmap bitmap = null;
		BitmapFactory.Options opts = null;
		if (opts == null) {
			opts = new BitmapFactory.Options();

			// 设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(pathName, opts);

			int hRatio = (int) Math.ceil(opts.outHeight / (float) width); // 图片是高度的几倍
			int wRatio = (int) Math.ceil(opts.outWidth / (float) height); // 图片是宽度的几倍

			// 缩小到 1/ratio的尺寸和 1/ratio^2的像素
			if (hRatio > 1 || wRatio > 1) {
				if (hRatio > wRatio) {
					opts.inSampleSize = hRatio;
				} else
					opts.inSampleSize = wRatio;
			}

			// 这里一定要将其设置回false，因为之前我们将其设置成了true
			opts.inJustDecodeBounds = false;
			opts.inTempStorage = new byte[100 * 1024];// 为位图设置100K的缓存
			opts.inInputShareable = true;// 设置解码位图的尺寸信息
			opts.inPurgeable = true;// 设置图片可以被回收
			opts.inPreferredConfig = Config.ARGB_8888; //
			// 每个像素占用1byte内存（8位）
			bitmap = BitmapFactory.decodeFile(pathName, opts);
		}
		return bitmap;
	}

	/**
	 * 计算要显示的图片,然后加载
	 * 
	 * @return
	 */
	public static Bitmap getDisPlayBg() {

		return null;
	}

	/**
	 * 通过decodeStream获取图片，以达到解决oom
	 * 
	 * @param drawableId
	 * @return
	 */
	public static Bitmap getDecodeResource(Resources res, int drawableId,
			int width, int height) {
		InputStream is = res.openRawResource(drawableId);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, opts);
		opts.inSampleSize = opts.outWidth / width;
		opts.outWidth = width;
		opts.outHeight = height;
		opts.inJustDecodeBounds = false;
		opts.inInputShareable = true;
		opts.inPurgeable = true;

		try {
			Bitmap bitmap = BitmapFactory.decodeStream(is, null, opts);
			if (bitmap == null) {
				return null;
			}
			if (bitmap.getWidth() != width) {
				bitmap = scaleBitmap(bitmap, width, height);
			}
			return bitmap;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 图片缩放
	 * 
	 * @param bitmapOrg
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap scaleBitmap(Bitmap bitmapOrg, int newWidth,
			int newHeight) {
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();

		// 计算缩放率，新尺寸除原始尺寸
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);

		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width,
				height, matrix, true);
		return resizedBitmap;
	}

	/**
	 * 图像处理成圆角
	 * 
	 * @param bitmap
	 * @param pixels
	 *            角半径
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 使用当前时间命名图片文件
	 */
	public static String getImageName() {
		String fileName = "";
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		fileName = format.format(date) + ".jpg";
		return fileName;
	}

	/**
	 * 使用当前时间命名音频文件
	 * 
	 * @return
	 */
	public static String getAudioName() {
		String audioName = "";
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		audioName = format.format(date) + ".mp3";
		return audioName;
	}

	/**
	 * 
	 * 方法说明
	 * 
	 * @param path
	 *            picture path
	 * @param photoName
	 *            picture filename
	 * @return null
	 */
	public static Bitmap savePhotoSDcard(String path, String photoName,
			Bitmap photoBitmap) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// 创建路径 这个path 是创建你保存图片的路径
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			// 在指定路径下创建文件
			File imageFile = new File(path, photoName);
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(imageFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
							fileOutputStream)) {
						fileOutputStream.flush();
						// fileOutputStream.close();
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				imageFile.delete();
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				imageFile.delete();
				e.printStackTrace();
			} finally {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return photoBitmap;
	}

	/**
	 * 
	 * @author  根据URL获取bitmap
	 * 
	 * */
	public static  Bitmap getBitmapURL(String urlString) {
		Bitmap bitmap;
		InputStream is;
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			is = new BufferedInputStream(conn.getInputStream());
			bitmap = BitmapFactory.decodeStream(is);
			conn.disconnect();
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private static FileNameGenerator generator;
	/**
	 * 初始化 ImageLoader
	 * @param context
	 */
	public static void initImageLoader(Context context) {
		if (generator == null) {
			generator = new HashCodeFileNameGenerator();
		}

		File cacheDir = StorageUtils.getCacheDirectory(context);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
		.memoryCacheExtraOptions(480, 800)
		.diskCacheExtraOptions(480, 800, null)
		//
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		//
		//
		.denyCacheImageMultipleSizesInMemory()
		.memoryCache(new WeakMemoryCache())
		.memoryCacheSizePercentage(15)
		//
		.diskCache(new UnlimitedDiscCache(cacheDir))
		// default
		.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(1000)
		.diskCacheFileNameGenerator(generator)//
		// .writeDebugLogs() // Remove for release app
		.build();

		ImageLoader.getInstance().init(config);
	}


}
