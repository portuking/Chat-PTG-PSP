package chat_servidor.cliente;

import java.awt.GridLayout;
import javax.swing.*;

public class VentanaC extends JFrame {

    private final String DEFAULT_PORT = "10101";
    private final String DEFAULT_IP = "127.0.0.1";
    private final Cliente cliente;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JComboBox contactos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea historial;
    private javax.swing.JTextField txtMensaje;

    public VentanaC() {
        initVentanaC();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String[] ipPuerto = getIPPuerto();
        String ip = ipPuerto[0];
        String puerto = ipPuerto[1];
        String nombre = ipPuerto[2];
        cliente = new Cliente(this, ip, Integer.valueOf(puerto), nombre);
    }

    private void initVentanaC() {
        jScrollPane1 = new javax.swing.JScrollPane();
        historial = new javax.swing.JTextArea();
        txtMensaje = new javax.swing.JTextField();
        contactos = new javax.swing.JComboBox();
        btnEnviar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        historial.setEditable(false);
        historial.setColumns(20);
        historial.setRows(5);
        jScrollPane1.setViewportView(historial);

        btnEnviar.setText("Enviar");
        btnEnviar.addActionListener(evt -> btnEnviarActionPerformed(evt));

        jLabel1.setText("Destinatario:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                .createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(contactos, javax.swing.GroupLayout.PREFERRED_SIZE, 198,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtMensaje)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnEnviar)))
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(contactos, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtMensaje, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnEnviar))
                                .addContainerGap()));

        pack();
    }

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {
        if (contactos.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe escoger un destinatario válido.");
            return;
        }
        String cliente_receptor = contactos.getSelectedItem().toString();
        String mensaje = txtMensaje.getText();
        cliente.mensajear(cliente_receptor, mensaje);
        historial.append("## Yo -> " + cliente_receptor + " ## : \n" + mensaje + "\n");
        txtMensaje.setText("");
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        cliente.desconexionCliente();
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {
    }

    private String[] getIPPuerto() {
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
        contactos.addItem(contacto);
    }

    void addMensaje(String emisor, String mensaje) {
        historial.append("##### " + emisor + " ##### : \n" + mensaje + "\n");
    }

    void sesionIniciada(String identificador) {
        this.setTitle(" --- " + identificador + " --- ");
    }

    void eliminarContacto(String identificador) {
        for (int i = 0; i < contactos.getItemCount(); i++) {
            if (contactos.getItemAt(i).equals(identificador)) {
                contactos.removeItemAt(i);
                return;
            }
        }
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new VentanaC().setVisible(true));
    }
}
