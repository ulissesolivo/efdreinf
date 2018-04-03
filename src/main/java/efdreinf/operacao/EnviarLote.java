package efdreinf.operacao;

import java.io.InputStream;
import java.util.List;

import efdreinf.adapter.IBackendAdapter;
import efdreinf.adapter.IServidorRemotoAdapter;
import efdreinf.util.ArquivoAssinado;
import efdreinf.util.AssinadorDigital;

public class EnviarLote implements IOperacao {

    @Override
    public void processar(IBackendAdapter backend, IServidorRemotoAdapter destino) throws Exception {

        List<String> listaIds = backend.getListaIds();
        
        for (String id : listaIds) {
            
            InputStream arquivo = backend.getArquivo(id);

            String mensagem = montarMensagem(arquivo);

            destino.enviar(mensagem);

            String resposta = destino.lerResposta();

            destino.desconectar();
            
            backend.guardarResposta(id, resposta);

        }
        
    }

    public static String montarMensagem(InputStream arquivo) throws Exception {

        AssinadorDigital assinador = new AssinadorDigital();

        StringBuilder sb = new StringBuilder();

        sb.append("<ReceberLoteEventos xmlns=\"http://sped.fazenda.gov.br/\">");
        sb.append("<loteEventos>");

        sb.append("<Reinf xmlns=\"http://www.reinf.esocial.gov.br/schemas/envioLoteEventos/v1_03_00\">");
        sb.append("<loteEventos>");

        // List<String> listaIds = entrada.getListaIds();

        // for (String id : listaIds) {
        // InputStream arquivo = entrada.getArquivo(id);
        ArquivoAssinado arqAssinado = assinador.assinar("Reinf", arquivo);

        sb.append("<evento id=\"" + arqAssinado.getId() + "\">");
        sb.append(arqAssinado.getConteudoXml());
        sb.append("</evento>");

        arquivo.close();
        // }

        sb.append("</loteEventos>");
        sb.append("</Reinf>");

        sb.append("</loteEventos>");
        sb.append("</ReceberLoteEventos>");

        return sb.toString();
    }

}
