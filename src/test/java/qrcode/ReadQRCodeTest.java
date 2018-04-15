package qrcode;

import com.google.zxing.NotFoundException;
import com.myth.qrcode.ReadQRCode;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by https://github.com/kuangcp on 17-9-28  上午10:51
 * 测试都通过了
 */
@Ignore
public class ReadQRCodeTest {

    @Test
    public void testFile() throws IOException, NotFoundException {
        String result = ReadQRCode.readQRCodeFromFile("/home/kcp/Documents/1592490429.jpg");
        Assert.assertEquals("http://e22a.com/A.ChVCF",result);
    }

    @Test
    public void testUrl(){
        try {
            ReadQRCode.readQRCodeFromURL("http://ovjs7rsrm.bkt.clouddn.com/1592490429.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
