package efdreinf.adapter;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
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
        String listaEventos = getRespostaHttp(SegurancaUtils.get().getUrlServicoErp(), "L", "");
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
        String arquivo = getRespostaHttp(url, "C", id);
        return InputStreamUtils.stringToInputStream(arquivo);
    }

    @Override
    public void guardarStatusRetorno(String id, String retorno) throws Exception {
        LOGGER.info("Gravando arquivo no ERP: " + id);
        
        HttpURLConnection connection = abreConexao(SegurancaUtils.get().getUrlServicoErp());

        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        connection.setRequestProperty("op", "A");
        connection.setRequestProperty("id", id);

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

    protected String getRespostaHttp(String url, String op, String id) throws Exception {
        HttpURLConnection connection = abreConexao(url);
        connection.setRequestMethod("GET");
        InputStream respostaStream = connection.getInputStream();
        String resposta = InputStreamUtils.inputStreamToString(respostaStream);
        respostaStream.close();
        connection.disconnect();
        connection.setRequestProperty("op", op);
        connection.setRequestProperty("id", id);
        return resposta;
    }

    protected HttpURLConnection abreConexao(String url) throws Exception {
        URL uurl = new URL(url);
        HttpURLConnection connection = HttpURLConnection.class.cast(uurl.openConnection());
        byte[] login = SegurancaUtils.get().getErpLogin().getBytes();
        String authLogin = "Basic " + new String(Base64.getEncoder().encode(login));
        connection.setRequestProperty("Authorization", authLogin);
        return connection;
    }

}
