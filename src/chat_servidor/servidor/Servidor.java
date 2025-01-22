package chat_servidor.servidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import javax.swing.JOptionPane;

public class Servidor extends Thread {    
    private ServerSocket socketServer;
    LinkedList<HiloCliente> clientes;
    private final VentanaS ventana;
    private final String puerto;
    static int diferenciador;

    public Servidor(String puerto, VentanaS ventana) {
        diferenciador = 0;
        this.puerto = puerto;
        this.ventana = ventana;
        clientes = new LinkedList<>();
        this.start();
    }

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

    LinkedList<String> getUsuariosConectados() {
        LinkedList<String> usuariosConectados = new LinkedList<>();
        clientes.forEach(c -> usuariosConectados.add(c.getIdentificador()));
        return usuariosConectados;
    }

    void log(String texto) {
        ventana.log(texto);
    }
}