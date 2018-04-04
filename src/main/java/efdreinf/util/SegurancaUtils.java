package efdreinf.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.Properties;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.log4j.Logger;

public class SegurancaUtils {
    
    public static final Logger LOGGER = Logger.getLogger(SegurancaUtils.class);

    private static SegurancaUtils instance;

    private SSLSocketFactory sslSocketFactory = null;
    private KeyStore keyStore;

    private String urlServicoReinf;
    private String clientPfx;
    private String clientPassword;
    private String clientAlias;
    private String erpLoginSenha;
    private String urlServicoErp;
    private String modoIntegracao;

    private String pastaEnvioEventos;

    private String pastaLogs;

    public static SegurancaUtils get() throws Exception {
        if (instance == null) {
            instance = new SegurancaUtils();
        }
        return instance;
    }

    public SegurancaUtils() throws Exception {
        LOGGER.info("Lendo configuracoes.properties...");
        
        Properties props = new Properties();
        props.load(new FileInputStream("configuracoes.properties"));
        
        urlServicoReinf = props.getProperty("efdreinf.urlServicoReinf");

        clientPfx = props.getProperty("efdreinf.clientPfx");
        clientPassword = props.getProperty("efdreinf.clientPassword");
        clientAlias = props.getProperty("efdreinf.clientAlias");

        urlServicoErp = props.getProperty("efdreinf.urlServicoERP");
        erpLoginSenha = props.getProperty("efdreinf.erpLoginSenha");

        modoIntegracao = props.getProperty("efdreinf.modoIntegracao");

        pastaEnvioEventos = props.getProperty("efdreinf.pastaEnvioEventos");
        pastaLogs = props.getProperty("efdreinf.pastaLogs");

        if ("true".equals(props.getProperty("efdreinf.log_ssl"))) {
            System.setProperty("javax.net.debug", "ssl");
        }

    }

    public void inicializarCertificados() throws Exception {
        if (sslSocketFactory != null) {
            return;
        }
        
        LOGGER.info("Inicializando certificados digitais...");
        
        if (clientPfx == null || clientAlias == null || clientPassword == null) {
            throw new UnsupportedOperationException("Dados do certificado digital nao informados!");
        }

        char[] senha = clientPassword.toCharArray();
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        keyStore = KeyStore.getInstance("PKCS12");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

        try {
            LOGGER.info("Certificado cliente: " + clientPfx);
            keyStore.load(new FileInputStream(clientPfx), senha);
            kmf.init(keyStore, senha);
        } catch (IOException e) {
            throw new Exception("Senha do Certificado Digital incorreta ou Certificado inválido.");
        }

        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(null, null);

        File[] arquivosCerts = new File("cacerts").listFiles();

        for (int i = 0; i < arquivosCerts.length; i++) {
            File file = arquivosCerts[i];
            LOGGER.info("Certificado servidor: " + file.getName());
            trustStore.setCertificateEntry(String.valueOf(i), cf.generateCertificate(new FileInputStream(file)));
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

        sslSocketFactory = sslContext.getSocketFactory();
    }

    public KeyStore getKeyStore() throws Exception {
        return keyStore;
    }
    
    public String getUrlServicoReinf() {
        return urlServicoReinf;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public String getClientPfx() {
        return clientPfx;
    }

    public String getClientPassword() {
        return clientPassword;
    }

    public String getClientAlias() {
        return clientAlias;
    }

    public String getUrlServicoErp() {
        return urlServicoErp;
    }

    public String getErpLogin() {
        return erpLoginSenha;
    }

    public void setClientAlias(String text) {
        this.clientAlias = text;
    }

    public void setClientPassword(String text) {
        this.clientPassword = text;
    }

    public void setClientPfx(String text) {
        this.clientPfx = text;
    }

    public void setERPLoginSenha(String text) {
        this.erpLoginSenha = text;
    }

    public String getERPLoginSenha() {
        return erpLoginSenha;
    }

    public String getModoIntegracao() {
        return modoIntegracao;
    }

    public String getPastaEnvioEventos() {
        return pastaEnvioEventos;
    }

    public String getPastaLogs() {
        return pastaLogs;
    }

    public void setServicoErp(String string) {
        this.urlServicoErp = string;
    }
    
    public void setUrlServicoReinf(String urlServicoReinf) {
        this.urlServicoReinf = urlServicoReinf;
    }

}
