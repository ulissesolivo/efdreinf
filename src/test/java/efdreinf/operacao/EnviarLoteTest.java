package efdreinf.operacao;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import efdreinf.adapter.ArquivoLocalAdapter;
import efdreinf.adapter.IBackendAdapter;
import efdreinf.adapter.IServidorRemotoAdapter;
import efdreinf.util.ArquivoAssinado;
import efdreinf.util.AssinadorDigital;
import efdreinf.util.InputStreamUtils;

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

        EnviarLote enviarLote = new EnviarLote(new AssinadorTeste());
        
        enviarLote.processar(entrada, destino);
        Assert.assertTrue(mensagem.contains("Reinf"));
        System.out.println(mensagem);
    }
    
    private class AssinadorTeste extends AssinadorDigital {        
        public AssinadorTeste() throws Exception {
            super();
        }
        
        @Override
        protected void preparaCertificado() throws Exception {
            // Auto-generated method stub
        }

        @Override
        public ArquivoAssinado assinar(String id, InputStream inArquivoXml) throws Exception {
            return new ArquivoAssinado(id, InputStreamUtils.inputStreamToString(inArquivoXml));
        }
    }

}
