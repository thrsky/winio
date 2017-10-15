import com.shuli.winio.WinioUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WinioTest {

    private static final Logger logger = LoggerFactory.getLogger(WinioTest.class);

    @Test
    public void testInit(){
        try{
            WinioUtils.enter("test");
        }catch (Exception e){
            logger.error("init winioUtils error"+e.getMessage());
        }
    }
}
