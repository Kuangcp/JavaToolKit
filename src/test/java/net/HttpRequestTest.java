package net;

import com.myth.net.HttpRequest;
import com.myth.time.GetRunTime;
import org.junit.Test;

/**
 * Created by mythos on 17-5-15
 * By kuangchengping@outlook.com
 */
public class HttpRequestTest {

    @Test
    public void testRun(){
        GetRunTime time = new GetRunTime();
        time.startCount();
        String result = HttpRequest.sendGet("https://www.bing.com","");
        System.out.println("返回结果；"+result);
        time.endCount("");
        time.startCount();
//        result = HttpRequest.sendGet("http://localhost:8888/myth/test/redis","?q=89");
//        System.out.println("返回结果；"+result);
        time.endCount("");
    }
    // 有道翻译的接口，使用的daocloud运行的实例注册的
    // http://fanyi.youdao.com/openapi.do?keyfrom=2048game&key=1859694540&type=data&doctype=json&version=1.1&q=good

    @Test
    public void testYouDao(){
        String content =
                "Let's you specify a custom LogoutSuccessHandler. If this is specified, logoutSuccessUrl() is ignored. For more information, please consult the JavaDoc.";
        youdaoQuery(content);
    }
    public void youdaoQuery(String content){
        GetRunTime time  = new GetRunTime();
        time.startCount();
        String url = "http://fanyi.youdao.com/openapi.do?keyfrom=2048game&key=1859694540&type=data&doctype=json&version=1.1";
        String result = HttpRequest.sendGet(url,"&q="+content);
        System.out.println("翻译结果"+result);
        String [] list = result.split("]");
        String temp = list[0].split(":")[1];
        temp = temp.substring(2,temp.length()-1);
//        for (String data : list) {
//            System.out.println(data);
//
//        }
        System.out.println(temp);

    }
}
