package com.myth.qiniu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import com.myth.common.exception.ConnectionException;
import com.myth.common.exception.MythException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.myth.common.info.ExceptionInfo.*;

/**
 * Created by https://github.com/kuangcp on 17-9-28  下午9:23
 */
public class QiNiuCloud {

    private static String accessKey;//accessKey
    private static String secretKey;//secretKey
    private static String bucketName;//要上传的空间名
    private static String qiniuLink;//七牛空间外链
    private static Auth auth;//密钥
    private static Zone zone;//空间
    private static Configuration config;

    /**
     * 加载配置文件并配置密钥()
     */
    public static void loadProperties() throws MythException {
        InputStream in = QiNiuCloud.class.getClassLoader().getResourceAsStream("qiniu.properties");
        Properties pro = new Properties();
        try{
            pro.load(in);//加载配置文件
        }catch(IOException e){
            throw new MythException(PROPERTIES_EXCEPTION,e,QiNiuCloud.class);
        }
        accessKey = pro.getProperty("accessKey");//加载ACCESS_KEY
        secretKey = pro.getProperty("secretKey");//加载SECRET_KEY
        bucketName = pro.getProperty("bucketName");//加载要上传的空间名
        qiniuLink = pro.getProperty("qiniuLink");//加载七牛空间外链
        auth = Auth.create(accessKey, secretKey);//密钥配置
        zone = Zone.autoZone();//指定上传的Zone的信息
        config = new Configuration(zone);
    }

    /**
     * 上传文件到七牛云，返回文件URL
     * @return URL
     */
    public static String upload(String filePath,String fileName) throws MythException{
        loadProperties();
        //创建上传对象
        UploadManager uploadManager = new UploadManager(config);
        Response res;
        try {
            //调用put方法上传文件
            res = uploadManager.put(filePath,fileName, auth.uploadToken(bucketName));
        } catch (IOException e) {
            throw new ConnectionException(UPLOADFILE_EXCEPTION,e,QiNiuCloud.class);
        }
        try{
            //解析返回的信息，得到图片地址
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(res.bodyString(),new TypeToken<Map<String, String>>(){}.getType());
            return qiniuLink +map.get("key");//返回文件URL
        }catch (IOException e) {
            throw new ConnectionException(PARSE_RESPONSE_EXCEPTION,e,QiNiuCloud.class);
        }
    }

    /**
     * 返回仓库下所有图片的URL
     * @return URL集合
     */
    public static List<String> getAllImage() throws MythException{
        loadProperties();
        BucketManager bucketManager = new BucketManager(auth, config);
        List<String> imageList = new ArrayList<>();//文件列表
        try {
            //调用listFiles方法列举指定空间的指定文件
            //参数一：bucket    空间名
            //参数二：prefix    文件名前缀
            //参数三：marker    上一次获取文件列表时返回的 marker
            //参数四：limit     每次迭代的长度限制，最大1000，推荐值 100
            //参数五：delimiter 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
            FileListing fileListing = bucketManager.listFiles(bucketName, null, null, 100, null);
            FileInfo[] items = fileListing.items;
            for (FileInfo fileInfo : items) {
                imageList.add(qiniuLink +fileInfo.key);
            }
        } catch (IOException e) {
            throw new ConnectionException(GET_IMAGE_LIST_EXCEPTION,e,QiNiuCloud.class);
        }
        return imageList;
    }

}