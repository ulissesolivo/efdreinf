package efdreinf.adapter;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import efdreinf.util.SegurancaUtils;

public class ArquivoLocalAdapter implements IBackendAdapter {

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
    public List<String> getListaIds() {
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

        return ids;
    }

    @Override
    public InputStream getArquivo(String id) throws Exception {
        File file = new File(pastaOrigem + "/" + id + ".xml");
        FileInputStream fileInputStream = new FileInputStream(file);
        return fileInputStream;
    }

    @Override
    public void guardarResposta(String id, String conteudo) throws Exception {
        FileOutputStream fos = new FileOutputStream(pastaDestino + "/" + id + ".xml");
        fos.write(conteudo.getBytes());
        fos.close();
    }

}
