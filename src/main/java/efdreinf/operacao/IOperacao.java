package efdreinf.operacao;

import efdreinf.adapter.IServidorRemotoAdapter;
import efdreinf.adapter.IBackendAdapter;

public interface IOperacao {

    public void processar(IBackendAdapter backend, IServidorRemotoAdapter destino) throws Exception;

}
