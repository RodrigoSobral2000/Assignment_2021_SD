import java.util.ArrayList;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.util.*;
import java.io.*;
 
public class MCServer extends Thread{
    //Atributos
    private long SLEEP_TIME = 5000;
    private VoteDesk desk;
    private String mensagens;

    //private ArrayList<MCClient> vote_terminals= new ArrayList<>();
    //contrutor

    public MCServer(String ip, String port,String depar) {
        this.mensagens = "";
        this.desk = new VoteDesk(ip,port,depar);
    }

    //public ArrayList<MCClient> getVote_terminals() { return vote_terminals; }
    
    //getters
    public VoteDesk getDesk() { return desk; }
    public String getMensagens() { return mensagens; }
    
    //setters
    public void setMensagens(String mensagens) { this.mensagens = mensagens; }

    //public void setVote_terminals(ArrayList<MCClient> vote_terminals) { this.vote_terminals = vote_terminals; }

    public static void main(String[] args) {
       
        Scanner scanner = new Scanner(System.in);
        String cart,depar;
        Inputs inpu = new Inputs();
        String messag = "";
        //gerar aleatoriamente
        //while(true){
            //System.out.println("Insert the department to which the desk vote belongs: ");
            depar = inpu.askVariable(scanner,"Insert the department to which the desk vote belongs: " , 0);
            MCServer mesa_voto = new MCServer(Gerar_Numeros.gerar_ip(),Gerar_Numeros.gerar_port(1000,10),depar);
            /*SecMultServer mesa_voto_2 = new SecMultServer("", "", depar);
            ReadWrite.Write("VoteDesk.txt", mesa_voto.desk.getDeparNome(), mesa_voto.desk.getIp(),mesa_voto.desk.getPort());
            System.out.println("----Vote Desk from "+mesa_voto.desk.getDeparNome()+"----");
            cart  = inpu.askVariable(scanner, "Insert CC: ", 2);
            messag = "type|RmiVerification|cc"+cart;
            mesa_voto.setMensagens(messag);
            //verificacao no rmi se o eleitor esta registado
            mesa_voto.start();*/
            Handler_Message.typeMessage("type|login;username|qwev;password|vdfgbv",mesa_voto);
            //mesa_voto_2.start();      

        //}
    }

    

    public void run() {
        System.out.println("entrouuu");
        while(true){
            MulticastSocket socket = null;
            try {
                socket = new MulticastSocket();  // create socket without binding it (only for sending)
                Scanner keyboardScanner = new Scanner(System.in);
                while (true) {
                    String readKeyboard = keyboardScanner.nextLine();
                    byte[] buffer = readKeyboard.getBytes();

                    InetAddress group = InetAddress.getByName(getDesk().getIp());
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, Integer.parseInt(getDesk().getPort()));
                    socket.send(packet);

                    try { sleep((long) (Math.random() * SLEEP_TIME)); } catch (InterruptedException e) { }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }
        }
        
   }  
}


class SecMultServer extends Thread{
    private MCServer mcserver;

    //getters
    public MCServer getMcserver() { return mcserver; }

    public SecMultServer(String ip, String port,String depar){
        this.mcserver = new MCServer(ip,port,depar);
    }

    public void run() {
        File f= new File("TerminalVote.txt");
        System.out.println("entrei client->server");
        while (true){
            if (f.exists() && f.isFile()){
                ReadWrite.read_ip_port_terminal("TerminalVote.txt", getMcserver().getDesk().getDeparNome(), getMcserver());
                if (getMcserver().getDesk().getIp().compareTo("")!=0){
                    System.out.println("IP->"+getMcserver().getDesk().getIp());
                    System.out.println("Port->"+getMcserver().getDesk().getPort());
                    MulticastSocket socket = null;
                    try {
                        socket = new MulticastSocket(Integer.parseInt(getMcserver().getDesk().getPort()));  // create socket and bind it
                        InetAddress group = InetAddress.getByName(getMcserver().getDesk().getIp());
                        System.out.println(Integer.parseInt(getMcserver().getDesk().getPort())+getMcserver().getDesk().getIp());
                        socket.joinGroup(group);
                        while (true) {
                            System.out.println("fuck");
                            byte[] buffer = new byte[256];
                            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                            socket.receive(packet);
                            System.out.println("chegou\n");
                            System.out.println("(Server)Received packet from " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " with message:");
                            String message = new String(packet.getData(), 0, packet.getLength());
                            System.out.println(message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        socket.close();
                    }
                }
                
            }
        }
        
    } 
}

class Handler_Message{
    
    //trata das mensagens que o server recebe
    public static void typeMessage(String mensagem,MCServer mesa_voto){
        System.out.println(mensagem);
        String [] sublista;
        String[] lista = mensagem.split(";");
        ArrayList <String> add_mensagens = new ArrayList<>();
        sublista= lista[0].split("\\|");
        if (sublista[1].compareTo("RmiVerification")==0){
            //fazer algo
        }
        else if(sublista[1].compareTo("envia_id")==0){
            sublista= lista[1].split("\\|");
            mesa_voto.getDesk().getArray_id().add(Integer.parseInt(sublista[1]));
        }
        else if(sublista[1].compareTo("login")==0){
            for (int i = 1; i < lista.length; i++) {
                sublista = lista[i].split("\\|");
                add_mensagens.add(sublista[1]);
                //enviar para o rmi para verificar o username e o login
                add_mensagens.clear();
            }
        }
    }


    //trata das mensagens que o cliente recebe
    public static String typeMessage_Client(String mensagem,int id){
        System.out.println(mensagem);
        String [] sublista;
        String[] lista = mensagem.split(";");
        System.out.println(lista.length);
        sublista= lista[0].split("\\|");
        if (sublista[1].compareTo(Integer.toString(id))==0){
            return "choose";
        }
        else if (sublista[1].compareTo("login")==0){

        }
        
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