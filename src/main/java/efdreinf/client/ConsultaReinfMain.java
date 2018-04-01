package efdreinf.client;

import efdreinf.adapter.ArquivoLocalAdapter;
import efdreinf.adapter.SoapAdapter;
import efdreinf.operacao.ConsultarFechamentoReinf;
import efdreinf.util.SegurancaUtils;

public class ConsultaReinfMain {

    public static void main(String args[]) throws Exception {

        SegurancaUtils.get().inicializar();

        SoapAdapter soap = new SoapAdapter(//
                "https://preprodefdreinf.receita.fazenda.gov.br/ConsultasReinf.svc", //
                "http://sped.fazenda.gov.br/ConsultasReinf/ConsultaInformacoesConsolidadas");

        ArquivoLocalAdapter entrada = new ArquivoLocalAdapter("xmlEventosTeste", "xmlResposta");

        new ConsultarFechamentoReinf().processar(entrada, soap);

    }

}
