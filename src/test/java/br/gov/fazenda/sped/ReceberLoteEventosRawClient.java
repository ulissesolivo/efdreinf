package br.gov.fazenda.sped;

import efdreinf.adapter.SoapAdapter;

public class ReceberLoteEventosRawClient {

    public static void main(String args[]) throws Exception {

        SoapAdapter soap = new SoapAdapter("http://localhost:8081/ws", "");

        System.out.println(" ### Conectado! ###");

        soap.enviar("<ns2:ReceberLoteEventos xmlns:ns2=\"http://sped.fazenda.gov.br/\"><loteEventos>outro lote de eventos</loteEventos></ns2:ReceberLoteEventos>");

        String resposta = soap.lerResposta();

        System.out.println(resposta);

        soap.desconectar();

    }

}
