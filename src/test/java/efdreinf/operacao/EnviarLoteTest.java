package efdreinf.operacao;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import efdreinf.adapter.IBackendAdapter;
import efdreinf.adapter.IServidorRemotoAdapter;
import efdreinf.util.ArquivoAssinado;
import efdreinf.util.AssinadorDigital;
import efdreinf.util.InputStreamUtils;

public class EnviarLoteTest {

    private String mensagem;

    @Test
    public void montarMensagemTest() throws Exception {

        IBackendAdapter entrada = new StubAdapter();

        IServidorRemotoAdapter destino = new IServidorRemotoAdapter() {
            @Override
            public String enviar(String montarMensagem) throws Exception {
                mensagem = montarMensagem;
                return "#";
            }
        };

        EnviarLote enviarLote = new EnviarLote(new AssinadorTeste());

        enviarLote.processar(entrada, destino);
        Assert.assertTrue(mensagem.contains("Reinf"));
        Assert.assertTrue(mensagem.startsWith("<ReceberLoteEventos"));
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

    private class StubAdapter implements IBackendAdapter {
        @Override
        public List<String> consultarListaIds() throws Exception {
            return Arrays.asList("ID001");
        }

        @Override
        public InputStream obterArquivo(String id) throws Exception {
            return new FileInputStream("xmlEventosTeste/ID1123456789012342017112320300100001.xml");
        }

        @Override
        public void guardarStatusRetorno(String id, String resposta) throws Exception {
            // Auto-generated method stub
        }
    }

}
