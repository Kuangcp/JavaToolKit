package time;

import com.myth.time.DateFormatUtils;
import java.util.Date;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by Administrator on 2017/05/09
 * Kuangchengping@outlook.com
 */
@Ignore
public class DateFormatUtilsTest {

  @Test
  public void toYMDHM() {
    String date = DateFormatUtils.toYMDHM(new Date());
    System.out.println(date);
  }

  @Test
  public void toYMDHMS() {
    String date = DateFormatUtils.toYMDHMS(new Date());
    System.out.println(date);
  }
}
