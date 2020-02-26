package edu.upm.midas.model.response.particular;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upm.midas.model.Disease;
import edu.upm.midas.model.response.ApiResponseError;
import edu.upm.midas.model.response.Parameter;
import edu.upm.midas.model.response.ResponseFatherInterface;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by gerardo on 22/11/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className DiseaseListResponse
 * @see
 */
public class DiseaseListPageResponse extends PageImpl<Disease> implements ResponseFatherInterface {

    private String token;
    private boolean authorized;
    private String authorizationMessage;

    private String responseCode;
    private String responseMessage;
    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ApiResponseError> errorsFound;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Parameter> extraInfo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String infoToken;





    public DiseaseListPageResponse(List<Disease> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public DiseaseListPageResponse(List<Disease> content) {
        super(content);
    }




    //============= implementation of interface =============

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean isAuthorized() {
        return this.authorized;
    }

    @Override
    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    @Override
    public String getAuthorizationMessage() {
        return this.authorizationMessage;
    }

    @Override
    public void setAuthorizationMessage(String authorizationMessage) {
        this.authorizationMessage = authorizationMessage;
    }

    @Override
    public String getResponseCode() {
        return this.responseCode;
    }

    @Override
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String getResponseMessage() {
        return this.responseMessage;
    }

    @Override
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public List<ApiResponseError> getErrorsFound() {
        return this.errorsFound;
    }

    @Override
    public void setErrorsFound(List<ApiResponseError> errorsFound) {
        this.errorsFound = errorsFound;
    }

    @Override
    public List<Parameter> getExtraInfo() {
        return this.extraInfo;
    }

    @Override
    public void setExtraInfo(List<Parameter> extraInfo) {
        this.extraInfo = extraInfo;
    }

    @Override
    public String getInfoToken() {
        return this.infoToken;
    }

    @Override
    public void setInfoToken(String infoToken) {
        this.infoToken = infoToken;
    }
}
