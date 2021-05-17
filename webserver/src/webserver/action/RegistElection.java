package webserver.action;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import rmiserver.classes.Election;

public class RegistElection extends Action {
	private String election_state;    //  Professor, Estudante ou Funcionario
    private String title, description, start_date, start_hour, end_date, end_hour;

	@Override
	public String execute() throws RemoteException {
		if (election_state!=null && title!=null && description!=null && start_date!=null && start_hour!=null && end_date!=null && end_hour!=null) {
            if (title.isBlank() || description.isBlank() || start_date.isBlank() || start_hour.isBlank() || end_date.isBlank() || end_hour.isBlank()) return ERROR;
            
            LocalDate temp_date1= checkDateFormat(start_date), temp_date2= checkDateFormat(end_date);
            LocalTime temp_hour1= checkHourFormat(start_hour), temp_hour2= checkHourFormat(end_hour);
			
            if ((election_state.compareTo("Estudante")!=0 && election_state.compareTo("Professor")!=0 && election_state.compareTo("Funcionario")!=0) || temp_date1==null || temp_hour1==null || temp_date2==null || temp_hour2==null || !checkString(title)) return ERROR;
            
            if (LocalDate.now().compareTo(temp_date1)>0 || (LocalDate.now().compareTo(temp_date1)==0 && LocalTime.now().compareTo(temp_hour1)>0) || temp_date1.compareTo(temp_date2)>0 || (temp_date1.compareTo(temp_date2)==0 &&temp_hour1.compareTo(temp_hour2)>0)) return ERROR;
            
            Election new_election= new Election();
            new_election.setElectionState(election_state);
            new_election.setTitle(title);
            new_election.setDescription(description);
            new_election.setStarting(LocalDateTime.of(temp_date1, temp_hour1));
            new_election.setEnding(LocalDateTime.of(temp_date2, temp_hour2));
			boolean result= getRMIConnection().registElection(new_election);
			if (result) return SUCCESS;
			return ERROR;
		} return ERROR;
	}


    public void setElection_state(String election_state) {
        this.election_state = election_state;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
    public void setStart_hour(String start_hour) {
        this.start_hour = start_hour;
    }
    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
    public void setEnd_hour(String end_hour) {
        this.end_hour = end_hour;
    }
 
    
    public boolean checkString(String s) {
        if (s.isBlank()) return false;
        if (!Character.isJavaIdentifierStart(s.charAt(0))) return false;
        for (int i = 1; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) return false;
        } return true;
    }
    
    public LocalDate checkDateFormat(String date) {
        try { return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy")); } 
        catch (Exception e) { return null; }
    }

    public LocalTime checkHourFormat(String hour) {
        try { return LocalTime.parse(hour); } 
        catch (Exception e) { return null; }
    }
}
