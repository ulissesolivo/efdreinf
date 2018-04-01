package efdreinf.adapter;

public interface IServidorRemotoAdapter {

    public void enviar(String conteudo) throws Exception;

    public String lerResposta() throws Exception;

    public void desconectar();

}
