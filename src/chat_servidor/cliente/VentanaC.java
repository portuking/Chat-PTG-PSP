package chat_servidor.cliente;

import java.awt.GridLayout;
import javax.swing.*;

/**
 * Ventana principal de la interfaz gráfica del cliente.
 * Proporciona funcionalidad para interactuar con el sistema de chat.
 * 
 * @author Manuel Abalo Rietz
 * @author Adrián Ces López
 * @author Pablo Dopazo Suárez
 * @version 1.0.0
 * 
 */
public class VentanaC extends JFrame {

    /** Puerto por defecto para conectarse al servidor. */
    private final String DEFAULT_PORT = "10101";
    /** Dirección IP por defecto del servidor. */
    private final String DEFAULT_IP = "127.0.0.1";
    /** Cliente que maneja la comunicación con el servidor. */
    private final Cliente cliente;
    /** Botón para enviar mensajes. */
    private javax.swing.JButton btnEnviar;
    /** Lista desplegable con los contactos disponibles. */
    private javax.swing.JComboBox contactos;
    /** Etiqueta de texto para indicar destinatarios. */
    private javax.swing.JLabel jLabel1;
    /** Panel de desplazamiento para el área de texto del historial de mensajes. */
    private javax.swing.JScrollPane jScrollPane1;
    /** Área de texto para mostrar el historial de mensajes. */
    private javax.swing.JTextArea historial;
    /** Campo de texto para ingresar el mensaje a enviar. */
    private javax.swing.JTextField txtMensaje;

    /**
     * Constructor de VentanaC.
     * Inicializa la interfaz gráfica y configura la conexión del cliente.
     */
    public VentanaC() {
        initVentanaC();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String[] ipPuerto = getIPPuerto();
        String ip = ipPuerto[0];
        String puerto = ipPuerto[1];
        String nombre = ipPuerto[2];
        cliente = new Cliente(this, ip, Integer.valueOf(puerto), nombre);
    }

    /**
     * Inicializa los componentes de la ventana.
     */
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
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(txtMensaje)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnEnviar))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(contactos, javax.swing.GroupLayout.PREFERRED_SIZE, 198,
                                javax.swing.GroupLayout.PREFERRED_SIZE)))
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

    /**
     * Acción del botón Enviar. Envía un mensaje al destinatario seleccionado.
     * 
     * @param evt Evento del botón.
     */
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

    /**
     * Cierra la sesión del cliente al cerrar la ventana.
     * 
     * @param evt Evento de cierre de ventana.
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        cliente.desconexionCliente();
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {
    }

    /**
     * Muestra un diálogo para configurar la IP, puerto y nombre de usuario.
     * 
     * @return Un arreglo con IP, puerto y nombre de usuario.
     */
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

        int result = JOptionPane.showConfirmDialog(null, myPanel, "Configuración de conexión", 
            JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            s[0] = ip.getText();
            s[1] = puerto.getText();
            s[2] = usuario.getText();
        } else {
            System.exit(0);
        }
        return s;
    }

    /**
     * Añade un nuevo contacto a la lista de destinatarios.
     * 
     * @param contacto Nombre del contacto.
     */
    void addContacto(String contacto) {
        contactos.addItem(contacto);
    }

    /**
     * Añade un mensaje al historial.
     * 
     * @param emisor Nombre del remitente.
     * @param mensaje Contenido del mensaje.
     */
    void addMensaje(String emisor, String mensaje) {
        historial.append("##### " + emisor + " ##### : \n" + mensaje + "\n");
    }

    /**
     * Configura el título de la ventana al iniciar sesión.
     * 
     * @param identificador Identificador del cliente.
     */
    void sesionIniciada(String identificador) {
        this.setTitle(" --- " + identificador + " --- ");
    }

    /**
     * Elimina un contacto de la lista de destinatarios.
     * 
     * @param identificador Identificador del contacto.
     */
    void eliminarContacto(String identificador) {
        for (int i = 0; i < contactos.getItemCount(); i++) {
            if (contactos.getItemAt(i).equals(identificador)) {
                contactos.removeItemAt(i);
                return;
            }
        }
    }

    /**
     * Método principal. Inicia la interfaz gráfica.
     * 
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new VentanaC().setVisible(true));
    }
}
