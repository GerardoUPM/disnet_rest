package edu.upm.midas.model.response;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by gerardo on 29/11/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className Error
 * @see
 */
public class Error {

    private int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String disnetCode;
    private String message;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDisnetCode() {
        return disnetCode;
    }

    public void setDisnetCode(String disnetCode) {
        this.disnetCode = disnetCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
