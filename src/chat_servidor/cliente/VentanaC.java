package chat_servidor.cliente;

import java.awt.GridLayout;
import javax.swing.*;

public class VentanaC extends JFrame {

    private final String DEFAULT_PORT = "10101";
    private final String DEFAULT_IP = "127.0.0.1";
    private final Cliente cliente;
    JComboBox<String> cmbContactos = new JComboBox<>();
    JTextArea txtHistorial = new JTextArea();

    public VentanaC() {
        initComponents();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String[] ip_puerto_nombre = getIP_Puerto_Nombre();
        String ip = ip_puerto_nombre[0];
        String puerto = ip_puerto_nombre[1];
        String nombre = ip_puerto_nombre[2];
        cliente = new Cliente(this, ip, Integer.valueOf(puerto), nombre);
    }

    private void initComponents() {
        JScrollPane jScrollPane1 = new JScrollPane();
        JTextField txtMensaje = new JTextField();
        JButton btnEnviar = new JButton("Enviar");
        JLabel jLabel1 = new JLabel("Destinatario:");
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        txtHistorial.setEditable(false);
        txtHistorial.setColumns(20);
        txtHistorial.setRows(5);
        jScrollPane1.setViewportView(txtHistorial);

        btnEnviar.addActionListener(evt -> {
            if (cmbContactos.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un destinatario.");
                return;
            }
            String cliente_receptor = cmbContactos.getSelectedItem().toString();
            String mensaje = txtMensaje.getText();
            cliente.enviarMensaje(cliente_receptor, mensaje);
            txtHistorial.append("## Yo -> " + cliente_receptor + " ## : \n" + mensaje + "\n");
            txtMensaje.setText("");
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbContactos, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(txtMensaje)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEnviar))
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(cmbContactos))
            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(txtMensaje)
                .addComponent(btnEnviar))
        );

        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new VentanaC().setVisible(true));
    }

    private String[] getIP_Puerto_Nombre() {
        String[] s = new String[3];
        JTextField ip = new JTextField(DEFAULT_IP);
        JTextField puerto = new JTextField(DEFAULT_PORT);
        JTextField usuario = new JTextField("Usuario");

        JPanel myPanel = new JPanel(new GridLayout(3, 2));
        myPanel.add(new JLabel("IP del Servidor:"));
        myPanel.add(ip);
        myPanel.add(new JLabel("Puerto:"));
        myPanel.add(puerto);
        myPanel.add(new JLabel("Nombre de usuario:"));
        myPanel.add(usuario);

        int result = JOptionPane.showConfirmDialog(null, myPanel, "Configuración de conexión", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            s[0] = ip.getText();
            s[1] = puerto.getText();
            s[2] = usuario.getText();
        } else {
            System.exit(0);
        }
        return s;
    }

    void addContacto(String contacto) {
        cmbContactos.addItem(contacto);
    }

    void addMensaje(String emisor, String mensaje) {
        txtHistorial.append("##### " + emisor + " ##### : \n" + mensaje + "\n");
    }

    void sesionIniciada(String identificador) {
        this.setTitle(" --- " + identificador + " --- ");
    }

    void eliminarContacto(String identificador) {
        for (int i = 0; i < cmbContactos.getItemCount(); i++) {
            if (cmbContactos.getItemAt(i).equals(identificador)) {
                cmbContactos.removeItemAt(i);
                return;
            }
        }
    }
}
