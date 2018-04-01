package efdreinf.util;

public class ArquivoAssinado {
    private String id;
    private String conteudoXml;

    public ArquivoAssinado(String id, String conteudoXml) {
        this.id = id;
        this.conteudoXml = conteudoXml;
    }

    public String getId() {
        return id;
    }

    public String getConteudoXml() {
        return conteudoXml;
    }
}
