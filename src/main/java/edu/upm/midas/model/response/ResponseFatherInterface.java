package edu.upm.midas.model.response;

import java.util.List;

/**
 * Created by gerardo on 02/11/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project get_diseases_list_rest
 * @className ResponseFather
 * @see
 */
public interface ResponseFatherInterface {


    public String getToken();

    public void setToken(String token);

    public boolean isAuthorized();

    public void setAuthorized(boolean authorized);

    public String getAuthorizationMessage();

    public void setAuthorizationMessage(String authorizationMessage);

    public String getResponseCode();

    public void setResponseCode(String responseCode);

    public String getResponseMessage();

    public void setResponseMessage(String responseMessage);

    public List<ApiResponseError> getErrorsFound();

    public void setErrorsFound(List<ApiResponseError> errorsFound);

    public List<Parameter> getExtraInfo();

    public void setExtraInfo(List<Parameter> extraInfo);

    public String getInfoToken();

    public void setInfoToken(String infoToken);



}
