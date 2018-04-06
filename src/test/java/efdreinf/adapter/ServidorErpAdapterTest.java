package efdreinf.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import efdreinf.util.InputStreamUtils;
import efdreinf.util.SegurancaUtils;

public class ServidorErpAdapterTest {

    private StubHttpConnection stubHttpConnection;
    private String resposta;
    private TestServAdapter adapter = new TestServAdapter();
    private String url;
    
    public ServidorErpAdapterTest() {
        SegurancaUtils.get().setServicoErp("http://test?c=1");
    }

    @Test
    public void consultarJsonListaIdsTest() throws Exception {
        resposta = "{\"listaIds\":[\"ID001\",\"ID002\"]}";
        List<String> lista = adapter.consultarListaIds();
        Assert.assertEquals("[ID001, ID002]", lista.toString());
        Assert.assertEquals("http://test?c=1&op=L&id=1", url);        
    }

    @Test
    public void consultarJsonListaVaziaTest() throws Exception {
        resposta = "{\"listaIds\":[]}";
        List<String> lista = adapter.consultarListaIds();
        Assert.assertTrue(lista.isEmpty());
        Assert.assertEquals("http://test?c=1&op=L&id=1", url);        
    }
    
    @Test
    public void consultarJsonListaUnicaTest() throws Exception {
        resposta = "{\"listaIds\":[\"ID001\"]}";
        List<String> lista = adapter.consultarListaIds();
        Assert.assertEquals("[ID001]", lista.toString());
        Assert.assertEquals("http://test?c=1&op=L&id=1", url);        
    }
    
    @Test
    public void receberArquivoTest() throws Exception {
        resposta = "<xml>teste</xml>";
        InputStream streamResp = adapter.obterArquivo("ID001");
        String string = InputStreamUtils.inputStreamToString(streamResp);
        Assert.assertEquals("<xml>teste</xml>", string);
        Assert.assertEquals("http://test?c=1&op=C&id=ID001", url);        
    }

    @Test
    public void atualizarItem() throws Exception {
        resposta = "<xml>teste</xml>";
        adapter.guardarStatusRetorno("ID001", "<xml>retorno</xml>");
        Assert.assertEquals("http://test?c=1&op=A&id=ID001", url);
    }

    private class TestServAdapter extends ServidorErpAdapter {

        @Override
        protected HttpURLConnection abreConexao(String url) throws MalformedURLException, IOException, Exception {
            ServidorErpAdapterTest.this.url = url;
            stubHttpConnection = new StubHttpConnection(new URL(url));
            return stubHttpConnection;
        }

    }

    private class StubHttpConnection extends HttpURLConnection {

        protected StubHttpConnection(URL u) throws MalformedURLException {
            super(u);
        }

        @Override
        public void disconnect() {
            // Auto-generated method stub
        }

        @Override
        public boolean usingProxy() {
            // Auto-generated method stub
            return false;
        }

        @Override
        public void connect() throws IOException {
            // Auto-generated method stub
        }
        
        @Override
        public InputStream getInputStream() throws IOException {
            return InputStreamUtils.stringToInputStream(resposta);
        }
        
        @Override
        public OutputStream getOutputStream() throws IOException {
            return new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    // Auto-generated method stub
                }
            };
        }

    }

}
