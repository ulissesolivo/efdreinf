package efdreinf.operacao;

import org.junit.Assert;
import org.junit.Test;

import efdreinf.adapter.IServidorRemotoAdapter;
import efdreinf.adapter.ArquivoLocalAdapter;
import efdreinf.adapter.IBackendAdapter;
import efdreinf.operacao.EnviarLote;

public class EnviarLoteTest {

    private String mensagem;

    @Test
    public void montarMensagemTest() throws Exception {

        IBackendAdapter entrada = new ArquivoLocalAdapter("xmlEventosTeste", "xmlResposta");

        IServidorRemotoAdapter destino = new IServidorRemotoAdapter() {
            @Override
            public String lerResposta() throws Exception {
                return "";
            }

            @Override
            public void enviar(String montarMensagem) throws Exception {
                mensagem = montarMensagem;
            }

            @Override
            public void desconectar() {
                // Auto-generated method stub
            }
        };

        new EnviarLote().processar(entrada, destino);
        Assert.assertTrue(mensagem.contains("Reinf"));
        Assert.assertTrue(mensagem.contains("Signature"));
        System.out.println(mensagem);
    }

}
