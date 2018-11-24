package com.sunwayworld.common.utils.image;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sunwayworld.common.constants.PathConstants;

import java.io.File;
import java.math.BigDecimal;

/**
 * 不使用GlideUtils命名的原因：可以动态更换不同图片加载框架，不需要重命名文件和文件夹名或者重建多个文件等
 * Created by Think on 2016/12/7.
 */

public class ImageUtils {
    private static ImageUtils inst;
    private final String ImageExternalCatchDir = PathConstants.APP_SDCARD_PATH + "/image_cache";

    public static ImageUtils getInstance() {
        if (inst == null) {
            inst = new ImageUtils();
        }
        return inst;
    }

    /**
     * 显示 图片，注：这里在内存和磁盘都不进行缓存，有需要请自行重载
     *
     * @param imageView 显示图片的ImageView
     * @param imageFile 图片文件
     */
    public static <M> void displayImage(Context context, ImageView imageView, M imageFile) {
        if (imageFile instanceof File) {
            Glide.with(context).load(((File) imageFile)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).fitCenter().into(imageView);
        } else if (imageFile instanceof String) {
            Glide.with(context).load(((String) imageFile)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).fitCenter().into(imageView);
        } else if (imageFile instanceof byte[]) {
            Glide.with(context).load(((byte[]) imageFile)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).fitCenter().into(imageView);
        }
    }

    /**
     * 显示 图片，注：这里在内存和磁盘都不进行缓存，有需要请自行重载
     *
     * @param imageView 显示图片的ImageView
     * @param resId     图片资源id
     */
    public static void displayImage(Context context, ImageView imageView, int resId) {
        Glide.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).fitCenter().into(imageView);
    }

    /**
     * 显示GIF图片
     *
     * @param imageView 显示图片的ImageView
     * @param resId     图片资源id
     */
    public static void displayImageForGif(Context context, ImageView imageView, int resId) {
        Glide.with(context).load(resId).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }

    /**
     * 清除图片所有缓存
     */
    public static void clearImageAllCache(Context context) {
        clearImageDiskCache(context);
        clearImageMemoryCache(context);
    }

    /**
     * 清除图片磁盘缓存
     */
    public static void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public static void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public String getCacheSize() {
        try {
            return getFormatSize(getFolderSize(new File(ImageExternalCatchDir)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    public long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    public static String getFormatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath       filePath
     * @param deleteThisPath deleteThisPath
     */
    public void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), deleteThisPath);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
