package efdreinf.adapter;

import java.io.InputStream;
import java.util.List;

public interface IBackendAdapter {

    public List<String> consultarListaIds() throws Exception;

    public InputStream obterArquivo(String id) throws Exception;

    public void guardarStatusRetorno(String id, String resposta) throws Exception;

}
