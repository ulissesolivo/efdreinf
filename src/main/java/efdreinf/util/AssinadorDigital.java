package efdreinf.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class AssinadorDigital {

    private KeyStore.PrivateKeyEntry keyEntry;

    public AssinadorDigital() throws Exception {
        KeyStore ks = SegurancaUtils.get().getKeyStore();
        String clientAlias = SegurancaUtils.get().getClientAlias();
        char[] senha = SegurancaUtils.get().getClientPassword().toCharArray();
        keyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(clientAlias, new KeyStore.PasswordProtection(senha));
    }

    public ArquivoAssinado assinar(String tagName, InputStream inArquivoXml) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        String id = assinaDigitalmente(tagName, inArquivoXml, os);
        String conteudoXml = new String(os.toByteArray())//
                .replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>", "");

        return new ArquivoAssinado(id, conteudoXml);
    }

    /**
     * fonte:
     * https://crestaniblog.wordpress.com/2013/03/27/assinatura-digital-em-xml/
     */
    private String assinaDigitalmente(String tagName, InputStream in, OutputStream os) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = dbf.newDocumentBuilder().parse(in);

        NodeList nodeList = doc.getElementsByTagName(tagName);
        Element rootItem = Element.class.cast(nodeList.item(0));

        String elementoID = null;
        Element childNode = null;

        // iterando nos nos filhos para encontrar o nó evento
        // só deve haver um no XML original
        NodeList childNodes = rootItem.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (Element.class.isInstance(childNodes.item(i))) {
                childNode = Element.class.cast(childNodes.item(i));
                elementoID = childNode.getAttribute("id");
                if (elementoID != null) {
                    break;
                }
            }
        }

        if (elementoID == null || elementoID.isEmpty()) {
            throw new NoSuchFieldException("Campo ID não encontrado na tag do Evento!");
        }

        // faltou esse flag
        childNode.setIdAttribute("id", true);

        DOMSignContext dsc = new DOMSignContext(keyEntry.getPrivateKey(), rootItem);

        // Assembling the XML Signature
        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

        List<Transform> transforms = new ArrayList<Transform>();
        transforms.add(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null));
        transforms.add(fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null));

        Reference ref = fac.newReference("#" + elementoID, //
                fac.newDigestMethod(DigestMethod.SHA256, null), //
                transforms, null, null);

        SignedInfo si = fac.newSignedInfo(//
                fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, //
                        (C14NMethodParameterSpec) null), //
                fac.newSignatureMethod("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256", null), //
                Collections.singletonList(ref));

        KeyInfoFactory kif = fac.getKeyInfoFactory();

        List<Certificate> x509Content = new ArrayList<Certificate>();
        x509Content.add(keyEntry.getCertificate());

        X509Data kv = kif.newX509Data(x509Content);
        KeyInfo ki = kif.newKeyInfo(Collections.singletonList(kv));
        XMLSignature signature = fac.newXMLSignature(si, ki);

        signature.sign(dsc);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();

        // salva resultado no arquivo de saída
        trans.transform(new DOMSource(doc), new StreamResult(os));

        return elementoID;
    }
}
