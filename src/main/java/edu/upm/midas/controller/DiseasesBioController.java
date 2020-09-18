package edu.upm.midas.controller;

import edu.upm.midas.client_modules.authorization.token.service.TokenAuthorization;
import edu.upm.midas.common.util.Common;
import edu.upm.midas.common.util.TimeProvider;
import edu.upm.midas.enums.ApiErrorEnum;
import edu.upm.midas.model.DiseaseBio;
import edu.upm.midas.model.response.ApiResponseError;
import edu.upm.midas.model.response.ResponseFather;
import edu.upm.midas.model.response.bio.DiseaseBioResponse;
import edu.upm.midas.model.response.bio.DiseasesBioListResponse;
import edu.upm.midas.repository.jpa.DiseaseBioRepository;
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
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${my.service.rest.request.mapping.general.url}")
public class DiseasesBioController {
    /**
     *
     * Querys de Disnet_biolayer
     *
     * */
    @Autowired
    private DiseaseBioRepository diseaseBioRepository;

    @Autowired
    private TokenAuthorization tokenAuthorization;

    @Autowired
    private ErrorService errorService;

    @Autowired
    private TimeProvider timeProvider;

    @Autowired
    private Common common;
    /**
     * @return
     * @throws Exception
     */
    @RequestMapping(path = { "/bio/diseases" },
            params = {"token"},
            method = RequestMethod.GET)
    public DiseasesBioListResponse getListDiseases(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                                   HttpServletRequest httpRequest, Device device) throws Exception{

            ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(),device);
            List<ApiResponseError> errorsFound = new ArrayList<>();
            DiseasesBioListResponse response = new DiseasesBioListResponse();
            response.setAuthorized(responseFather.isAuthorized());
            response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
            response.setToken(responseFather.getToken());
            //Respuesta hecha
            if (response.isAuthorized()){
                try {
                    String start = timeProvider.getTimestampFormat();
                    List<DiseaseBio> diseases = diseaseBioRepository.findDiseases();
                    String end = timeProvider.getTimestampFormat();
                    if (diseases.size() > 0){
                        response.setDiseases(diseases);
                        response.setResponseCode(HttpStatus.OK.toString());
                        response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                    } else {
                        response.setResponseCode(HttpStatus.OK.toString());
                        response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                        errorService.insertApiErrorEnumGenericError(
                                errorsFound,
                                ApiErrorEnum.RESOURCES_NOT_FOUND,
                                "DiseaseBio list empty.",
                                "No diseases were found in the DISNET database.",
                                true,
                                null);
                    }

                } catch (Exception e){
                    response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
                    response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                    errorService.insertInternatServerError(errorsFound, e, true);
                }

            }
            return response;
    }

    @RequestMapping(path = { "/bio/diseases" },
            method = RequestMethod.GET,
            params = {"name" , "token"})
    public DiseaseBioResponse getDiseaseByName(@RequestParam(name = "name") @Valid @NotBlank @NotNull @NotEmpty String name,
                                       @RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                               HttpServletRequest httpRequest, Device device) throws Exception{
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(),device);
        List<ApiResponseError> errorsFound = new ArrayList<>();
        DiseaseBioResponse response = new DiseaseBioResponse();
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());
        //Respuesta hecha
        if (response.isAuthorized()) {
            try {
                String start = timeProvider.getTimestampFormat();
                DiseaseBio disease = diseaseBioRepository.findDiseaseByName(name);
                String end = timeProvider.getTimestampFormat();
                if (disease != null){
                    response.setDiseases(disease);
                    response.setResponseCode(HttpStatus.OK.toString());
                    response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                    common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                }else {
                    response.setResponseCode(HttpStatus.OK.toString());
                    response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                    common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                    errorService.insertApiErrorEnumGenericError(
                            errorsFound,
                            ApiErrorEnum.RESOURCES_NOT_FOUND,
                            "No disease found it.",
                            "No diseases were found in the DISNET database with this name.",
                            true,
                            null);
                }
            } catch (Exception e) {
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                errorService.insertInternatServerError(errorsFound, e, true);
            }
        }
        return response;
    }

}
