package chat_servidor.servidor;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Clase que representa un hilo encargado de manejar la conexión con un cliente en el servidor de chat.
 * Extiende la clase {@link Thread}.
 */
public class HiloCliente extends Thread {
    private final Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final Servidor server;
    private String id;
    private boolean listening;

    /**
     * Constructor que inicializa el hilo para manejar la comunicación con un cliente.
     *
     * @param socket Socket de comunicación con el cliente.
     * @param server Instancia del servidor que administra este hilo.
     */
    public HiloCliente(Socket socket, Servidor server) {
        this.server = server;
        this.socket = socket;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            System.err.println("Error en la inicialización del ObjectOutputStream y ObjectInputStream");
        }
    }

    /**
     * Desconecta al cliente cerrando el socket y deteniendo la escucha.
     */
    public void desconectar() {
        try {
            socket.close();
            listening = false;
        } catch (IOException ex) {
            System.err.println("Error al cerrar el socket de comunicación con el cliente.");
        }
    }

    /**
     * Método principal del hilo que se ejecuta al iniciar el hilo.
     * Maneja la escucha y la comunicación con el cliente.
     */
    public void run() {
        try {
            listening();
        } catch (Exception ex) {
            System.err.println("Error al llamar al método readLine del hilo del cliente.");
        }
        desconectar();
    }

    /**
     * Inicia el bucle de escucha para recibir mensajes del cliente.
     */
    public void listening() {
        listening = true;
        while (listening) {
            try {
                Object aux = ois.readObject();
                if (aux instanceof LinkedList) {
                    ejecutar((LinkedList<String>) aux);
                }
            } catch (Exception e) {
                System.err.println("Error al leer lo enviado por el cliente.");
            }
        }
    }

    /**
     * Ejecuta acciones basadas en los mensajes recibidos del cliente.
     *
     * @param lista Lista de cadenas que representa el mensaje recibido.
     */
    public void ejecutar(LinkedList<String> lista) {
        String tipo = lista.get(0);
        switch (tipo) {
            case "SOLICITUD_CONEXION":
                confirmarConexion(lista.get(1));
                break;
            case "SOLICITUD_DESCONEXION":
                confirmarDesConexion();
                break;
            case "MENSAJE":
                String destinatario = lista.get(2);
                server.clientes
                      .stream()
                      .filter(h -> destinatario.equals(h.getIdent()))
                      .forEach(h -> h.enviarMensaje(lista));
                break;
            default:
                break;
        }
    }

    /**
     * Envía un mensaje al cliente asociado a este hilo.
     *
     * @param lista Lista de cadenas que representa el mensaje a enviar.
     */
    private void enviarMensaje(LinkedList<String> lista) {
        try {
            oos.writeObject(lista);
        } catch (Exception e) {
            System.err.println("Error al enviar el objeto al cliente.");
        }
    }

    /**
     * Confirma la conexión del cliente asignándole un identificador único.
     *
     * @param id Identificador proporcionado por el cliente.
     */
    private void confirmarConexion(String id) {
        Servidor.diferenciador++;
        this.id = Servidor.diferenciador + " - " + id;
        LinkedList<String> lista = new LinkedList<>();
        lista.add("CONEXION_ACEPTADA");
        lista.add(this.id);
        lista.addAll(server.getUsuariosConectados());
        enviarMensaje(lista);
        server.log("\nNuevo cliente: " + this.id);
        LinkedList<String> auxLista = new LinkedList<>();
        auxLista.add("NUEVO_USUARIO_CONECTADO");
        auxLista.add(this.id);
        server.clientes.forEach(cliente -> cliente.enviarMensaje(auxLista));
        server.clientes.add(this);
    }

    /**
     * Obtiene el identificador único del cliente asociado a este hilo.
     *
     * @return Identificador del cliente.
     */
    public String getIdent() {
        return id;
    }

    /**
     * Confirma la desconexión del cliente y notifica a los demás.
     */
    private void confirmarDesConexion() {
        LinkedList<String> auxLista = new LinkedList<>();
        auxLista.add("USUARIO_DESCONECTADO");
        auxLista.add(this.id);
        server.log("\nEl cliente \"" + this.id + "\" se ha desconectado.");
        this.desconectar();
        for (int i = 0; i < server.clientes.size(); i++) {
            if (server.clientes.get(i).equals(this)) {
                server.clientes.remove(i);
                break;
            }
        }
        server.clientes.forEach(h -> h.enviarMensaje(auxLista));
    }
}