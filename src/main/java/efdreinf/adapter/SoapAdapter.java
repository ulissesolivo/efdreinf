package efdreinf.adapter;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;

import efdreinf.util.InputStreamUtils;
import efdreinf.util.SegurancaUtils;

public class SoapAdapter implements IServidorRemotoAdapter {

    public static final Logger LOGGER = Logger.getLogger(SoapAdapter.class);

    private String url;
    private String soapAction;

    public SoapAdapter(String url, String soapAction) {
        this.url = url;
        this.soapAction = soapAction;
    }

    public String enviar(String mensagem) throws Exception {
        LOGGER.info("Acessando " + url);

        URL uurl = new URL(url);
        HttpURLConnection connection = HttpURLConnection.class.cast(uurl.openConnection());
        if (url.startsWith("https")) {
            HttpsURLConnection httpsURLConnection = HttpsURLConnection.class.cast(connection);
            httpsURLConnection.setSSLSocketFactory(SegurancaUtils.get().getSslSocketFactory());
        }
        if (soapAction != null && !soapAction.isEmpty()) {
            connection.setRequestProperty("SOAPAction", soapAction);
        }

        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

        OutputStream outputStream = connection.getOutputStream();

        BufferedOutputStream out = new BufferedOutputStream(outputStream);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

        writer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        writer.append("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"");
        writer.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        writer.append(" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">");
        writer.append("<soap:Header/><soap:Body>");
        writer.append(mensagem);
        writer.append("</soap:Body></soap:Envelope>");

        writer.flush();
        writer.close();
        out.close();
        outputStream.close();

        InputStream respostaStream = connection.getInputStream();
        String resposta = InputStreamUtils.inputStreamToString(respostaStream);
        respostaStream.close();

        connection.disconnect();

        return resposta;
    }

}
