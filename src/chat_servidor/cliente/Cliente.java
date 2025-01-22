package chat_servidor.cliente;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import javax.swing.JOptionPane;

public class Cliente extends Thread {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private final VentanaC ventanaCliente;
    private String idCliente;
    private boolean listening;
    private final String host;
    private final int puerto;

    public Cliente(VentanaC ventana, String host, Integer puerto, String nombre) {
        this.ventanaCliente = ventana;
        this.host = host;
        this.puerto = puerto;
        this.idCliente = nombre;
        listening = true;
        this.start();
    }

    public void run() {
        try {
            socket = new Socket(host, puerto);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Conexion exitosa!");
            this.conexionCliente(idCliente);
            this.escuchar();
        } catch (UnknownHostException ex) {
            JOptionPane.showMessageDialog(ventanaCliente, "Conexión rehusada, servidor desconocido,\n"
                                                        + "puede que haya ingresado una ip incorrecta\n"
                                                        + "o que el servidor no este corriendo.\n"
                                                        + "Esta aplicación se cerrará.");
            System.exit(0);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(ventanaCliente, "Conexión rehusada, error de Entrada/Salida,\n"
                                                 + "puede que haya ingresado una ip o un puerto\n"
                                                 + "incorrecto, o que el servidor no este corriendo.\n"
                                                 + "Esta aplicación se cerrará.");
            System.exit(0);
        }
    }

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

    public void escuchar() {
        try {
            while (listening) {
                Object aux = objectInputStream.readObject();
                if (aux instanceof LinkedList) {
                    ejecutar((LinkedList<String>) aux);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(ventanaCliente, "Se perdió la comunicación con el servidor.");
            System.exit(0);
        }
    }

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
        }
    }

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

    String getIdCliente() {
        return idCliente;
    }
}
