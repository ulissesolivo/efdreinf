package efdreinf.adapter;

import java.io.InputStream;
import java.util.List;

public interface IBackendAdapter {

    public List<String> getListaIds() throws Exception;

    public InputStream getArquivo(String id) throws Exception;

    public void guardarResposta(String id, String resposta) throws Exception;

}
