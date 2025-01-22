package chat_servidor.cliente;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import javax.swing.JOptionPane;

/**
 * Clase Cliente que representa un cliente en un sistema de chat.
 * Se encarga de la comunicación entre el cliente y el servidor a través de sockets.
 * 
 * @author Manuel Abalo Rietz
 * @author Adrián Ces López
 * @author Pablo Dopazo Suárez
 * @version 1.0.0
 * 
 */
public class Cliente extends Thread {

    /** Socket utilizado para la comunicación con el servidor. */
    private Socket socket;
    /** Flujo de salida para enviar datos al servidor. */
    private ObjectOutputStream objectOutputStream;
    /** Flujo de entrada para recibir datos del servidor. */
    private ObjectInputStream objectInputStream;
    /** Referencia a la ventana gráfica del cliente. */
    private final VentanaC ventanaCliente;
    /** Identificador único del cliente. */
    private String idCliente;
    /** Indicador de si el cliente debe seguir escuchando mensajes del servidor. */
    private boolean listening;
    /** Dirección IP del servidor. */
    private final String host;
    /** Puerto del servidor. */
    private final int puerto;

    /**
     * Constructor de Cliente.
     * 
     * @param ventana Instancia de la ventana asociada al cliente.
     * @param host Dirección IP del servidor.
     * @param puerto Puerto del servidor.
     * @param nombre Nombre del cliente.
     */
    public Cliente(VentanaC ventana, String host, Integer puerto, String nombre) {
        this.ventanaCliente = ventana;
        this.host = host;
        this.puerto = puerto;
        this.idCliente = nombre;
        listening = true;
        this.start();
    }

    /**
     * Método principal del hilo, establece la conexión con el servidor
     * y maneja la comunicación.
     */
    public void run() {
        try {
            socket = new Socket(host, puerto);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Conexion exitosa!");
            this.conexionCliente(idCliente);
            this.escuchar();
        } catch (UnknownHostException ex) {
            JOptionPane.showMessageDialog(ventanaCliente, "Conexión rehusada, servidor desconocido.\n"
                    + "Puede que la IP sea incorrecta o el servidor no esté corriendo.");
            System.exit(0);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(ventanaCliente, "Conexión rehusada, error de Entrada/Salida.\n"
                    + "Puede que la IP o el puerto sean incorrectos.");
            System.exit(0);
        }
    }

    /**
     * Desconecta al cliente del servidor y cierra los flujos de comunicación.
     */
    public void desconectar() {
        try {
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();
            listening = false;
        } catch (Exception e) {
            System.err.println("Error al cerrar los elementos de comunicación.");
        }
    }

    /**
     * Envía un mensaje a otro cliente a través del servidor.
     * 
     * @param cliente_receptor Nombre del cliente receptor.
     * @param mensaje Mensaje a enviar.
     */
    public void mensajear(String cliente_receptor, String mensaje) {
        LinkedList<String> lista = new LinkedList<>();
        lista.add("MENSAJE");
        lista.add(idCliente);
        lista.add(cliente_receptor);
        lista.add(mensaje);
        try {
            objectOutputStream.writeObject(lista);
        } catch (IOException ex) {
            System.out.println("Error al enviar mensaje al servidor.");
        }
    }

    /**
     * Escucha mensajes provenientes del servidor y los procesa.
     */
    public void escuchar() {
        try {
            while (listening) {
                Object aux = objectInputStream.readObject();
                if (aux != null && aux instanceof LinkedList) {
                    ejecutar((LinkedList<String>) aux);
                } else {
                    System.err.println("Se recibió un objeto desconocido o nulo.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(ventanaCliente, "Se perdió la comunicación con el servidor.");
            System.exit(0);
        }
    }

    /**
     * Ejecuta acciones en función del tipo de mensaje recibido.
     * 
     * @param lista Lista de parámetros del mensaje.
     */
    public void ejecutar(LinkedList<String> lista) {
        String tipo = lista.get(0);
        switch (tipo) {
            case "CONEXION_ACEPTADA":
                idCliente = lista.get(1);
                ventanaCliente.sesionIniciada(idCliente);
                for (int i = 2; i < lista.size(); i++) {
                    ventanaCliente.addContacto(lista.get(i));
                }
                break;
            case "NUEVO_USUARIO_CONECTADO":
                ventanaCliente.addContacto(lista.get(1));
                break;
            case "USUARIO_DESCONECTADO":
                ventanaCliente.eliminarContacto(lista.get(1));
                break;
            case "MENSAJE":
                ventanaCliente.addMensaje(lista.get(1), lista.get(3));
                break;
            default:
                break;
        }
    }

    /**
     * Solicita la conexión del cliente al servidor.
     * 
     * @param identificador Identificador del cliente.
     */
    private void conexionCliente(String identificador) {
        LinkedList<String> lista = new LinkedList<>();
        lista.add("SOLICITUD_CONEXION");
        lista.add(identificador);
        try {
            objectOutputStream.writeObject(lista);
        } catch (IOException ex) {
            System.out.println("Error al enviar mensaje de conexión.");
        }
    }

    /**
     * Solicita la desconexión del cliente al servidor.
     */
    void desconexionCliente() {
        LinkedList<String> lista = new LinkedList<>();
        lista.add("SOLICITUD_DESCONEXION");
        lista.add(idCliente);
        try {
            objectOutputStream.writeObject(lista);
        } catch (IOException ex) {
            System.out.println("Error al enviar mensaje de desconexión.");
        }
    }

    /**
     * Obtiene el identificador del cliente.
     * 
     * @return Identificador del cliente.
     */
    String getIdCliente() {
        return idCliente;
    }
}
