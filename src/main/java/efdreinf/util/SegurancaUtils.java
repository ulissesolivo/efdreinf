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

public class SegurancaUtils {

    private static SegurancaUtils instance;

    private SSLSocketFactory sslSocketFactory = null;
    private KeyStore keyStore;
    
    private String clientPfx;
    private String clientPassword;
    private String clientAlias;
    private String erpLogin;
    private String erpSenha;
    private String urlListaEventosNaoEnviadosReinf;
    private String urlObterEventoReinf;
    private String urlSalvarStatusErp;
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
        Properties props = new Properties();
        props.load(new FileInputStream("configuracoes.properties"));

        clientPfx = props.getProperty("efdreinf.clientPfx");
        clientPassword = props.getProperty("efdreinf.clientPassword");
        clientAlias = props.getProperty("efdreinf.clientAlias");

        urlListaEventosNaoEnviadosReinf = props.getProperty("efdreinf.urlListaEventosNaoEnviadosReinf");
        urlObterEventoReinf = props.getProperty("efdreinf.urlObterEventoReinf");
        urlSalvarStatusErp = props.getProperty("efdreinf.urlSalvarStatusErp");
        erpLogin = props.getProperty("efdreinf.erpLogin");
        erpSenha = props.getProperty("efdreinf.erpSenha");
        
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
        if (clientPfx == null || clientAlias == null || clientPassword == null) {
            throw new UnsupportedOperationException("Dados do certificado digital nao informados!");
        }

        char[] senha = clientPassword.toCharArray();
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        keyStore = KeyStore.getInstance("PKCS12");

        try {
            keyStore.load(new FileInputStream(clientPfx), senha);
        } catch (IOException e) {
            throw new Exception("Senha do Certificado Digital incorreta ou Certificado inválido.");
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, senha);

        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(null, null);

        File[] arquivosCerts = new File("cacerts").listFiles();
        for (int i = 0; i < arquivosCerts.length; i++) {
            File file = arquivosCerts[i];
            trustStore.setCertificateEntry(String.valueOf(i), cf.generateCertificate(new FileInputStream(file)));
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        SSLContext sslContext = null;
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

        sslSocketFactory = sslContext.getSocketFactory();
    }

    public KeyStore getKeyStore() throws Exception {
        return keyStore;
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

    public String getUrlListaEventosNaoEnviadosReinf() {
        return urlListaEventosNaoEnviadosReinf;
    }

    public String getUrlObterEventoReinf() {
        return urlObterEventoReinf;
    }

    public String getUrlSalvarStatusErp() {
        return urlSalvarStatusErp;
    }

    public String getErpLogin() {
        return erpLogin;
    }

    public String getErpSenha() {
        return erpSenha;
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

    public void setERPLogin(String text) {
        this.erpLogin = text;
    }

    public void setERPSenha(String text) {
        this.erpSenha = text;
    }

    public String getERPLogin() {
        return erpLogin;
    }

    public String getERPSenha() {
        return erpSenha;
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

}
