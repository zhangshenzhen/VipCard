package com.bjypt.vipcard.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by chengwenlong on 2017/1/5.
 */

public class CompressPic {
    public static Bitmap compressImages(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 压缩图片，质量压缩法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    /**
     * 按比例大小压缩图片
     *
     * @param image
     * @return
     */
    public static Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 100) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    public static void saveBitmapFile(Bitmap bitmap, File file){
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param path 压缩图片的路径
     * @param count 图片质量
     * @param outPath 图片大小
     * @param outSize 压缩图片的输出路径
     * @return
     */
    public static String compressImage1(String path, int count, String outPath, int outSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inTempStorage = new byte[1024 * 20];
        Bitmap bitmapTemp = BitmapFactory.decodeFile(path, options);
        // if(count <= 70)
        int height_be = (int) (options.outHeight / (float) outSize);
        int width_be = (int) (options.outWidth / (float) outSize);
        int be = Math.max(height_be, width_be);
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;
        // options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        try {
            bitmapTemp = BitmapFactory.decodeFile(path, options);
            File pathfile = new File(outPath);
            new File(outPath).mkdirs(); // +"view.jpg"
            if (!pathfile.exists()) {
                pathfile.mkdirs();
                try {
                    new File(outPath + "view.jpg").createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileOutputStream out = new FileOutputStream(outPath + "view.jpg");
            bitmapTemp.compress(Bitmap.CompressFormat.JPEG, count, out);
            bitmapTemp.recycle();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return outPath + "view.jpg";
    }

    public static String compressDynamicImage(String path, String outPath, int outSize) {
        File file = new File(path);
        if (file.exists()) {
            if (file.length() >= 1024 * 1024 * 8) {
                return compressImage1(path, 10, outPath, outSize);
            } else if (file.length() >= 1024 * 1024 * 5 && file.length() < 1024 * 1024 * 8) {
                return compressImage1(path, 15, outPath, outSize);
            } else if (file.length() >= 1024 * 1024 * 4 && file.length() < 1024 * 1024 * 5) {
                return compressImage1(path, 20, outPath, outSize);
            } else if (file.length() >= 1024 * 1024 * 3 && file.length() < 1024 * 1024 * 4) {
                return compressImage1(path, 30, outPath, outSize);
            } else if (file.length() >= 1024 * 1024 * 2 && file.length() < 1024 * 1024 * 3) {
                return compressImage1(path, 40, outPath, outSize);
            } else if (file.length() >= 1024 * 1024 * 1 && file.length() < 1024 * 1024 * 2) {
                return compressImage1(path, 50, outPath, outSize);
            } else if (file.length() >= 1024 * 1024 * 0.5 && file.length() < 1024 * 1024 * 1) {
                return compressImage1(path, 60, outPath, outSize);
            } else if (file.length() >= 1024 * 1024 * 0.1 && file.length() < 1024 * 1024 * 0.5) {
                return compressImage1(path, 70, outPath, outSize);
            } else {
                return compressImage1(path, 100, outPath, outSize);
            }
        }
        return path;
    }
}
