import java.util.ArrayList;
import java.net.MulticastSocket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.util.*;
import java.io.*;
import java.lang.Thread;
 


//envia as mensagens para o cliente
public class MCServer extends UnicastRemoteObject implements Runnable {
    private static final long serialVersionUID = 1L;

    private MCServerData desk;
    private String mensagens;

    //threads
    private static MCServer mesa_voto;
    private static SecMultServer mesa_voto2;
    private static RMIClient rmi_connection;
    public Thread thread;
    
    public MCServer(SecMultServer mesa_voto2,String threadname,String ip, String port,String depar) throws RemoteException {
        this.mensagens = "";
        this.desk = new MCServerData(ip,port,depar);
        MCServer.mesa_voto2 = mesa_voto2;
        thread = new Thread(this,threadname);
    }


    public Thread getThread() { return thread; }
    public MCServerData getDesk() { return desk; }
    public String getMensagens() { return mensagens; }
    public void setMensagens(String mensagens) { this.mensagens = mensagens; }

    public static void main(String[] args) throws RemoteException,InterruptedException{
        Scanner scanner = new Scanner(System.in);
        String cart,depar;
        Inputs inpu = new Inputs();
        String messag = "";
        int cont = 0;
        /*rmi_connection = new RMIClient();
        rmi_connection.connect2Servers(rmi_connection);
        ArrayList<String> test= rmi_connection.getServer1().getCollegesNames();
        for (String string : test) System.out.println(string);*/
        
        depar = inpu.askVariable(scanner,"Insira o departamento a qual pertence: " , 0);
        mesa_voto = new MCServer(mesa_voto2,"mesa_voto",Gerar_Numeros.gerar_ip(),Gerar_Numeros.gerar_port(1000,10),depar);
        mesa_voto2 = new SecMultServer(mesa_voto,"mesa_voto2","", "", depar);
        //thread_eleitor = new Ask_Info_Eleitor(mesa_voto,"thread_eleitor", scanner, inpu);
        ReadWrite.Write("MCServerData.txt", mesa_voto.desk.getDeparNome(), mesa_voto.desk.getIp(),mesa_voto.desk.getPort());
        System.out.println("--------Mesa de Voto do Departamento "+mesa_voto.desk.getDeparNome()+"--------");
        mesa_voto2.thread.start();
        mesa_voto.thread.start();
        while(true){
            try {Thread.sleep(1000);} catch (InterruptedException e){}
            cart  = inpu.askVariable(scanner, "Insere o CC: ", 2);
            synchronized (mesa_voto.thread) {
                try {
                    System.out.println("WAIT");
                    mesa_voto.thread.wait();
                    System.out.println("SAIU DO WAIT");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
        
        
    public void run () {
        String []conection;
        String []aux;
        Scanner keyboardScanner= new Scanner(System.in);
        InetAddress group;
        DatagramPacket packet;
        System.out.println("entrei server->client");
        while(true){
            MulticastSocket socket = null;
            try {
                socket = new MulticastSocket();  // create socket without binding it (only for sending)                
                while (true) {
                    try {Thread.sleep(1000);} catch (InterruptedException e){}
                    if (mesa_voto.getMensagens().compareTo("")!=0){
                        System.out.println("mesa voto envia para o client: "+mesa_voto.getMensagens());
                        byte[] buffer = mesa_voto.getMensagens().getBytes();
                        group = InetAddress.getByName(getDesk().getIp());
                        packet = new DatagramPacket(buffer, buffer.length, group, Integer.parseInt(getDesk().getPort()));
                        socket.send(packet);
                        conection = mesa_voto.getMensagens().split(";");
                        aux = conection[0].split("\\|");
                        if(aux[1].compareTo("connected")==0){
                            synchronized (mesa_voto.thread) {
                                System.out.println("Parte de notify");
                                mesa_voto.thread.notify();
                                System.out.println("notificou");
                            }
                            
                        }
                        mesa_voto.setMensagens("");
                        
                    }
                }
            } catch (IOException e) { e.printStackTrace(); } 
            finally { socket.close(); keyboardScanner.close(); }
        }  
    }  
}

//recebe as mensagens do cliente
class SecMultServer implements Runnable {

    private MCServerData desk;
    private String mensagens;

    public Thread thread;
    private static MCServer mesa_voto;


    public SecMultServer(MCServer mesa_voto,String threadname,String ip, String port,String depar) throws RemoteException {
        this.desk = new MCServerData(ip, port, depar);
        SecMultServer.mesa_voto = mesa_voto;
        thread = new Thread(this,threadname);
    }
    
    public MCServerData getDesk() { return desk; }
    public String getMensagens() { return mensagens; }

    public void run() {
        
        File f= new File("TerminalVote.txt");
        System.out.println("entrei client->server");
        while(true){
            if (f.exists() && f.isFile()){
                ReadWrite.read_ip_port_terminal("TerminalVote.txt", getDesk());
                if (getDesk().getIp().compareTo("")!=0){
                    MulticastSocket socket = null;
                    try {
                        socket = new MulticastSocket(Integer.parseInt(getDesk().getPort()));  // create socket and bind it
                        InetAddress group = InetAddress.getByName(getDesk().getIp());
                        System.out.println(Integer.parseInt(getDesk().getPort())+getDesk().getIp());
                        socket.joinGroup(group);
                        while (true) {
                            byte[] buffer = new byte[256];
                            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);                            
                            socket.receive(packet);
                            System.out.println("(Server)Received packet from " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " with message:");
                            String message = new String(packet.getData(), 0, packet.getLength());
                            System.out.println(message);
                            Handler_Message.typeMessage(message, mesa_voto);

                        }
                    } catch (IOException e) { e.printStackTrace();
                    } finally { socket.close(); }
                }
            }
        }
       
    }    
}

class Handler_Message{
    //trata das mensagens que o server recebe
    public static Boolean typeMessage(String mensagem,MCServer mesa_voto){
        String [] sublista;
        String[] lista = mensagem.split(";");
        Random alea = new Random();
        String message_client = "";
        ArrayList <String> add_mensagens = new ArrayList<>();
        int ind;
        String id;
        sublista= lista[0].split("\\|");
        if (sublista[1].compareTo("RmiVerification")==0){
            //fazer algo
        }

        else if(sublista[1].compareTo("envia_id")==0){
            sublista= lista[1].split("\\|");
            mesa_voto.getDesk().getArray_id().add(Integer.parseInt(sublista[1]));
            if (mesa_voto.getDesk().getArray_id().size()==1){
                ind = 0;
            }
            else{
                ind = alea.nextInt((mesa_voto.getDesk().getArray_id().size()) + 1);
            }
            for (int i = 0; i < mesa_voto.getDesk().getArray_id().size(); i++) {
                System.out.println("Elemento "+i+" "+mesa_voto.getDesk().getArray_id().get(i));
            }
            //formar mensagem para enviar ao client
            System.out.println(mesa_voto.getDesk().getArray_id().get(ind));
            mesa_voto.setMensagens("type|connected;id|"+mesa_voto.getDesk().getArray_id().get(ind));
            System.out.println("Mensagem a enviar para o cliente: "+mesa_voto.getMensagens());
            mesa_voto.getDesk().getArray_id().remove(mesa_voto.getDesk().getArray_id().get(ind));
        }
        else if(sublista[1].compareTo("login")==0){
            for (int i = 1; i < lista.length-2; i++) {
                sublista = lista[i].split("\\|");
                add_mensagens.add(sublista[1]);
                //enviar para o rmi para verificar o username e o login 
            }
            sublista = lista[lista.length -1].split("\\|");
            id = sublista[1];

            add_mensagens.clear();
            return true;
        }

        return false;
    }


    //trata das mensagens que o cliente recebe
    public static String typeMessage_Client(String mensagem,int id){
        String [] sublista;
        String message="";
        String[] lista = mensagem.split(";");
        System.out.println(lista.length);
        sublista= lista[lista.length-1].split("\\|");
        //verificar se e o terminal que queremos
        if (id == Integer.parseInt(sublista[1])){
            sublista = lista[0].split("\\|");
            if (sublista[1].compareTo("login")==0){
                //verificar status
                sublista = lista[1].split("\\|");
                if (sublista[1].compareTo("sucessed")==0) return "sucessed";    
                else return "unsucessed";
            }
            else if(sublista[1].compareTo("connected")==0)return "choose";
            else if(sublista[1].compareTo("listaeleicoes")==0){
                for (int i = 1; i < lista.length-1; i++) {
                    message+=lista[i];
                    message+=";";
                    if (i==lista.length-1);
                    message+=lista[i];
                }
                return message;
            }

        } 
        else{
            return "false";
        }
        return "";
    }
}


class Gerar_Numeros {
    public static String gerar_ip(){
        int max = 239,min = 224;
        String ip="";
        for (int i = 0; i < 4; i++) {
            Random alea = new Random();
            int end = alea.nextInt((max - min) + 1) + min;
            if (i!=3) ip+=Integer.toString(end) +".";
            else ip+=Integer.toString(end);
            max = 255;
            min = 0;
        }
        return ip;
    }

    public static String gerar_port(int max,int min){
        Random alea = new Random();
        return Integer.toString(alea.nextInt((max - min) + 1) + min);
    }
}

/*
class Ask_Info_Eleitor implements Runnable{
    public Thread thread;
    private Scanner scanner;
    private Inputs inpu;
    private static MCServer mesa_voto;

    public Ask_Info_Eleitor(MCServer mesa_voto,String threadname,Scanner scanner,Inputs inpu){
        thread = new Thread(this,threadname);
        this.scanner = scanner;
        this.inpu = inpu;
        Ask_Info_Eleitor.mesa_voto=mesa_voto;
    }
    public static MCServer getMesa_voto() { return mesa_voto; }

    public void run(){
        String cart;
        System.out.println("entrou aqui\n");
        
    }

}*/