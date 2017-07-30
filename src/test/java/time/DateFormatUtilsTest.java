package time;

import com.myth.time.DateFormatUtils;
import org.junit.Test;

import java.util.Date;

/**
 * Created by Administrator on 2017/05/09
 * Kuangchengping@outlook.com
 */
public class DateFormatUtilsTest {
    @Test
    public void toYMDHM(){
        String date = DateFormatUtils.toYMDHM(new Date());
        System.out.println(date);
    }
    @Test
    public void toYMDHMS(){
        String date = DateFormatUtils.toYMDHMS(new Date());
        System.out.println(date);
    }
}
