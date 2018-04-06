package efdreinf.userinterface;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.apache.log4j.Logger;

import efdreinf.batch.BatchEnviaLoteMain;
import efdreinf.util.SegurancaUtils;

public class JanelaMain extends JFrame {

    private static final String TITULO = "Sincronizador ERP x EFD-Reinf";

    private static final long serialVersionUID = -7362159179640950148L;

    public static final Logger LOGGER = Logger.getLogger(JanelaMain.class);

    private JTextField txtLoginERP;
    private JTextField txtSenhaERP;
    private JTextField txtArquivoPFX;
    private JTextField txtIdCertificado;
    private JTextField txtSenhaCertificado;
    private JTextField txtPastaEnvio;
    private JTextField txtPastaLogs;

    private JRadioButton radioErp;
    private JRadioButton radioFile;
    private JButton btnSelecaoPastaEvento;
    private JLabel lblPastaEventos;
    private JLabel lblUsuarioSenha;

    public static void main(String[] args) {
        LOGGER.info(TITULO);
        new JanelaMain();
    }

    public JanelaMain() {
        super(TITULO);

        setBounds(320, 240, 640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 5));
        contentPane.setLayout(new BorderLayout(15, 15));

        JPanel panel1 = makePanel("Certificado Digital");
        panel1.add(new JLabel("Certificado PFX:"));

        txtArquivoPFX = new JTextField("");
        panel1.add(txtArquivoPFX);
        panel1.add(criaBotaoSelecaoArquivo());

        panel1.add(new JLabel("ID e Senha:"));

        txtIdCertificado = new JTextField("");
        panel1.add(txtIdCertificado);

        txtSenhaCertificado = new JPasswordField("");
        panel1.add(txtSenhaCertificado);

        JPanel panel2 = makePanel("Fonte de dados");

        panel2.add(new JLabel("Fonte de dados:"));
        radioErp = new JRadioButton("ERP");
        radioFile = new JRadioButton("Arquivo");
        ButtonGroup radiogroup = new ButtonGroup();
        radiogroup.add(radioErp);
        radiogroup.add(radioFile);
        panel2.add(radioErp);
        panel2.add(radioFile);

        lblUsuarioSenha = new JLabel("Usuario e Senha:");
        panel2.add(lblUsuarioSenha);
        txtLoginERP = new JTextField("");
        panel2.add(txtLoginERP);
        txtSenhaERP = new JPasswordField("");
        panel2.add(txtSenhaERP);
        contentPane.add(panel2);

        lblPastaEventos = new JLabel("Pasta Eventos XML:");
        panel2.add(lblPastaEventos);
        txtPastaEnvio = new JTextField("");
        panel2.add(txtPastaEnvio);
        btnSelecaoPastaEvento = criaBotaoSelecaoPasta(txtPastaEnvio);
        panel2.add(btnSelecaoPastaEvento);

        JLabel lblPastaLogs = new JLabel("Pasta Logs:");
        panel2.add(lblPastaLogs);
        txtPastaLogs = new JTextField("");
        panel2.add(txtPastaLogs);
        JButton btnSelecaoPastaLogs = criaBotaoSelecaoPasta(txtPastaLogs);
        panel2.add(btnSelecaoPastaLogs);

        ActionListener actionMudaFonteDados = criaActionAtualizaRadio();
        radioErp.addActionListener(actionMudaFonteDados);
        radioFile.addActionListener(actionMudaFonteDados);

        JPanel panel3 = createButtons();

        contentPane.add(panel1, BorderLayout.PAGE_START);
        contentPane.add(panel2, BorderLayout.CENTER);
        contentPane.add(panel3, BorderLayout.PAGE_END);

        try {
            inicializar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(JanelaMain.this, e.getMessage(), "Erro!", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("Problema ao iniciar o aplicativo!", e);
        }

        atualizaCamposFonteDados();

        setContentPane(contentPane);
        pack();
        toFront();
        repaint();
        setVisible(true);

    }

    private ActionListener criaActionAtualizaRadio() {
        ActionListener actionMudaFonteDados = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizaCamposFonteDados();
            }
        };
        return actionMudaFonteDados;
    }

    private void atualizaCamposFonteDados() {
        boolean isErp = radioErp.isSelected();
        txtLoginERP.setVisible(isErp);
        txtSenhaERP.setVisible(isErp);
        lblUsuarioSenha.setVisible(isErp);
        txtPastaEnvio.setVisible(!isErp);
        btnSelecaoPastaEvento.setVisible(!isErp);
        lblPastaEventos.setVisible(!isErp);
    }

    private JButton criaBotaoSelecaoPasta(final JTextField txtfield) {
        final JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        JButton btn = new JButton("Pasta...", UIManager.getIcon("FileView.directoryIcon"));
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (fc.showOpenDialog(JanelaMain.this) == JFileChooser.APPROVE_OPTION) {
                    txtfield.setText(fc.getSelectedFile().getAbsolutePath());
                }
            }
        });

        return btn;
    }

    private JButton criaBotaoSelecaoArquivo() {
        final JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivo de certificado digital (PFX)", "pfx");
        fc.setFileFilter(filter);

        JButton btn = new JButton("Arquivo...", UIManager.getIcon("FileView.fileIcon"));
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (fc.showOpenDialog(JanelaMain.this) == JFileChooser.APPROVE_OPTION) {
                    txtArquivoPFX.setText(fc.getSelectedFile().getAbsolutePath());
                }
            }
        });

        return btn;
    }

    protected JPanel makePanel(String titulo) {
        JPanel panel = new JPanel(false);
        GridLayout layout = new GridLayout(0, 3);
        layout.setHgap(10);
        layout.setVgap(2);
        panel.setLayout(layout);
        if (!"".equals(titulo)) {
            panel.setBorder(BorderFactory.createTitledBorder(titulo));
        }
        return panel;
    }

    private JPanel createButtons() {
        JPanel btnPanel = makePanel("");
        btnPanel.add(new JLabel());

        JButton btn1 = new JButton("Processar lotes", UIManager.getIcon("FileChooser.listViewIcon"));
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    processar();
                    JOptionPane.showMessageDialog(JanelaMain.this, "Sincronizacao concluida!", "EFD Reinf", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(JanelaMain.this, e1.getMessage(), "Erro!", JOptionPane.ERROR_MESSAGE);
                    LOGGER.error("Erro durante a sincronizacao!", e1);
                }
            }
        });
        btnPanel.add(btn1);

        JButton btn2 = new JButton("Cancelar");
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JanelaMain.this.setEnabled(false);
                System.exit(0);
            }
        });
        btnPanel.add(btn2);
        return btnPanel;
    }

    private void inicializar() {
        txtIdCertificado.setText(SegurancaUtils.get().getClientAlias());
        txtSenhaCertificado.setText(SegurancaUtils.get().getClientPassword());
        txtArquivoPFX.setText(SegurancaUtils.get().getClientPfx());
        txtPastaEnvio.setText(SegurancaUtils.get().getPastaEnvioEventos());
        txtPastaLogs.setText(SegurancaUtils.get().getPastaLogs());

        boolean isErp = "ERP".equals(SegurancaUtils.get().getModoIntegracao());
        radioErp.setSelected(isErp);
        radioFile.setSelected(!isErp);

        String erpLoginSenha = SegurancaUtils.get().getERPLoginSenha();
        if (erpLoginSenha != null && erpLoginSenha.contains(":")) {
            String[] split = erpLoginSenha.split(":");
            txtLoginERP.setText(split[0]);
            txtSenhaERP.setText(split[1]);
        }
    }

    protected void processar() throws Exception {

        SegurancaUtils.get().setClientAlias(txtIdCertificado.getText());
        SegurancaUtils.get().setClientPassword(txtSenhaCertificado.getText());
        SegurancaUtils.get().setClientPfx(txtArquivoPFX.getText());
        SegurancaUtils.get().setERPLoginSenha(txtLoginERP.getText() + ":" + txtSenhaERP.getText());
        SegurancaUtils.get().setModoIntegracao(radioErp.isSelected() ? "ERP" : "FILE");
        SegurancaUtils.get().setPastaEnvioEventos(txtPastaEnvio.getText());
        SegurancaUtils.get().setPastaLogs(txtPastaLogs.getText());

        new BatchEnviaLoteMain().processar();
    }

}
