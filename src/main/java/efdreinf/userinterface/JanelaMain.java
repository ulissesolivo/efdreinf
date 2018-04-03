package efdreinf.userinterface;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import efdreinf.adapter.ArquivoLocalAdapter;
import efdreinf.adapter.IBackendAdapter;
import efdreinf.adapter.ServidorErpAdapter;
import efdreinf.adapter.SoapAdapter;
import efdreinf.operacao.EnviarLote;
import efdreinf.util.SegurancaUtils;

public class JanelaMain extends JFrame {

    private static final long serialVersionUID = -7362159179640950148L;

    private JTextField txtLoginERP;
    private JTextField txtSenhaERP;
    private JTextField txtArquivoPFX;
    private JTextField txtIdCertificado;
    private JTextField txtSenhaCertificado;

    public static void main(String[] args) {
        new JanelaMain();
    }

    public JanelaMain() {
        super("Sincronizador ERP x EFD-Reinf");

        setBounds(320, 240, 640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(5, 5));

        JPanel panel1 = makePanel("Certificado Digital", 3);
        panel1.add(new JLabel("Certificado PFX:"));

        txtArquivoPFX = new JTextField("");

        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivo de certificado digital (PFX)", "pfx");
        fileChooser.setFileFilter(filter);

        JButton button = new JButton("Arquivo...");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (fileChooser.showOpenDialog(JanelaMain.this) == JFileChooser.APPROVE_OPTION) {
                    txtArquivoPFX.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        panel1.add(txtArquivoPFX);
        panel1.add(button);

        panel1.add(new JLabel("ID e Senha:"));

        txtIdCertificado = new JTextField("");
        panel1.add(txtIdCertificado);

        txtSenhaCertificado = new JPasswordField("");
        panel1.add(txtSenhaCertificado);

        JPanel panel2 = makePanel("Acesso ERP", 3);
        panel2.add(new JLabel("Usuário e Senha:"));

        txtLoginERP = new JTextField("");
        panel2.add(txtLoginERP);

        txtSenhaERP = new JPasswordField("");
        panel2.add(txtSenhaERP);
        contentPane.add(panel2);

        JPanel panel3 = createButtons();

        contentPane.add(panel1, BorderLayout.PAGE_START);
        contentPane.add(panel2, BorderLayout.CENTER);
        contentPane.add(panel3, BorderLayout.PAGE_END);

        try {
            inicializar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(JanelaMain.this, e.getMessage(), "Erro!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        setContentPane(contentPane);
        pack();
        toFront();
        repaint();
        setVisible(true);

    }

    protected JPanel makePanel(String titulo, int itensHorizontais) {
        JPanel panel = new JPanel(false);
        GridLayout layout = new GridLayout(0, itensHorizontais);
        layout.setHgap(10);
        layout.setVgap(2);
        panel.setLayout(layout);
        if (!"".equals(titulo)) {
            panel.setBorder(BorderFactory.createTitledBorder(titulo));
        }
        return panel;
    }

    private JPanel createButtons() {
        JPanel btnPanel = makePanel("", 2);

        JButton btn1 = new JButton("Processar");
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    iniciarProcesso();
                    JOptionPane.showMessageDialog(JanelaMain.this, "Sincronizacao concluida!", "EFD Reinf", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(JanelaMain.this, e1.getMessage(), "Erro!", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
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

    private void inicializar() throws Exception {
        txtIdCertificado.setText(SegurancaUtils.get().getClientAlias());
        txtSenhaCertificado.setText(SegurancaUtils.get().getClientPassword());
        txtArquivoPFX.setText(SegurancaUtils.get().getClientPfx());
        txtLoginERP.setText(SegurancaUtils.get().getERPLogin());
        txtSenhaERP.setText(SegurancaUtils.get().getERPSenha());
    }

    protected void iniciarProcesso() throws Exception {

        SegurancaUtils.get().setClientAlias(txtIdCertificado.getText());
        SegurancaUtils.get().setClientPassword(txtSenhaCertificado.getText());
        SegurancaUtils.get().setClientPfx(txtArquivoPFX.getText());
        SegurancaUtils.get().setERPLogin(txtLoginERP.getText());
        SegurancaUtils.get().setERPSenha(txtSenhaERP.getText());
        
        SegurancaUtils.get().inicializarCertificados();

        SoapAdapter soap = new SoapAdapter(//
                "https://preprodefdreinf.receita.fazenda.gov.br/RecepcaoLoteReinf.svc", //
                "http://sped.fazenda.gov.br/RecepcaoLoteReinf/ReceberLoteEventos");

        IBackendAdapter entrada;
        if ("ERP".equals(SegurancaUtils.get().getModoIntegracao())) {
            entrada = new ServidorErpAdapter();
        } else {
            entrada = new ArquivoLocalAdapter();
        }

        EnviarLote enviarLote = new EnviarLote();

        enviarLote.processar(entrada, soap);
    }

}
