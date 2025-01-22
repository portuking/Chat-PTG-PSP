package chat_servidor.servidor;
import java.awt.GridLayout;
import javax.swing.*;

public class VentanaS extends JFrame {
    private final String DEFAULT_PORT = "10101";
    private final Servidor servidor;

    public VentanaS() {
        initComponents();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String puerto = getPuerto();
        servidor = new Servidor(puerto, this);
    }

    private void initComponents() {
        JScrollPane jScrollPane1 = new JScrollPane();
        JTextArea txtClientes = new JTextArea();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Servidor");

        jScrollPane1.setBorder(BorderFactory.createTitledBorder("Log del Servidor"));
        txtClientes.setEditable(false);
        txtClientes.setColumns(20);
        txtClientes.setRows(5);
        jScrollPane1.setViewportView(txtClientes);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        pack();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new VentanaS().setVisible(true));
    }

    void agregarLog(String texto) {
        txtClientes.append(texto);
    }

    private String getPuerto() {
        String p = DEFAULT_PORT;
        JTextField puerto = new JTextField(20);
        puerto.setText(p);
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new GridLayout(2, 1));
        myPanel.add(new JLabel("Puerto de la conexión:"));
        myPanel.add(puerto);
        int result = JOptionPane.showConfirmDialog(null, myPanel, 
                                                   "Configuraciones de la comunicación", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            p = puerto.getText();
        } else {
            System.exit(0);
        }
        return p;
    }

    void addServidorIniciado() {
        txtClientes.setText("Inicializando el servidor... [OK].");
    }
}
