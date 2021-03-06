package efdreinf.batch;

import org.apache.log4j.Logger;

import efdreinf.adapter.ArquivoLocalAdapter;
import efdreinf.adapter.IBackendAdapter;
import efdreinf.adapter.ServidorErpAdapter;
import efdreinf.adapter.SoapAdapter;
import efdreinf.operacao.EnviarLote;
import efdreinf.util.AssinadorDigital;
import efdreinf.util.LogUtil;
import efdreinf.util.SegurancaUtils;

public class BatchEnviaLoteMain {

    public static final Logger LOGGER = Logger.getLogger(BatchEnviaLoteMain.class);

    public static void main(String args[]) throws Exception {
        new BatchEnviaLoteMain().processar();
    }

    public void processar() throws Exception {

        LogUtil.inicializaConfiguracaoLog();

        LOGGER.info("Iniciando sincronizacao EFD Reinf x ERP...");

        SegurancaUtils.get().inicializarCertificados();

        SoapAdapter soap = new SoapAdapter(//
                SegurancaUtils.get().getUrlServicoReinf(), //
                "http://sped.fazenda.gov.br/RecepcaoLoteReinf/ReceberLoteEventos");

        IBackendAdapter entrada;
        if ("ERP".equals(SegurancaUtils.get().getModoIntegracao())) {
            entrada = new ServidorErpAdapter();
        } else {
            entrada = new ArquivoLocalAdapter();
        }

        AssinadorDigital assinador = new AssinadorDigital();

        EnviarLote enviarLote = new EnviarLote(assinador);

        enviarLote.processar(entrada, soap);

        LOGGER.info("Processamento concluido");
    }

}
