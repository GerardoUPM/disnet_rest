package edu.upm.midas.controller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.upm.midas.client_modules.authorization.token.component.JwtTokenUtil;
import edu.upm.midas.client_modules.authorization.token.service.TokenAuthorization;
import edu.upm.midas.common.util.Common;
import edu.upm.midas.common.util.TimeProvider;
import edu.upm.midas.constants.Constants;
import edu.upm.midas.enums.ApiErrorEnum;
import edu.upm.midas.model.response.ApiResponseError;
import edu.upm.midas.model.response.ResponseFather;
import edu.upm.midas.model.response.analysis.DatabaseStatisticsResponse;
import edu.upm.midas.model.response.analysis.Source;
import edu.upm.midas.model.response.particular.SourcesResponse;
import edu.upm.midas.service.analysis.Descriptive;
import edu.upm.midas.service.jpa.helperNative.DiseaseHelperNative;
import edu.upm.midas.service.jpa.helperNative.SourceHelperNative;
import edu.upm.midas.service.error.ErrorService;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerardo on 30/10/2018.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className AnalisysController
 * @see
 */
@RestController
@RequestMapping("${my.service.rest.request.mapping.general.url}")
public class AnalisysController {

    @Autowired
    private TokenAuthorization tokenAuthorization;
    @Autowired
    private TimeProvider timeProvider;
    @Autowired
    private Descriptive descriptive;
    @Autowired
    private Common common;
    @Autowired
    private ErrorService errorService;


    @RequestMapping(path = { "/analysis/descriptive" },
            method = RequestMethod.GET,
            params = {"token"})
    public DatabaseStatisticsResponse getDatabaseStatictics(
            @RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
            HttpServletRequest httpRequest, Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(), device);
        //Se forma la respuesta
        List<ApiResponseError> errorsFound = new ArrayList<>();
        List<Source> sources = new ArrayList<>();
        DatabaseStatisticsResponse response = new DatabaseStatisticsResponse();
        String creationDate = timeProvider.getNowFormatyyyyMMdd();
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());

        response.setSources(sources);
        response.setSourceCount(sources.size());
        //</editor-fold>
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()) {
            try {
                //Se registra el tiempo de ejecución de la consulta
                String start = timeProvider.getTimestampFormat();
                sources = descriptive.getDatabaseStatistics(errorsFound, response);
                String end = timeProvider.getTimestampFormat();
                if (sources.size() > 0) {
                    response.setSources(sources);
                    response.setSourceCount(sources.size());
                    response.setResponseCode(HttpStatus.OK.toString());
                    response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                    common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                }else{
                    response.setResponseCode(HttpStatus.OK.toString());
                    response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                    common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                    errorService.insertApiErrorEnumGenericError(
                            errorsFound,
                            ApiErrorEnum.RESOURCES_NOT_FOUND,
                            "Source list empty.",
                            "Statistical information from sources is not charged or there are no data to analyze.",
                            true,
                            null);
                }
            }catch (Exception e){
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                errorService.insertInternatServerError(errorsFound, e, true);
            }
        }else {
            response.setResponseCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.toString());
            response.setResponseMessage(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.getReasonPhrase());
            errorService.insertAuthorizationError(errorsFound, true);
        }
        response.setErrorsFound(errorsFound);

        if (sources!=null){
            //<editor-fold desc="WriteJSON">
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            common.writeStatisticsJSONFile(gson.toJson(response), creationDate, Constants.DESCRIPTIVE_STATISTICS_FILE_NAME);
            //</editor-fold>
        }

        return response;
    }


    @RequestMapping(path = { "/analysis/descriptive/last/json" },
            method = RequestMethod.GET
            /*, params = {"snapshot"}*/)
    public DatabaseStatisticsResponse getLastDatabaseStatisticsJSON(
            /*@RequestParam(value = "snapshot") @Valid @NotBlank @NotNull @NotEmpty String snapshot,*/
            HttpServletRequest httpRequest, Device device) throws Exception {
        return descriptive.getLastDatabaseStatistics();
    }


}
