package edu.upm.midas.model.response;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by gerardo on 02/11/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project get_diseases_list_rest
 * @className ResponseFather
 * @see
 */
public class ResponseFather {

    private String token;
    private boolean authorized;
    private String authorizationMessage;

    private int responseCode;
    private String responseMessage;

    @JsonIgnore
    private String infoToken;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public String getAuthorizationMessage() {
        return authorizationMessage;
    }

    public void setAuthorizationMessage(String authorizationMessage) {
        this.authorizationMessage = authorizationMessage;
    }


    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getInfoToken() {
        return infoToken;
    }

    public void setInfoToken(String infoToken) {
        this.infoToken = infoToken;
    }
}
