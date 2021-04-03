import java.io.*;
import java.util.ArrayList;

public class ReadWrite implements Serializable{
    private static final long serialVersionUID = 1L;

    //escrever no ficheiro txt os ips do departamento
    public static void Write (String filename,String depar,String ip,String port,boolean flag){
        try {
            FileWriter fp = new FileWriter(filename,flag);
            if(depar.compareTo("")!=0 && ip.compareTo("")!=0 && port.compareTo("")!=0){
                fp.write(depar+" "+ip+" "+port+"\n");
                fp.close();
            }
            else fp.close();
        } catch (IOException e) { }
    }

    //buscar info para o client
    public static void read_ip_port(String filename,String depar,MCClient client){
        //se for a primeira utilização, le o ficheiro que contem as pessoas e coloca as no array pessoas que exsite no centro de investigação
        File f= new File(filename);
        if(f.exists() && f.isFile()){
            try{
                FileReader fr = new FileReader(f);
                BufferedReader br= new BufferedReader(fr);
                String line;
                while((line=br.readLine()) !=null){
                    String lista[] = line.split(" ");
                    if(lista[0].compareTo(depar)==0){
                        //linha correta
                        client.getVote_terminal().setDepar(depar);
                        client.getVote_terminal().setPort(lista[2]);
                        client.getVote_terminal().setIp(lista[1]);
                    }
                }
                br.close();
                fr.close();
            } catch(Exception e){ }
        }
    }

    public static void read_ip_port_terminal(String filename,MCServerData mesa){
        //se for a primeira utilização, le o ficheiro que contem as pessoas e coloca as no array pessoas que exsite no centro de investigação
        File f= new File(filename);
        if(f.exists() && f.isFile()){
            try{
                FileReader fr = new FileReader(f);
                BufferedReader br= new BufferedReader(fr);
                String line;
                
                while((line=br.readLine()) !=null){
                    System.out.println("Print: "+line);
                    if(line.compareTo("")==0) break;
                    else {
                        String lista[] = line.split(" ");
                        //linha correta
                        if(lista[0].compareTo(mesa.getDeparNome())==0) {
                            mesa.setDepar(mesa.getDeparNome());
                            mesa.setPort(lista[2]);
                            mesa.setIp(lista[1]);
                        }
                    }
                }
                br.close();
                fr.close();
            } catch(Exception e){ }
        }
    }
    
    //serve para quando uma mesa de voto/terminal de voto fechar
    //apagar a linha para n haver conflito quando 
    public static void remove_line(String filename,String depar){
        ArrayList <String> new_list = new ArrayList<>();
        File f= new File(filename);
        if(f.exists() && f.isFile()){
            try{
                FileReader fr = new FileReader(f);
                BufferedReader br= new BufferedReader(fr);
                String line;
                while((line=br.readLine()) !=null){
                    String lista[] = line.split(" ");
                    if(lista[0].compareTo(depar)!=0) new_list.add(line);
                }
                br.close();
                fr.close();
            } catch(Exception e){ }
        }
        if (new_list.size()==0) ReadWrite.Write(filename, "", "", "",false);
        else{
            for (int i = 0; i < new_list.size(); i++) {
                String lista[] = new_list.get(i).split(" ");
                if (i==0) ReadWrite.Write(filename, lista[0], lista[1], lista[2],false);
                else  ReadWrite.Write(filename, lista[0], lista[1], lista[2],true);
            }
        }
    }

    //verifica se esta algum cliente conectado na mesa de voto, se estiver vai buscar
    //no ficheiro txt
    public static String check_client_connect(String filename,String depar){
        String ip_port = "";
        File f= new File(filename);
        if(f.exists() && f.isFile()){
            try{
                FileReader fr = new FileReader(f);
                BufferedReader br= new BufferedReader(fr);
                String line;
                while((line=br.readLine())!=null){
                    String lista[] = line.split(" ");
                    //linha correta
                    if(lista[0].compareTo(depar)==0){
                        ip_port=lista[1]+" "+lista[2];
                        br.close();
                        fr.close();
                        return ip_port;
                    }
                }
                br.close();
                fr.close();
            } catch(Exception e){ return ""; }
        } return "";
    }
}
