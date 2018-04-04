package efdreinf.util;

import java.io.ByteArrayInputStream;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class AssinadorDigitalTest {
    
    @Test
    @Ignore //requer PFX real
    public void assinaturaTest() throws Exception {
        SegurancaUtils.get().setClientPfx("");  
        SegurancaUtils.get().setClientAlias("");
        SegurancaUtils.get().setClientPassword("");

        String conteudoXml = //
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" //
                        + "<Reinf xmlns=\"http://www.reinf.esocial.gov.br/schemas/NOME_DO_EVENTO/v01_01_01\">" //
                        + "  <evento id=\"10012017\">" //
                        + "     <evtExemplo></evtExemplo>" //
                        + "  </evento></Reinf>";

        ByteArrayInputStream inArquivoXml = new ByteArrayInputStream(conteudoXml.getBytes());
        
        ArquivoAssinado assinado = new AssinadorDigital().assinar("Reinf", inArquivoXml);

        Assert.assertTrue(assinado.getConteudoXml().contains("Signature"));
        Assert.assertTrue(assinado.getId().contains("10012017"));

        System.out.println(assinado.getConteudoXml());
    }

}
