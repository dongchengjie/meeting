package com.dcj.meeting.util;

import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ImageUtil {

    private final static List<String> accept = Arrays.asList(".png", ".gif", ".jpeg", ".jpg", ".bmp");

    //按照指定比例进行缩小和放大
    public static void scale(File file, double scale) {
        try {
            Thumbnails.of(file).scale(scale).toFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //压缩图片文件大小,图片精度由quality决定,等于1为最高质量
    public static void compress(File file, double quality) {
        try {
            Thumbnails.of(file).scale(1f).outputQuality(quality).toFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isSupported(String suffix) {
        boolean supported = false;
        for (String s : accept) {
            if (s.equals(suffix)) {
                supported = true;
                break;
            }
        }
        return supported;
    }
}
