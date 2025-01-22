package chat_servidor.servidor;

import java.awt.GridLayout;
import javax.swing.*;

/**
 * Clase que representa la ventana principal de la interfaz gráfica del servidor.
 * Extiende la clase {@link JFrame}.
 */
public class VentanaS extends JFrame {
    private final String DEFAULT_PORT = "10101";
    private final Servidor server;
    JTextArea txtClientes = new JTextArea();

    /**
     * Constructor que inicializa la ventana gráfica del servidor y su configuración.
     */
    public VentanaS() {
        initComponents();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String puerto = getPuerto();
        server = new Servidor(puerto, this);
    }

    /**
     * Configura los componentes gráficos de la ventana.
     */
    private void initComponents() {
        JScrollPane jScrollPane1 = new JScrollPane();
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

    /**
     * Método principal que inicia la aplicación del servidor.
     *
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new VentanaS().setVisible(true));
    }

    /**
     * Agrega texto al log en la interfaz gráfica del servidor.
     *
     * @param texto Texto a agregar al log.
     */
    void log(String texto) {
        txtClientes.append(texto);
    }

    /**
     * Obtiene el puerto de conexión ingresado por el usuario.
     *
     * @return Puerto configurado para el servidor.
     */
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

    /**
     * Muestra un mensaje indicando que el servidor se ha iniciado correctamente.
     */
    void addServidorIniciado() {
        txtClientes.setText("Inicializando el servidor... [OK].");
    }
}
