package com.sunwayworld.common.utils.http;

import android.content.Context;
import android.util.Log;

import com.sunwayworld.common.dao.model.AppModel;
import com.sunwayworld.common.utils.CompressUtils;
import com.sunwayworld.common.utils.EncoderUtils;

import java.io.File;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author yanghan ${2016/5/13}
 * @version V1.0
 * @Description 网络请求进行加密处理
 */
public class HttpBuilder {
    private static final String TAG = "HttpBuilder";
    private FormBody.Builder builder = new FormBody.Builder();
    private MultipartBody.Builder multi = new MultipartBody.Builder();
    private Context context;
    protected String deskey = null;

    private static final MediaType MEDIA_TYPE_WORD = MediaType.parse("application/msword");
    private static final MediaType MEDIA_TYPE_EXCEL = MediaType.parse("application/msexcel");
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/octet-stream");
    private static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");
    private static final MediaType MEDIA_TYPE_PDF = MediaType.parse("application/pdf");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json");
    private static final MediaType MEDIA_TYPE_HTML = MediaType.parse("application/html");

    /*String[] allowType = new String[] { "text/html", "application/pdf;charset=UTF-8", "text/plain;charset=UTF-8",
                "application/msword;charset=UTF-8", "application/msexcel;charset=utf-8",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8",
                "application/vnd.ms-excel;charset=UTF-8", "image/jpeg;charset=UTF-8", "image/png;charset=UTF-8" };*/
    public HttpBuilder() {
    }

    public HttpBuilder(Context context) {
        this.context = context;
    }

    public HttpBuilder(Context context, String deskey) {
        this.context = context;
        this.deskey = deskey;
    }

    public void add(String key, String value) {
        if (key != null && value != null) {
            try {
                if (AppModel.getSysConfig() != null && AppModel.getSysConfig().isEncrypt() && deskey != null) {
                    String compress = CompressUtils.compressStr(EncoderUtils.getencString(value, deskey));
                    builder.add(key, compress);
                    Log.d(TAG, "数据已加密");
                } else {
                    builder.add(key, value);
                    Log.d(TAG, "数据未加密");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setMultiType() {
        multi.setType(MultipartBody.FORM);
    }

    public void addMulti(String key, String value) {
        if (key != null && value != null) {
            try {
                if (AppModel.getSysConfig() != null && AppModel.getSysConfig().isEncrypt() && deskey != null) {
//                    String compress = ZipUtils.zipStr(EncryptUtils.encrypt3DES2Base64(value.getBytes(), deskey.getBytes()));
                    String compress = CompressUtils.compressStr(EncoderUtils.getencString(value, deskey));
                    multi.addFormDataPart(key, compress);
//                    multi.addPart(MultipartBody.Part.createFormData(key, null, RequestBody.create(MEDIA_TYPE_TEXT, compress)));
                } else {
                    multi.addFormDataPart(key, value);
//                    multi.addPart(MultipartBody.Part.createFormData(key, null, RequestBody.create(MEDIA_TYPE_TEXT, value)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addFileMulti(String key, String filename, File file, MediaType type) {
        if (key != null && file != null) {
            Log.d("YY007", "filename=" + file.getName());
            multi.addFormDataPart(key, filename, RequestBody.create(type, file));
        }
    }

    public void addFileMulti(String key, String filename, File file) {
        if (key != null && file != null) {
            Log.d("YY007", "filename=" + file.getName());

//            if (file.getName().lastIndexOf("png") > 0 || file.getName().lastIndexOf("PNG") > 0) {
//                multi.addFormDataPart(key, filename, RequestBody.create(MEDIA_TYPE_PNG, file));
//            } else if (file.getName().lastIndexOf("jpg") > 0 || file.getName().lastIndexOf("JPG") > 0
//                    || file.getName().lastIndexOf("jpeg") > 0 || file.getName().lastIndexOf("jpeg") > 0) {
//                multi.addFormDataPart(key, filename, RequestBody.create(MEDIA_TYPE_JPEG, file));
//            } else if (file.getName().lastIndexOf("txt") > 0 || file.getName().lastIndexOf("TXT") > 0) {
//                multi.addFormDataPart(key, filename, RequestBody.create(MEDIA_TYPE_TEXT, file));
//            } else if (file.getName().lastIndexOf("pdf") > 0 || file.getName().lastIndexOf("PDF") > 0) {
            multi.addFormDataPart(key, filename, RequestBody.create(MEDIA_TYPE, file));
//            } else {
//                T.showShort("未找到该文件数据类型");
//            }
        }
    }


    public FormBody.Builder getBuilder() {
        return builder;
    }

    public MultipartBody.Builder getMultiBuilder() {
        return multi;
    }
}
