package chat_servidor.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

public class HiloCliente extends Thread {
    private final Socket socket;    
    private ObjectOutputStream oos;
    private ObjectInputStream ois;            
    private final Servidor server;
    private String id;
    private boolean listening;

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

    public void desconectar() {
        try {
            socket.close();
            listening = false;
        } catch (IOException ex) {
            System.err.println("Error al cerrar el socket de comunicación con el cliente.");
        }
    }

    public void run() {
        try {
            listening();
        } catch (Exception ex) {
            System.err.println("Error al llamar al método readLine del hilo del cliente.");
        }
        desconectar();
    }

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

    private void enviarMensaje(LinkedList<String> lista) {
        try {
            oos.writeObject(lista);            
        } catch (Exception e) {
            System.err.println("Error al enviar el objeto al cliente.");
        }
    }    

    private void confirmarConexion(String id) {
        Servidor.correlativo++;
        this.id = Servidor.correlativo + " - " + id;
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

    public String getIdent() {
        return id;
    }

    private void confirmarDesConexion() {
        LinkedList<String> auxLista=new LinkedList<>();
        auxLista.add("USUARIO_DESCONECTADO");
        auxLista.add(this.id);
        server.log("\nEl cliente \""+this.id+"\" se ha desconectado.");
        this.desconectar();
        for(int i=0;i<server.clientes.size();i++){
            if(server.clientes.get(i).equals(this)){
                server.clientes.remove(i);
                break;
            }
        }
        server.clientes
                .stream()
                .forEach(h -> h.enviarMensaje(auxLista));        
    }
}