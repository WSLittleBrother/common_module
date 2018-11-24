package com.sunwayworld.common.utils.retrofit2.service;


import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 当@GET或@POST注解的url为全路径时（可能和baseUrl不是一个域），会直接使用注解的url的域。
 * 如果请求为post实现，那么最好传递参数时使用@Field、@FieldMap和@FormUrlEncoded。因为@Query和或QueryMap都是将参数拼接在url后面的，再get请求数据的时候使用，而@Field或@FieldMap传递的参数时放在请求体的。
 * 使用@Path时，path对应的路径不能包含”/”，否则会将其转化为%2F。在遇到想动态的拼接多节url时，还是使用@Url吧。
 * <p>
 * Created by Yanghan on 2017/12/8.
 * <p>
 * 该接口禁止再添加其他方法，如需添加请重新创建新的接口
 */
public interface BaseService {

    /****
     * 上传接口，RequestBody;用于上传文件和数据的集合体
     * FormUrlEncoded只能在post请求中加，并且是必须要加，但遇到 @Body 则不需要添加
     *
     * 注：尽量避免使用改接口处理下载数据
     *
     * @param url 该url必须为全路径，例：http://www.baidu.com
     *@param requestBody 请求体
     * ****/
    @POST
    Observable<ResponseBody> uploadData(@Url String url, @Body RequestBody requestBody);

    /****
     * 获取数据接口
     * @param url 该url必须为全路径，例：http://www.baidu.com
     * ****/
    @GET
    Observable<ResponseBody> getData(@Url String url);
    /****
     * 下载数据接口，RequestBody;用于上传文件和数据的集合体
     * FormUrlEncoded只能在post请求中加，并且是必须要加,但遇到 @Body 则不需要添加
     *
     * 注：尽量避免使用该接口下载数据
     *
     * @param url 该url必须为全路径，例：http://www.baidu.com
     *@param requestBody 请求体
     * ****/
    @POST
    Observable<ResponseBody> postData(@Url String url, @Body RequestBody requestBody);

}
