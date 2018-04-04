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
    
    public ServidorErpAdapterTest() throws Exception {
        SegurancaUtils.get().setServicoErp("http://localhost/test?c=1");
    }

    @Test
    public void consultarJsonListaIdsTest() throws Exception {
        resposta = "{\"listaIds\":[\"ID001\",\"ID002\"]}";
        List<String> lista = adapter.consultarListaIds();
        Assert.assertEquals("[ID001, ID002]", lista.toString());
        Assert.assertEquals("L", stubHttpConnection.getRequestProperty("op"));        
    }

    @Test
    public void receberArquivoTest() throws Exception {
        resposta = "<xml>teste</xml>";
        InputStream streamResp = adapter.obterArquivo("ID001");
        String string = InputStreamUtils.inputStreamToString(streamResp);
        Assert.assertEquals("<xml>teste</xml>", string);
        Assert.assertEquals("C", stubHttpConnection.getRequestProperty("op"));        
        Assert.assertEquals("ID001", stubHttpConnection.getRequestProperty("id"));        
    }

    @Test
    public void atualizarItem() throws Exception {
        resposta = "<xml>teste</xml>";
        adapter.guardarStatusRetorno("ID001", "<xml>retorno</xml>");
        Assert.assertEquals("A", stubHttpConnection.getRequestProperty("op"));        
        Assert.assertEquals("ID001", stubHttpConnection.getRequestProperty("id"));        
    }

    private class TestServAdapter extends ServidorErpAdapter {

        @Override
        protected HttpURLConnection abreConexao(String url) throws MalformedURLException, IOException, Exception {
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
