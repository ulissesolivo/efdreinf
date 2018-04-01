package efdreinf.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class InputStreamUtils {

    public static String inputStreamToString(InputStream is) throws Exception {
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while (result != -1) {
            buf.write((byte) result);
            result = bis.read();
        }
        String resultado = buf.toString("UTF-8");
        buf.close();
        bis.close();
        return resultado;
    }

    public static InputStream stringToInputStream(String conteudo) {
        return new ByteArrayInputStream(conteudo.getBytes());
    }

}
