package webserver.action;

import java.rmi.RemoteException;
import java.util.Random;
import rmiserver.classes.User;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import javax.swing.*;
import uc.sd.apis.FacebookApi2;


public class AssociateFbAction extends Action {
    
    private static final Token EMPTY_TOKEN = null;
    public String autho_url;
    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
    private String code;
	@Override
	public String execute() throws RemoteException{
        final String secretState = "secret" + new Random().nextInt(999_999);
		// Replace these with your own api key and secret
        String apiKey = "2894802620848226";
        String apiSecret = "fcbe57b400b0a254657053e308521c9e";
        
        OAuthService service = new ServiceBuilder()
                                      .provider(FacebookApi2.class)
                                      .apiKey(apiKey)
                                      .apiSecret(apiSecret)
                                      .callback("http://localhost:8080/webserver/associaFb") // Do not change this.
                                      .state(secretState)
                                      .build();
    
    
        autho_url=service.getAuthorizationUrl(EMPTY_TOKEN);
        saveService(service);
        saveData("autho_url", autho_url);
        return SUCCESS;
         
	}

    public String associar_face() throws RemoteException{
        Token EMPTY_TOKEN= null;
        OAuthService service = getService();
        Verifier verifier = new Verifier(code);
        Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);

        // Now let's go and ask for a protected resource!
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service);
        service.signRequest(accessToken, request);
        Response response = request.send();
        String body = response.getBody();

        JSONObject obj = (JSONObject)JSONValue.parse(body); 
        System.out.println("Valor do id: " + obj.get("id").toString());
        System.out.println("Valor do nome: " + obj.get("name").toString());
        //savefacedata(obj.get("name").toString(), obj.get("id").toString());
        for(User user:getRMIConnection().getUsers()){
            if (user.getCc_number().compareTo(getLoggedUser().getCc_number())==0){
                user.setNome_id(obj.get("name").toString());
                user.setId_fb(obj.get("id").toString());
                if (getRMIConnection().updateUser(user)== true){
                    System.out.println("ASSOCIADO AO FACEBOOK COM SUCESSO");
                    JOptionPane.showMessageDialog(null,"ASSOCIADO AO FACEBOOK COM SUCESSO","Alert",JOptionPane.WARNING_MESSAGE);
                    return SUCCESS;
                }
                saveLoggedUser(user);
            }
        }
        System.out.println("ERRO A ASSOCIAR COM O FACEBBOK");
        JOptionPane.showMessageDialog(null,"ASSOCIAR AO FACEBOOK SEM SUCESSO","Alert",JOptionPane.ERROR_MESSAGE);
        return ERROR;
    }

    public String getAutho_url() {
        return autho_url;
    }

    public void setAutho_url(String autho_url) {
        this.autho_url = autho_url;
    }
	

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
