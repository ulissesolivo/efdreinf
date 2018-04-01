package br.gov.fazenda.sped;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class ReceberLoteEventosClient {

    public static void main(String args[]) throws Exception {
        URL url = new URL("http://localhost:8081/ws?wsdl");
        QName qname = new QName("http://sped.fazenda.gov.br/", "ReceberLoteEventosServerService");
        Service ws = Service.create(url, qname);

        ReceberLoteEventosService service = ws.getPort(ReceberLoteEventosService.class);

        String result = service.ReceberLoteEventos("algum lote de eventos");
        
		System.out.println("Resposta: " + result);
    }

    /*

POST /ws HTTP/1.1
Accept: text/xml, multipart/related
Content-Type: text/xml; charset=utf-8
SOAPAction: "http://sped.fazenda.gov.br/ReceberLoteEventosService/ReceberLoteEventosRequest"
User-Agent: JAX-WS RI 2.2.9-b130926.1035 svn-revision#5f6196f2b90e9460065a4c2f4e30e065b245e51e
Host: localhost:8081
Connection: keep-alive
Content-Length: 239

<?xml version="1.0" ?><S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/"><S:Body>

<ns2:ReceberLoteEventos xmlns:ns2="http://sped.fazenda.gov.br/"><arg0>algum lote de eventos</arg0></ns2:ReceberLoteEventos>

</S:Body></S:Envelope>
      
     */
}
