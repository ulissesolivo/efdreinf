package efdreinf.client;

import efdreinf.adapter.ArquivoLocalAdapter;
import efdreinf.adapter.SoapAdapter;
import efdreinf.operacao.EnviarLote;
import efdreinf.util.SegurancaUtils;

public class EnviaLoteMain {

    public static void main(String args[]) throws Exception {

        SegurancaUtils.get().inicializar();

        SoapAdapter soap = new SoapAdapter(//
                "https://preprodefdreinf.receita.fazenda.gov.br/RecepcaoLoteReinf.svc", //
                "http://sped.fazenda.gov.br/RecepcaoLoteReinf/ReceberLoteEventos");

        ArquivoLocalAdapter entrada = new ArquivoLocalAdapter("xmlEventosTeste", "xmlResposta");

        EnviarLote enviarLote = new EnviarLote();
        
        enviarLote.processar(entrada, soap);
    }

}
