package efdreinf.operacao;

import efdreinf.adapter.IBackendAdapter;
import efdreinf.adapter.IServidorRemotoAdapter;

public class ConsultarFechamentoReinf implements IOperacao {

    @Override
    public void processar(IBackendAdapter entrada, IServidorRemotoAdapter destino) throws Exception {

        String mensagem = montarMensagem(entrada);

        String resposta = destino.enviar(mensagem);

        entrada.guardarStatusRetorno("", resposta);
    }

    public static String montarMensagem(IBackendAdapter entrada) throws Exception {

        StringBuilder sb = new StringBuilder();

        sb.append("<ConsultaInformacoesConsolidadas xmlns=\"http://sped.fazenda.gov.br/\">");
        sb.append("<loteEventos>");

        sb.append("<tipoInscricaoContribuinte>" + "</tipoInscricaoContribuinte>");
        sb.append("<numeroInscricaoContribuinte>" + "</numeroInscricaoContribuinte>");

        // Número do Protocolo do Fechamento recebido no retorno do evento
        // R-2099.
        sb.append("<numeroReciboFechamento>" + "</numeroReciboFechamento>");

        sb.append("</loteEventos>");
        sb.append("</ReceberLoteEventos>");

        return sb.toString();
    }

}
