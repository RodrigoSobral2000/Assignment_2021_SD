package webserver.action;

import java.rmi.RemoteException;
import rmiserver.classes.User;

public class RegistUser extends Action {
	private static final long serialVersionUID = 4L;
	private String user_type;    //  Professor, Estudante ou Funcionario
    private String name, password, address, phone_number;
    private String cc_number, cc_shelflife;
    private String college, department;

	@Override
	public String execute() throws RemoteException {
		if (user_type!=null && name!=null && password!=null && address!=null && phone_number!=null && cc_number!=null && cc_shelflife!=null && college!=null && department!=null) {
			if (name.isBlank() || password.isBlank() || address.isBlank() || phone_number.isBlank() || cc_number.isBlank() || cc_shelflife.isBlank() || college.isBlank() || department.isBlank()) return ERROR;
			if (user_type.compareTo("Estudante")==0 || user_type.compareTo("Professor")==0 || user_type.compareTo("Funcionario")==0) {
                User new_user = new User(user_type, name, password, address, phone_number, cc_number, cc_shelflife, college, department);
				boolean result= this.getRMIConnection().registarPessoa(new_user);
				System.out.println("resultado do registo: "+result);
				if (result) return SUCCESS;
				return ERROR;
            } return ERROR;
		} return ERROR;
	}	

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public void setCc_number(String cc_number) {
		this.cc_number = cc_number;
	}
	public void setCc_shelflife(String cc_shelflife) {
		this.cc_shelflife = cc_shelflife;
	}
	public void setCollege(String college) {
		this.college = college;
	}
	public void setDepartment(String department) {
		this.department = department;
	}

}
