package chat_servidor.cliente;

import java.awt.GridLayout;
import javax.swing.*;

public class VentanaC extends JFrame {

    private final String DEFAULT_PORT = "10101";
    private final String DEFAULT_IP = "127.0.0.1";
    private final Cliente cliente;
    JComboBox<String> contactos = new JComboBox<>();
    JTextArea historial = new JTextArea();

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
        JScrollPane jScrollPane1 = new JScrollPane();
        JTextField Mensaje = new JTextField();
        JButton btnEnviar = new JButton("Enviar");
        JLabel jLabel1 = new JLabel("Destinatario:");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        historial.setEditable(false);
        historial.setColumns(20);
        historial.setRows(5);
        jScrollPane1.setViewportView(historial);

        btnEnviar.addActionListener(evt -> {
            if (contactos.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un destinatario.");
                return;
            }
            String cliente_receptor = contactos.getSelectedItem().toString();
            String mensaje = Mensaje.getText();
            cliente.mensajear(cliente_receptor, mensaje);
            historial.append("## Yo -> " + cliente_receptor + " ## : \n" + mensaje + "\n");
            Mensaje.setText("");
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contactos, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(Mensaje)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEnviar))
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(contactos))
            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(Mensaje)
                .addComponent(btnEnviar))
        );

        pack();
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
