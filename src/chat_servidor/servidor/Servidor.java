package chat_servidor.servidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import javax.swing.JOptionPane;

/**
 * Clase que representa el servidor principal que maneja las conexiones de los clientes.
 * Extiende la clase {@link Thread}.
 * 
 * @author Manuel Abalo Rietz
 * @author Adrián Ces López
 * @author Pablo Dopazo Suárez
 * @version 1.0.0
 * 
 */
public class Servidor extends Thread {
    /** Socket del servidor que escucha conexiones entrantes. */
    private ServerSocket socketServer;

    /** Lista de hilos de cliente conectados al servidor. */
    LinkedList<HiloCliente> clientes;

    /** Ventana gráfica asociada al servidor. */
    private final VentanaS ventana;

    /** Puerto en el que se inicia el servidor. */
    private final String puerto;

    /** Diferenciador para generar identificadores únicos para los clientes. */
    static int diferenciador;

    /**
     * Constructor que inicializa el servidor y lo configura para escuchar conexiones en un puerto dado.
     *
     * @param puerto Puerto en el que se iniciará el servidor.
     * @param ventana Interfaz gráfica del servidor.
     */
    public Servidor(String puerto, VentanaS ventana) {
        diferenciador = 0;
        this.puerto = puerto;
        this.ventana = ventana;
        clientes = new LinkedList<>();
        this.start();
    }

    /**
     * Método principal del hilo que se ejecuta al iniciar el servidor.
     * Maneja la recepción de conexiones entrantes.
     */
    public void run() {
        try {
            socketServer = new ServerSocket(Integer.valueOf(puerto));
            ventana.addServidorIniciado();
            while (true) {
                Socket socket = socketServer.accept();
                System.out.println("Nueva conexión entrante: " + socket);
                HiloCliente h = new HiloCliente(socket, this);
                h.start();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(ventana, "El servidor no se ha podido iniciar,\n"
                                                 + "puede que haya ingresado un puerto incorrecto.\n"
                                                 + "Esta aplicación se cerrará.");
            System.exit(0);
        }
    }

    /**
     * Obtiene la lista de identificadores de los usuarios conectados al servidor.
     *
     * @return Lista de identificadores de usuarios conectados.
     */
    LinkedList<String> getUsuariosConectados() {
        LinkedList<String> usuariosConectados = new LinkedList<>();
        clientes.forEach(c -> usuariosConectados.add(c.getIdent()));
        return usuariosConectados;
    }

    /**
     * Agrega una entrada al log en la interfaz gráfica del servidor.
     *
     * @param texto Texto a agregar al log.
     */
    void log(String texto) {
        ventana.log(texto);
    }
}
