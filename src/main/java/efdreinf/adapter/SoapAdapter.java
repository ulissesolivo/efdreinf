package efdreinf.adapter;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import efdreinf.util.InputStreamUtils;

public class SoapAdapter implements IServidorRemotoAdapter {

    private HttpURLConnection connection;

    public SoapAdapter(String url, String soapAction) throws Exception {
        URL uurl = new URL(url);
        connection = HttpURLConnection.class.cast(uurl.openConnection());
        if (soapAction != null && !soapAction.isEmpty()) {
            connection.setRequestProperty("SOAPAction", soapAction);
        }
    }

    public void enviar(String mensagem) throws Exception {
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
    }

    public String lerResposta() throws Exception {
        InputStream respostaStream = connection.getInputStream();
        String resposta = InputStreamUtils.inputStreamToString(respostaStream);
        respostaStream.close();
        return resposta;
    }

    public void desconectar() {
        connection.disconnect();
    }

}
