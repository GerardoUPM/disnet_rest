package edu.upm.midas.service.error;
import com.google.common.base.Throwables;
import edu.upm.midas.constants.Constants;
import edu.upm.midas.enums.ApiErrorEnum;
import edu.upm.midas.model.response.ApiResponseError;
import edu.upm.midas.model.response.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by gerardo on 30/11/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className ErrorService
 * @see
 */
@Component
public class ErrorService {


    public void insertAuthorizationError(List<ApiResponseError> errorsFound, boolean isForTheUser){
        ApiResponseError error = new ApiResponseError(ApiErrorEnum.UNAUTHORIZED, "Token authorization.", "Wrong token.", isForTheUser, new Parameter(Constants.TOKEN, true, false, "", ""));
        errorsFound.add(error);
    }


    public void insertInternatServerError(List<ApiResponseError> errorsFound, Exception ex, boolean isForTheUser){
        ApiResponseError error = new ApiResponseError(ApiErrorEnum.INTERNAL_SERVER_ERROR, Throwables.getRootCause(ex).getClass().getName(), ex.getMessage(), isForTheUser, (Parameter) null);
        errorsFound.add(error);
    }

    public void insertApiErrorEnumGenericError(List<ApiResponseError> errorsFound, ApiErrorEnum errorEnum, String exception, String errorDetail, boolean isForTheUser, Parameter parameter){
        ApiResponseError error = new ApiResponseError(errorEnum, exception, errorDetail, isForTheUser, parameter);
        errorsFound.add(error);
    }

    public void insertApiErrorEnumGenericErrorWithParameters(List<ApiResponseError> errorsFound, ApiErrorEnum errorEnum, String exception, String errorDetail, boolean isForTheUser, List<Parameter> parameters){
        ApiResponseError error = new ApiResponseError(errorEnum, exception, errorDetail, isForTheUser, parameters);
        errorsFound.add(error);
    }

    public void insertHttpStatusGenericError(List<ApiResponseError> errorsFound, HttpStatus httpStatus, String exception, String errorDetail, boolean isForTheUser, Parameter parameter){
        ApiResponseError error = new ApiResponseError(httpStatus, exception, errorDetail, isForTheUser, parameter);
        errorsFound.add(error);
    }

    public void insertGenericError(List<ApiResponseError> errorsFound, String returnCode, String returnStatus, String message, String exception, String errorSource, boolean isForTheUser, Parameter parameter){
        ApiResponseError error = new ApiResponseError(returnCode, returnStatus, message, exception, errorSource, isForTheUser, parameter);
        errorsFound.add(error);
    }

}
