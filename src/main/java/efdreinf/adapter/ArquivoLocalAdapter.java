package efdreinf.adapter;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import efdreinf.util.SegurancaUtils;

public class ArquivoLocalAdapter implements IBackendAdapter {

    public static final Logger LOGGER = Logger.getLogger(ArquivoLocalAdapter.class);

    private String pastaDestino;
    private String pastaOrigem;

    public ArquivoLocalAdapter(String pastaOrigem, String pastaDestino) {
        this.pastaOrigem = pastaOrigem;
        this.pastaDestino = pastaDestino;
    }

    public ArquivoLocalAdapter() throws Exception {
        this.pastaOrigem = SegurancaUtils.get().getPastaEnvioEventos();
        this.pastaDestino = SegurancaUtils.get().getPastaLogs();
    }

    @Override
    public List<String> consultarListaIds() {
        LOGGER.info("Buscando arquivos XML na pasta " + pastaOrigem);
        File[] arquivosEventos = new File(pastaOrigem).listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".xml");
            }
        });

        List<String> ids = new ArrayList<>();
        for (File file : arquivosEventos) {
            ids.add(file.getName().replaceAll(".xml", ""));
        }

        LOGGER.info("Arquivos encontrados: " + ids.size());

        return ids;
    }

    @Override
    public InputStream obterArquivo(String id) throws Exception {
        File file = new File(pastaOrigem + "/" + id + ".xml");
        LOGGER.info("Lendo arquivo: " + file.getName());
        FileInputStream fileInputStream = new FileInputStream(file);
        return fileInputStream;
    }

    @Override
    public void guardarStatusRetorno(String id, String conteudo) throws Exception {
        String filename = pastaDestino + "/" + id + ".xml";
        LOGGER.info("Salvando arquivo: " + filename);
        FileOutputStream fos = new FileOutputStream(filename);
        fos.write(conteudo.getBytes());
        fos.close();
    }

}
