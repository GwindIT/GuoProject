package com.idsmanager.eagleeye.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by 雅麟 on 2015/6/16.
 */
public class ImageUtil {


    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static String getCompressedFilesBase64(List<String> pathList) {
        if (pathList == null) {
            return null;
        }
        int size = pathList.size();
        StringBuilder sb = new StringBuilder();
        String separator = ",";
        for (int i = 0; i < size; i++) {
            String path = pathList.get(i);
            try {
                Bitmap bitmap = getSmallBitmap(path, 600, 800);
                int rotate = readPicDegree(path);
                if (rotate > 0) {
                    bitmap = rotateBitmap(rotate, bitmap);
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int quality = 100;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                while (baos.toByteArray().length / 1024 > 50) {
                    baos.reset();
                    quality -= 10;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                }
                String imageBase64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                sb.append(imageBase64);
                if (i < size - 1) {
                    sb.append(separator);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.length() > 0 ? sb.toString() : null;
    }

    public static Bitmap getSmallBitmap(String filePath, int requestWidth, int requestHight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, requestWidth, requestHight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int readPicDegree(String path) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        }

        return degree;
    }

    public static Bitmap rotateBitmap(int degree, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }
}
