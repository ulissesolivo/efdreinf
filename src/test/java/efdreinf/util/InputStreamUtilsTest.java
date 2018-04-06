package efdreinf.util;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import efdreinf.util.InputStreamUtils;

public class InputStreamUtilsTest {

    @Test
    public void inputStreamToStringTest() throws Exception {
        InputStream inputStream = InputStreamUtils.stringToInputStream("<ola-teste Marcos!/>");
        String outraString = InputStreamUtils.inputStreamToString(inputStream);
        Assert.assertEquals("<ola-teste Marcos!/>", outraString);
    }

}
