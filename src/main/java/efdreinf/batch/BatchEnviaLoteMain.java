package efdreinf.batch;

import org.apache.log4j.Logger;

import efdreinf.adapter.ArquivoLocalAdapter;
import efdreinf.adapter.SoapAdapter;
import efdreinf.operacao.EnviarLote;
import efdreinf.util.AssinadorDigital;
import efdreinf.util.SegurancaUtils;

public class BatchEnviaLoteMain {

    public static final Logger LOGGER = Logger.getLogger(BatchEnviaLoteMain.class);
    
    public static void main(String args[]) throws Exception {
        
        LOGGER.info("Iniciando sincronizacao EFD Reinf x ERP em modo Batch");

        SegurancaUtils.get().inicializarCertificados();
        
        SoapAdapter soap = new SoapAdapter(//
                SegurancaUtils.get().getUrlServicoReinf(), //
                "http://sped.fazenda.gov.br/RecepcaoLoteReinf/ReceberLoteEventos");

        ArquivoLocalAdapter entrada = new ArquivoLocalAdapter("xmlEventosTeste", "xmlResposta");

        AssinadorDigital assinadorDigital = new AssinadorDigital();
        
        EnviarLote enviarLote = new EnviarLote(assinadorDigital);

        enviarLote.processar(entrada, soap);

        LOGGER.info("Processamento concluido");

    }

}
