package br.gov.fazenda.sped;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface ReceberLoteEventosService {
    
    @WebMethod
    public String ReceberLoteEventos(@WebParam(name = "loteEventos") String loteEventos); 

}
