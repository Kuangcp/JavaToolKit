package qrcode;

import com.myth.qrcode.ReadQRCode;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp on 17-9-28  上午10:51
 */
public class ReadQRCodeTest {

    @Test
    public void testFile(){
        String result = ReadQRCode.readQRCodeFromFile("/home/kcp/Documents/1592490429.jpg");
        Assert.assertEquals("http://e22a.com/A.ChVCF",result);
    }
}
