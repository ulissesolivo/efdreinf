package efdreinf.util;

import java.io.FileInputStream;
import java.util.Properties;

public class SegurancaUtils {

    private static SegurancaUtils instance;
    private String clientPfx;
    private String clientPassword;
    private String clientAlias;

    public static SegurancaUtils get() {
        if (instance == null) {
            instance = new SegurancaUtils();
            instance.inicializar();
        }
        return instance;
    }

    public SegurancaUtils() {
        Properties props = new Properties();

        try {
            props.load(new FileInputStream("certificados/certificados.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        clientPfx = props.getProperty("efdreinf.clientPfx");
        clientPassword = props.getProperty("efdreinf.clientPassword");
        clientAlias = props.getProperty("efdreinf.clientAlias");

        if ("true".equals(props.getProperty("efdreinf.log_ssl"))) {
            System.setProperty("javax.net.debug", "ssl");
        }

        System.setProperty("javax.net.ssl.keyStoreType", "jks");
        System.setProperty("javax.net.ssl.keyStore", props.getProperty("efdreinf.keyStore"));
        System.setProperty("javax.net.ssl.keyStorePassword", props.getProperty("efdreinf.keyStorePassword"));

        System.setProperty("javax.net.ssl.trustStoreType", "jks");
        System.setProperty("javax.net.ssl.trustStore", props.getProperty("efdreinf.trustStore"));
        System.setProperty("javax.net.ssl.trustStorePassword", props.getProperty("efdreinf.trustStorePassword"));
    }

    public void inicializar() {
        if (clientPfx == null || clientAlias == null || clientPassword == null) {
            throw new UnsupportedOperationException("Certificados nao carregados!");
        }
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
        return null;
    }

    public String getUrlObterEventoReinf() {
        return null;
    }

    public String getUrlSalvarStatusErp() {
        return null;
    }

}
