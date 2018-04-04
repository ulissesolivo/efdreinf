package efdreinf.adapter;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.apache.log4j.Logger;

import efdreinf.util.InputStreamUtils;
import efdreinf.util.SegurancaUtils;

public class ServidorErpAdapter implements IBackendAdapter {

    public static final Logger LOGGER = Logger.getLogger(ServidorErpAdapter.class);

    @Override
    public List<String> consultarListaIds() throws Exception {
        LOGGER.info("Obtendo lista IDs do ERP...");
        String url = SegurancaUtils.get().getUrlServicoErp() + "&op=L";
        String listaEventos = getRespostaHttp(url);
        String[] split = //
                listaEventos.substring(listaEventos.indexOf("["), listaEventos.indexOf("]")) //
                .replaceAll("[^A-Z\\d,]", "") //
                .split(",");
        return Arrays.asList(split);
    }

    @Override
    public InputStream obterArquivo(String id) throws Exception {
        LOGGER.info("Baixando arquivo do ERP: " + id);
        String url = SegurancaUtils.get().getUrlServicoErp() + "&op=C&id=" + id;
        String arquivo = getRespostaHttp(url);
        return InputStreamUtils.stringToInputStream(arquivo);
    }

    @Override
    public void guardarStatusRetorno(String id, String retorno) throws Exception {
        LOGGER.info("Gravando arquivo no ERP: " + id);
        
        String url = SegurancaUtils.get().getUrlServicoErp() + "&op=A&id=" + id;

        HttpURLConnection connection = abreConexao(url);

        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

        OutputStream outputStream = connection.getOutputStream();

        BufferedOutputStream out = new BufferedOutputStream(outputStream);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

        writer.append(retorno);

        writer.flush();
        writer.close();
        out.close();
        outputStream.close();

        connection.disconnect();
    }

    protected String getRespostaHttp(String url) throws Exception {
        HttpURLConnection connection = abreConexao(url);
        connection.setRequestMethod("GET");
        InputStream respostaStream = connection.getInputStream();
        String resposta = InputStreamUtils.inputStreamToString(respostaStream);
        respostaStream.close();
        connection.disconnect();
        return resposta;
    }

    protected HttpURLConnection abreConexao(String url) throws MalformedURLException, IOException, Exception {
        URL uurl = new URL(url);
        HttpURLConnection connection = HttpURLConnection.class.cast(uurl.openConnection());
        byte[] login = SegurancaUtils.get().getErpLogin().getBytes();
        String authLogin = "Basic " + new String(Base64.getEncoder().encode(login));
        connection.setRequestProperty("Authorization", authLogin);
        return connection;
    }

}
