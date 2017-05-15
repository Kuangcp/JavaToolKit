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
        time.Start();
        String result = HttpRequest.sendGet("https://www.bing.com","");
        System.out.println("返回结果；"+result);
        time.End("");
        time.Start();
        result = HttpRequest.sendGet("http://localhost:8888/myth/test/redis","q=89");
        System.out.println("返回结果；"+result);
        time.End("");
    }
}
