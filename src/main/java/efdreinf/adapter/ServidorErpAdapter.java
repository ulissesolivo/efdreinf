package efdreinf.adapter;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import efdreinf.util.InputStreamUtils;
import efdreinf.util.SegurancaUtils;

public class ServidorErpAdapter implements IBackendAdapter {

    @Override
    public List<String> getListaIds() throws Exception {
        String listaEventos = getRespostaHttp(SegurancaUtils.get().getUrlListaEventosNaoEnviadosReinf());
        String[] split = listaEventos.replaceAll("\\{\\}\\[\\]", "").split(";");
        return Arrays.asList(split);
    }

    @Override
    public InputStream getArquivo(String id) throws Exception {
        String url = SegurancaUtils.get().getUrlObterEventoReinf() + "?id=" + id;
        String arquivo = getRespostaHttp(url);
        return InputStreamUtils.stringToInputStream(arquivo);
    }

    @Override
    public void guardarResposta(String id, String conteudo) throws Exception {
        String url = SegurancaUtils.get().getUrlSalvarStatusErp();
        URL uurl = new URL(url);
        HttpURLConnection connection = HttpURLConnection.class.cast(uurl.openConnection());
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

        OutputStream outputStream = connection.getOutputStream();

        BufferedOutputStream out = new BufferedOutputStream(outputStream);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

        writer.append(conteudo);

        writer.flush();
        writer.close();
        out.close();
        outputStream.close();

        connection.disconnect();
    }

    private String getRespostaHttp(String url) throws Exception {
        URL uurl = new URL(url);
        HttpURLConnection connection = HttpURLConnection.class.cast(uurl.openConnection());
        connection.setRequestMethod("GET");
        InputStream respostaStream = connection.getInputStream();
        String resposta = InputStreamUtils.inputStreamToString(respostaStream);
        respostaStream.close();
        connection.disconnect();
        return resposta;
    }
}
