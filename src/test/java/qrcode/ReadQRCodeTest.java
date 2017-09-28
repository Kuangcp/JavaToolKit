package qrcode;

import com.google.zxing.NotFoundException;
import com.myth.qrcode.ReadQRCode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by https://github.com/kuangcp on 17-9-28  上午10:51
 */
public class ReadQRCodeTest {

    @Test
    public void testFile(){
//        String result = ReadQRCode.readQRCodeFromFile("/home/kcp/Documents/1592490429.jpg");
//        Assert.assertEquals("http://e22a.com/A.ChVCF",result);
    }

    @Test
    public void testUrl(){
        try {
            ReadQRCode.readQRCodeFromURL("http://ovjs7rsrm.bkt.clouddn.com/501683107.jpg");
        } catch (IOException | NotFoundException e) {
            e.printStackTrace();
            System.out.println("uiui");
        }
    }
}
