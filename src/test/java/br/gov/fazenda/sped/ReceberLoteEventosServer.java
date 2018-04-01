package br.gov.fazenda.sped;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(endpointInterface = "br.gov.fazenda.sped.ReceberLoteEventosService")
public class ReceberLoteEventosServer implements ReceberLoteEventosService {

    @WebMethod
    @Override
    public String ReceberLoteEventos(String arg0) {
        System.out.println("Mensagem recebida!");
        System.out.println(arg0);
        return "######";
    }

}
