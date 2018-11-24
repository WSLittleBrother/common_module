package com.sunwayworld.common.utils;

import com.sunwayworld.common.constants.PathConstants;
import com.sunwayworld.common.dao.model.AppModel;
import com.sunwayworld.utils.util.SDCardUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 99351 on 2018/1/19.
 */

public class FileHelperUtil {
    public static String copyFile(String path){
        //先得到后缀
        String suffix ="";
        if (path.lastIndexOf(".") ==-1){
            suffix = "";
        }else{
            suffix=path.substring(path.lastIndexOf("."));
        }
        String fileName = "";
        if (SDCardUtils.isSDCardEnable()) {
            fileName = PathConstants.SDCARD_USER_DATA_PATH
                    + "/comment/camera_" + "_"
                    + Long.toString(System.currentTimeMillis()) + suffix;
        }
        FileInputStream in=null;
        FileOutputStream out=null;
        byte[] b=new byte[1024];
        try {//输入流的来源
            in=new FileInputStream(path);
            //输出流的去向
            out=new FileOutputStream(fileName);
            while(true)
            {//进行输入操作
                int num=in.read(b, 0, b.length);
                if(num==-1)
                {break;}
                //进行输出操作
                out.write(b,0,b.length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try {
                //关闭流
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    //获取去除后缀名的文件名
    public static String getNoSuffixNameByFilename(String fileName) {
        if (fileName.lastIndexOf(".")==-1){
            return fileName;
        }
        return fileName.substring(0, fileName.lastIndexOf("."));//获取除后缀1位的名
    }

    //获取文件的后缀名
    public static String getSuffixByFilename(String fileName) {
        if (fileName.lastIndexOf(".") ==-1){
            return  "";
        }else{
            return fileName.substring(fileName.lastIndexOf("."));
        }
    }

}
