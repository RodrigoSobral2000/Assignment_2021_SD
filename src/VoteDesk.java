import java.util.ArrayList;

public class VoteDesk {

    private String ip;
    private String port;
    //n sei se preciso do terminal de voto
    private int n_terminal_vote;
    private String depar; 
    private ArrayList<Integer> array_id = new ArrayList<>();

    public VoteDesk(String ip, String port,String depar){
        this.ip = ip;
        this.port = port;
        this.n_terminal_vote =0;
        this.depar = depar;
        this.array_id = new ArrayList<>();
    }
    
    //getters
    public String getDeparNome() { return depar; }
    public String getIp() { return ip; }
    public int getN_terminal_vote() { return n_terminal_vote; }
    public String getPort() { return port; }
    public ArrayList<Integer> getArray_id() { return array_id; }

    //setters
    public void setDepar(String depar) { this.depar = depar; }
    public void setIp(String ip) { this.ip = ip; }
    public void setN_terminal_vote(int n_terminal_vote) { this.n_terminal_vote = n_terminal_vote; }
    public void setPort(String port) { this.port = port; }
    

    public void print() {
        System.out.println("ArrayList Clients: "+getArray_id().size()); 

        for(int i = 0; i < getArray_id().size(); i++) {   
            System.out.print(getArray_id().get(i));
        }   
    }
}
