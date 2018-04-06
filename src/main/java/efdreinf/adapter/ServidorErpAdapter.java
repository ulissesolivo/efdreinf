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
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import efdreinf.util.InputStreamUtils;
import efdreinf.util.SegurancaUtils;

public class ServidorErpAdapter implements IBackendAdapter {

    public static final Logger LOGGER = Logger.getLogger(ServidorErpAdapter.class);

    @Override
    public List<String> consultarListaIds() throws Exception {
        LOGGER.info("Obtendo lista IDs do ERP...");
        String url = getUrlServicoErp("L", "1");
        String listaEventos = getRespostaHttp(url);
        String jsontratado = listaEventos.substring(listaEventos.indexOf("["), listaEventos.indexOf("]")) //
                .replaceAll("[^A-Z\\d,]", "");
        if (jsontratado.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(jsontratado.split(","));
    }

    @Override
    public InputStream obterArquivo(String id) throws Exception {
        LOGGER.info("Baixando arquivo do ERP: " + id);
        String url = getUrlServicoErp("C", id);
        String arquivo = getRespostaHttp(url);
        return InputStreamUtils.stringToInputStream(arquivo);
    }

    @Override
    public void guardarStatusRetorno(String id, String retorno) throws Exception {
        LOGGER.info("Gravando arquivo no ERP: " + id);
        String url = getUrlServicoErp("A", id);

        try {
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

            InputStream respostaStream = connection.getInputStream();
            respostaStream.close();

            connection.disconnect();
        } catch (Exception e) {
            throw new Exception("Erro ao conectar servidor ERP " + url, e);
        }
    }

    protected String getRespostaHttp(String url) throws Exception {
        String resposta;
        try {
            HttpURLConnection connection = abreConexao(url);
            connection.setRequestMethod("GET");
            InputStream respostaStream = connection.getInputStream();
            resposta = InputStreamUtils.inputStreamToString(respostaStream);
            respostaStream.close();
            connection.disconnect();
        } catch (Exception e) {
            throw new Exception("Erro ao conectar servidor ERP " + url, e);
        }
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

    private String getUrlServicoErp(String op, String id) {
        StringBuilder sb = new StringBuilder();
        sb.append(SegurancaUtils.get().getUrlServicoErp());
        if (SegurancaUtils.get().getUrlServicoErp().contains("?")) {
            sb.append("&");
        } else {
            sb.append("?");
        }
        sb.append("op=");
        sb.append(op);
        sb.append("&id=");
        sb.append(id);
        return sb.toString();
    }

}
