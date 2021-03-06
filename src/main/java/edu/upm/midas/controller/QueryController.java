package edu.upm.midas.controller;

import edu.upm.midas.client_modules.authorization.token.component.JwtTokenUtil;
import edu.upm.midas.client_modules.authorization.token.service.TokenAuthorization;
import edu.upm.midas.common.util.Common;
import edu.upm.midas.common.util.TimeProvider;
import edu.upm.midas.constants.Constants;
import edu.upm.midas.service.jpa.helperNative.DiseaseHelperNative;
import edu.upm.midas.service.jpa.helperNative.SourceHelperNative;
import edu.upm.midas.enums.ApiErrorEnum;
import edu.upm.midas.model.Disease;
import edu.upm.midas.model.DisnetConcept;
import edu.upm.midas.model.response.ApiResponseError;
import edu.upm.midas.model.response.Configuration;
import edu.upm.midas.model.response.Parameter;
import edu.upm.midas.model.response.ResponseFather;
import edu.upm.midas.model.response.particular.*;
import edu.upm.midas.model.response.validations.TypeSearchValidation;
import edu.upm.midas.service.error.ErrorService;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 28/08/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className SourceController
 * @see
 */
@RestController
@RequestMapping("${my.service.rest.request.mapping.general.url}")
public class QueryController {

    @Autowired
    private TokenAuthorization tokenAuthorization;

    @Autowired
    private SourceHelperNative sourceHelper;
    @Autowired
    private DiseaseHelperNative diseaseHelper;

    @Autowired
    private TimeProvider timeProvider;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private Common common;
    @Autowired
    private ErrorService errorService;
    @Autowired
    private Constants constants;


    @RequestMapping(path = { "/query/diseaseListPageable" },
            method = RequestMethod.GET,
            params = {"token", "source", "version"})
    public Page<Disease> diseaseListPageable(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                             @RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,//Nombre de la fuente "wikipedia"
                                             @RequestParam(value = "version") @Valid @NotBlank @NotNull @NotEmpty String version,
                                             @RequestParam(value = "validated", required = false, defaultValue = "true") boolean validated,
                                             HttpServletRequest httpRequest, Device device, Pageable pageable) throws Exception {

        Date dataVersion = timeProvider.getSdf().parse(version);

        return diseaseHelper.getDiseasePageWithTheirCodes(source, dataVersion, pageable);
    }


    /**
     * @param token
     * @param httpRequest
     * @param device
     * @return
     * @throws Exception
     */
    @RequestMapping(path = { "/query/sourceList" },// OKOK
                    method = RequestMethod.GET,
                    params = {"token"})
    public SourcesResponse sourceList(/*@PathVariable(value = "operation") @Valid @NotBlank @NotNull @NotEmpty String operation,*/
                                      @RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                      HttpServletRequest httpRequest, Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        //ANTES SE COGIA EL TOKEN DEL HEADER String token = httpRequest.getHeader(constants.HEADER_PARAM_TOKEN_NAME);
        //System.out.println("token en el header: " + token);
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(), device);
        SourcesResponse response = new SourcesResponse();
        //Se forma la respuesta
        List<ApiResponseError> errorsFound = new ArrayList<>();
        List<String> sources = new ArrayList<>();
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
                sources = sourceHelper.getSources(errorsFound);
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
                            "No sources were found in the DISNET database.",
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

        return response;
    }


    /**
     * @param token
     * @param source
     * @param httpRequest
     * @param device
     * @return
     * @throws Exception
     */
    @RequestMapping(path = { "/query/versionList" },// OKOK
                    method = RequestMethod.GET,
                    params = {"token", "source"})
    public VersionsResponse versionList(/*@PathVariable(value = "operation") @Valid @NotBlank @NotNull @NotEmpty String operation,*/
                                        @RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                        @RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,
                                    HttpServletRequest httpRequest, Device device) throws Exception {
        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //gson.toJson(conceptList)
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        //String token = httpRequest.getHeader(constants.HEADER_PARAM_TOKEN_NAME);
        VersionsResponse response = new VersionsResponse();
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(), device);
        //</editor-fold>
        List<ApiResponseError> errorsFound = new ArrayList<>();
        List<String> versions = new ArrayList<>();
        //Se forma la respuesta
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());

        response.setVersions(versions);
        response.setVersionsCount(versions.size());
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()) {
            try {
                String start = timeProvider.getTimestampFormat();
                versions = sourceHelper.getVersions(errorsFound, source);
                String end = timeProvider.getTimestampFormat();
                if (versions.size() > 0) {//System.out.println("size: "+versions.size());
                    response.setVersions(versions);
                    response.setVersionsCount(versions.size());
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
                            "Source exception.",
                            "No versions were found for the source. Verify the DISNET source list.",
                            true,
                            new Parameter(Constants.SOURCE, true, false, source, ""));
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

        return response;
    }


    /**
     * @param token
     * @param source
     * @param version
     * @param validated
     * @param httpRequest
     * @param device
     * @return
     * @throws Exception
     *//*

    @RequestMapping(path = { "/query/diseaseList" },//disease name OKOK
            method = RequestMethod.GET,
            params = {"token", "source", "version"})
    public DiseaseListResponse diseaseList(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                           @RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,//Nombre de la fuente "wikipedia"
                                           @RequestParam(value = "version") @Valid @NotBlank @NotNull @NotEmpty String version,
                                           @RequestParam(value = "validated", required = false, defaultValue = "true") boolean validated,
                                           HttpServletRequest httpRequest, Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        DiseaseListResponse response = new DiseaseListResponse();
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(), device);
        List<ApiResponseError> errorsFound = new ArrayList<>();
        List<Parameter> parameters = new ArrayList<>();
        List<Disease> diseaseList = new ArrayList<>();
        //</editor-fold>
        //Se forma la respuesta
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());

        response.setDiseaseList(diseaseList);
        response.setDiseaseCount(diseaseList.size());
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()){
            try {
                Date dataVersion = timeProvider.getSdf().parse(version);
                //System.out.println(" SOURCE: " + source + " VERSION: " + dataVersion);
                //Validar versión y fuente
                TypeSearchValidation validation = diseaseHelper.sourceAndVersionValidation(errorsFound, parameters, source, dataVersion);
                if (!validation.isErrors()) {
                    String start = timeProvider.getTimestampFormat();
                    diseaseList = diseaseHelper.getAllDiseaseWithTheirCodes(errorsFound, source, dataVersion, validated);
                    String end = timeProvider.getTimestampFormat();
                    if (diseaseList.size() > 0) {
                        response.setDiseaseList(diseaseList);
                        response.setDiseaseCount(diseaseList.size());
                        response.setResponseCode(HttpStatus.OK.toString());
                        response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                    } else {
                        response.setResponseCode(HttpStatus.OK.toString());
                        response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                        errorService.insertApiErrorEnumGenericError(
                                errorsFound,
                                ApiErrorEnum.RESOURCE_NOT_FOUND,
                                "Disease list exception.",
                                "No diseases were found with the requested parameters. Check the list of DISNET sources and versions.",
                                true,
                                null);
                    }
                }else{
                    response.setResponseCode(ApiErrorEnum.INVALID_PARAMETERS.getKey());
                    response.setResponseMessage(ApiErrorEnum.INVALID_PARAMETERS.getDescription());
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
        //if (errorsFound.size() > 0)
        response.setErrorsFound(errorsFound);
        //if (parameters.size() > 0 && errorsFound.size() <= 0)
        //    response.setExtraInfo(parameters);

        return response;
    }
*/


    /**
     * @param token
     * @param source
     * @param version
     * @param validated
     * @param httpRequest
     * @param device
     * @param pageable
     * @return
     * @throws Exception
     */
    @RequestMapping(path = { "/query/diseaseList" /*"/query/diseaseListPageableResponse"*/ },
            method = RequestMethod.GET,
            params = {"token", "source", "version"})
    public DiseaseListPageResponse diseaseListPageableResponse(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                                               @RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,//Nombre de la fuente "wikipedia"
                                                               @RequestParam(value = "version") @Valid @NotBlank @NotNull @NotEmpty String version,
                                                               @RequestParam(value = "validated", required = false, defaultValue = "true") boolean validated,
                                                               HttpServletRequest httpRequest, Device device, Pageable pageable) throws Exception {

        DiseaseListPageResponse response = new DiseaseListPageResponse(new ArrayList<>(), pageable, 0);
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(), device);
        List<ApiResponseError> errorsFound = new ArrayList<>();
        List<Parameter> parameters = new ArrayList<>();
        //</editor-fold>
        //Si la autorización es exitosa se completa la respuesta
        if (responseFather.isAuthorized()){
            try {
                Date dataVersion = timeProvider.getSdf().parse(version);
                //System.out.println(" SOURCE: " + source + " VERSION: " + dataVersion);
                //Validar versión y fuente
                TypeSearchValidation validation = diseaseHelper.sourceAndVersionValidation(errorsFound, parameters, source, dataVersion);
                if (!validation.isErrors()) {
                    String start = timeProvider.getTimestampFormat();
                    response = diseaseHelper.getDiseasePageWithTheirCodesPages(errorsFound, source, dataVersion, validated, responseFather, pageable);
                    String end = timeProvider.getTimestampFormat();
                    if (response.getContent().size() > 0) {
                        response.setResponseCode(HttpStatus.OK.toString());
                        response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                    } else {
                        response.setResponseCode(HttpStatus.OK.toString());
                        response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                        errorService.insertApiErrorEnumGenericError(
                                errorsFound,
                                ApiErrorEnum.RESOURCE_NOT_FOUND,
                                "Disease list exception.",
                                "No diseases were found with the requested parameters. Check the list of DISNET sources and versions.",
                                true,
                                null);
                    }
                }else{
                    response.setResponseCode(ApiErrorEnum.INVALID_PARAMETERS.getKey());
                    response.setResponseMessage(ApiErrorEnum.INVALID_PARAMETERS.getDescription());
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
        //if (errorsFound.size() > 0)
        response.setErrorsFound(errorsFound);
        //if (parameters.size() > 0 && errorsFound.size() <= 0)
        //    response.setExtraInfo(parameters);

        return response;
    }


    /**
     * @param token
     * @param source
     * @param version
     * @param diseaseName
     * @param diseaseCode
     * @param typeCode
     * @param validated
     * @param excludeSemanticTypes
     * @param forceSemanticTypes
     * @param matchExactName
     * @param httpRequest
     * @param device
     * @return
     * @throws Exception
     */
    @RequestMapping(path = { "/query/disnetConceptList" },//disease name | Before path: findingList OKOK
            method = RequestMethod.GET,
            params = {"token", "source", "version"})
    public DiseaseListResponse disnetConceptList(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                              @RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,//Nombre de la fuente "wikipedia"
                                              @RequestParam(value = "version") @Valid @NotBlank @NotNull @NotEmpty String version,
                                              @RequestParam(value = "diseaseName", required = false, defaultValue = "") String diseaseName,
                                              @RequestParam(value = "diseaseCode", required = false, defaultValue = "") String diseaseCode,
                                              @RequestParam(value = "typeCode", required = false, defaultValue = "") String typeCode,
                                              @RequestParam(value = "validated", required = false, defaultValue = "true") boolean validated,
                                              @RequestParam(value = "excludeSemanticTypes", required = false, defaultValue = "") String excludeSemanticTypes,
                                              @RequestParam(value = "forceSemanticTypes", required = false, defaultValue = "") String forceSemanticTypes,
                                              @RequestParam(value = "matchExactName", required = false, defaultValue = "false") boolean matchExactName,
                                              @RequestParam(value = "detectionInformation", required = false, defaultValue = "false") boolean detectionInformation,
                                              @RequestParam(value = "includeCode", required = false, defaultValue = "false") boolean includeCode,
                                              HttpServletRequest httpRequest, Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        DiseaseListResponse response = new DiseaseListResponse();
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(), device);
        //</editor-fold>
        List<ApiResponseError> errorsFound = new ArrayList<>();
        List<Parameter> parameters = new ArrayList<>();
        List<Disease> diseases = new ArrayList<>();
        //Se forma la respuesta
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());

        response.setDiseaseCount(diseases.size());
        response.setDiseaseList(diseases);
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()){
            try {
                Date dataVersion = timeProvider.getSdf().parse(version);
                //System.out.println("DIS: " + disease + " SOURCE: " + source + " VERSION: " + dataVersion + " VAL: " + validated + " diseaseName: "+diseaseName);
                //Validación de los parametros de busqueda
                TypeSearchValidation validation = diseaseHelper.validateDiseaseSearchingParameters(errorsFound, parameters, source, dataVersion, diseaseName, diseaseCode, typeCode, excludeSemanticTypes, forceSemanticTypes, matchExactName);
                if (!validation.isErrors()) {
                    String start = timeProvider.getTimestampFormat();
                    diseases = diseaseHelper.getDiseasesAndTheirDisnetConcepts_2(errorsFound, source, dataVersion, diseaseName, diseaseCode, typeCode, validated, validation, matchExactName, detectionInformation, includeCode);
                    String end = timeProvider.getTimestampFormat();
                    if (diseases.size() > 0) {
                        response.setDiseaseCount(diseases.size());
                        response.setDiseaseList(diseases);
                        response.setResponseCode(HttpStatus.OK.toString());
                        response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                    } else {
                        response.setResponseCode(HttpStatus.OK.toString());
                        response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                        errorService.insertApiErrorEnumGenericErrorWithParameters(
                                errorsFound,
                                ApiErrorEnum.RESOURCES_NOT_FOUND,
                                "Disease and DISNET Concept list exception",
                                "No DISNET Concepts were found with the specified parameters.",
                                true,
                                null );
                    }
                }else{
                    response.setResponseCode(ApiErrorEnum.INVALID_PARAMETERS.getKey());
                    response.setResponseMessage(ApiErrorEnum.INVALID_PARAMETERS.getDescription());
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

        return response;
    }


    /**
     * @param token
     * @param source
     * @param version
     * @param httpRequest
     * @param device
     * @return
     * @throws Exception
     */
    @RequestMapping(path = { "/query/diseaseCount" },//disease name OKOK
            method = RequestMethod.GET,
            params = {"token", "source", "version"})
    public CountResponse numberDisease(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                     @RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,//Nombre de la fuente "wikipedia"
                                     @RequestParam(value = "version") @Valid @NotBlank @NotNull @NotEmpty String version,
                                     HttpServletRequest httpRequest, Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        CountResponse response = new CountResponse();
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(), device);
        //</editor-fold>
        List<ApiResponseError> errorsFound = new ArrayList<>();
        List<Parameter> parameters = new ArrayList<>();
        Integer numberOfDiseases = 0;
        //Se forma la respuesta
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());

        response.setCount(numberOfDiseases);
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()){
            try {
                Date dataVersion = timeProvider.getSdf().parse(version);
                //System.out.println(String.format(" SOURCE: " + source + " VERSION: " + dataVersion));
                //Validar versión y fuente
                TypeSearchValidation validation = diseaseHelper.sourceAndVersionValidation(errorsFound, parameters, source, dataVersion);
                if (!validation.isErrors()) {
                    String start = timeProvider.getTimestampFormat();
                    numberOfDiseases = diseaseHelper.getNumberDiseases(source, dataVersion);
                    String end = timeProvider.getTimestampFormat();
                    if (numberOfDiseases != null) {
                        response.setCount(numberOfDiseases);
                        response.setResponseCode(HttpStatus.OK.toString());
                        response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                    } else {
                        response.setResponseCode(HttpStatus.NOT_FOUND.toString());
                        response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                    }
                }else{
                    response.setResponseCode(ApiErrorEnum.INVALID_PARAMETERS.getKey());
                    response.setResponseMessage(ApiErrorEnum.INVALID_PARAMETERS.getDescription());
                }
            }catch (Exception e){
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() + e.getMessage());
            }
        }else {
            response.setResponseCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.toString());
            response.setResponseMessage(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.getReasonPhrase());
        }
        response.setErrorsFound(errorsFound);

        return response;
    }


    /**
     * @param token
     * @param source
     * @param version
     * @param validated
     * @param excludeSemanticTypes
     * @param forceSemanticTypes
     * @param limit
     * @param httpRequest
     * @param device
     * @return
     * @throws Exception
     */
    @RequestMapping(path = { "/query/diseaseWithMoreDisnetConcepts" },//OKOK
            method = RequestMethod.GET,
            params = {"token", "source", "version"})
    public DiseaseListResponse diseaseWithMoreSymptoms(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                        @RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,//Nombre de la fuente "wikipedia"
                                        @RequestParam(value = "version") @Valid @NotBlank @NotNull @NotEmpty String version,
                                        @RequestParam(value = "validated", required = false, defaultValue = "true") boolean validated,
                                        @RequestParam(value = "excludeSemanticTypes", required = false, defaultValue = "") String excludeSemanticTypes,
                                        @RequestParam(value = "forceSemanticTypes", required = false, defaultValue = "") String forceSemanticTypes,
                                        @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
                                        @RequestParam(value = "detectionInformation", required = false, defaultValue = "false") boolean detectionInformation,
                                        @RequestParam(value = "includeCode", required = false, defaultValue = "false") boolean includeCode,
                                        HttpServletRequest httpRequest, Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        DiseaseListResponse response = new DiseaseListResponse();
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(), device);
        //</editor-fold>
        List<ApiResponseError> errorsFound = new ArrayList<>();
        List<Parameter> parameters = new ArrayList<>();
        List<Disease> diseases = new ArrayList<>();
        //Se forma la respuesta
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());

        response.setDiseaseCount(diseases.size());
        response.setDiseaseList(diseases);
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()){
            try {
                Date dataVersion = timeProvider.getSdf().parse(version);
                //System.out.println(String.format(" SOURCE: " + source + " VERSION: " + dataVersion + " VAL: " + validated+ " LIMIT: " + limit));
                //Validar versión y fuente
                TypeSearchValidation validation = diseaseHelper.sourceAndVersionAndSemanticTypesValidation(errorsFound, parameters, source, dataVersion, excludeSemanticTypes, forceSemanticTypes);
                if (!validation.isErrors()) {
                    String start = timeProvider.getTimestampFormat();
                    diseases = diseaseHelper.getDiseasesWithMoreOrFewerDisnetConcepts(errorsFound, source, dataVersion, validated, limit, true, validation, detectionInformation, includeCode);
                    String end = timeProvider.getTimestampFormat();
                    if (diseases.size() > 0) {
                        response.setDiseaseCount(diseases.size());
                        response.setDiseaseList(diseases);
                        response.setResponseCode(HttpStatus.OK.toString());
                        response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                    } else {
                        response.setResponseCode(HttpStatus.OK.toString());
                        response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                        errorService.insertApiErrorEnumGenericErrorWithParameters(
                                errorsFound,
                                ApiErrorEnum.RESOURCES_NOT_FOUND,
                                "Disease and DISNET Concept list exception",
                                "No DISNET Concepts were found with the specified parameters.",
                                true,
                                null );
                    }
                }else{
                    response.setResponseCode(ApiErrorEnum.INVALID_PARAMETERS.getKey());
                    response.setResponseMessage(ApiErrorEnum.INVALID_PARAMETERS.getDescription());
                }
            }catch (Exception e){
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
        }else {
            response.setResponseCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.toString());
            response.setResponseMessage(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.getReasonPhrase());
        }
        response.setErrorsFound(errorsFound);

        return response;
    }


    /**
     * @param token
     * @param source
     * @param version
     * @param validated
     * @param excludeSemanticTypes
     * @param forceSemanticTypes
     * @param limit
     * @param httpRequest
     * @param device
     * @return
     * @throws Exception
     */
    @RequestMapping(path = { "/query/diseaseWithFewerDisnetConcepts" },//OKOK
            method = RequestMethod.GET,
            params = {"token", "source", "version"})
    public DiseaseListResponse diseaseWithFewerSymptoms(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                                           @RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,//Nombre de la fuente "wikipedia"
                                                           @RequestParam(value = "version") @Valid @NotBlank @NotNull @NotEmpty String version,
                                                           @RequestParam(value = "validated", required = false, defaultValue = "true") boolean validated,
                                                           @RequestParam(value = "excludeSemanticTypes", required = false, defaultValue = "") String excludeSemanticTypes,
                                                           @RequestParam(value = "forceSemanticTypes", required = false, defaultValue = "") String forceSemanticTypes,
                                                           @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
                                                           @RequestParam(value = "detectionInformation", required = false, defaultValue = "false") boolean detectionInformation,
                                                           @RequestParam(value = "includeCode", required = false, defaultValue = "false") boolean includeCode,
                                                           HttpServletRequest httpRequest, Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        DiseaseListResponse response = new DiseaseListResponse();
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(), device);
        //</editor-fold>
        List<ApiResponseError> errorsFound = new ArrayList<>();
        List<Parameter> parameters = new ArrayList<>();
        List<Disease> diseases = new ArrayList<>();
        //Se forma la respuesta
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());

        response.setDiseaseCount(diseases.size());
        response.setDiseaseList(diseases);
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()){
            try {
                Date dataVersion = timeProvider.getSdf().parse(version);
                //System.out.println(String.format(" SOURCE: " + source + " VERSION: " + dataVersion + " VAL: " + validated));
                //Validar versión y fuente
                TypeSearchValidation validation = diseaseHelper.sourceAndVersionAndSemanticTypesValidation(errorsFound, parameters, source, dataVersion, excludeSemanticTypes, forceSemanticTypes);
                if (!validation.isErrors()) {
                    String start = timeProvider.getTimestampFormat();
                    diseases = diseaseHelper.getDiseasesWithMoreOrFewerDisnetConcepts(errorsFound, source, dataVersion, validated, limit, false, validation, detectionInformation, includeCode);
                    String end = timeProvider.getTimestampFormat();
                    if (diseases.size() > 0) {
                        response.setDiseaseCount(diseases.size());
                        response.setDiseaseList(diseases);
                        response.setResponseCode(HttpStatus.OK.toString());
                        response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                    } else {
                        response.setResponseCode(HttpStatus.OK.toString());
                        response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                        errorService.insertApiErrorEnumGenericErrorWithParameters(
                                errorsFound,
                                ApiErrorEnum.RESOURCES_NOT_FOUND,
                                "Disease and DISNET Concept list exception",
                                "No DISNET Concepts were found with the specified parameters.",
                                true,
                                null );
                    }
                }else{
                    response.setResponseCode(ApiErrorEnum.INVALID_PARAMETERS.getKey());
                    response.setResponseMessage(ApiErrorEnum.INVALID_PARAMETERS.getDescription());
                }
            }catch (Exception e){
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
        }else {
            response.setResponseCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.toString());
            response.setResponseMessage(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.getReasonPhrase());
        }
        response.setErrorsFound(errorsFound);

        return response;
    }


    /**
     * @param token
     * @param source
     * @param version
     * @param validated
     * @param excludeSemanticTypes
     * @param forceSemanticTypes
     * @param limit
     * @param httpRequest
     * @param device
     * @return
     * @throws Exception
     */
    @RequestMapping(path = { "/query/mostCommonDisnetConcepts" },//disease name
            method = RequestMethod.GET,
            params = {"token", "source", "version"})
    public CommonDisnetConceptsResponse mostCommonSymptoms(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                                           @RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,//Nombre de la fuente "wikipedia"
                                                           @RequestParam(value = "version") @Valid @NotBlank @NotNull @NotEmpty String version,
                                                           @RequestParam(value = "validated", required = false, defaultValue = "true") boolean validated,
                                                           @RequestParam(value = "excludeSemanticTypes", required = false, defaultValue = "") String excludeSemanticTypes,
                                                           @RequestParam(value = "forceSemanticTypes", required = false, defaultValue = "") String forceSemanticTypes,
                                                           @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
                                                           HttpServletRequest httpRequest, Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        CommonDisnetConceptsResponse response = new CommonDisnetConceptsResponse();
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(), device);
        //</editor-fold>
        List<ApiResponseError> errorsFound = new ArrayList<>();
        List<Parameter> parameters = new ArrayList<>();
        List<DisnetConcept> symptoms = new ArrayList<>();
        //Se forma la respuesta
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());

        response.setDisnetConceptsCount(symptoms.size());
        response.setDisnetConcepts(symptoms);
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()){
            try {
                Date dataVersion = timeProvider.getSdf().parse(version);
                //System.out.println(String.format(" SOURCE: " + source + " VERSION: " + dataVersion + " VAL: " + validated));
                //Validar versión y fuente
                TypeSearchValidation validation = diseaseHelper.sourceAndVersionAndSemanticTypesValidation(errorsFound, parameters, source, dataVersion, excludeSemanticTypes, forceSemanticTypes);
                if (!validation.isErrors()) {
                    String start = timeProvider.getTimestampFormat();
                    symptoms = diseaseHelper.getMostOrLessCommonDisnetConcepts(errorsFound, source, dataVersion, validated, limit, true, validation);
                    String end = timeProvider.getTimestampFormat();
                    if (symptoms.size() > 0) {
                        response.setDisnetConceptsCount(symptoms.size());
                        response.setDisnetConcepts(symptoms);
                        response.setResponseCode(HttpStatus.OK.toString());
                        response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                    } else {
                        response.setResponseCode(HttpStatus.OK.toString());
                        response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                    }
                }else{
                    response.setResponseCode(ApiErrorEnum.INVALID_PARAMETERS.getKey());
                    response.setResponseMessage(ApiErrorEnum.INVALID_PARAMETERS.getDescription());
                }
            }catch (Exception e){
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
        }else {
            response.setResponseCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.toString());
            response.setResponseMessage(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.getReasonPhrase());
        }
        response.setErrorsFound(errorsFound);

        return response;
    }


    /**
     * @param token
     * @param source
     * @param version
     * @param validated
     * @param excludeSemanticTypes
     * @param forceSemanticTypes
     * @param limit
     * @param httpRequest
     * @param device
     * @return
     * @throws Exception
     */
    @RequestMapping(path = { "/query/lessCommonDisnetConcepts" },//disease name
            method = RequestMethod.GET,
            params = {"token", "source", "version"})
    public CommonDisnetConceptsResponse lessCommonSymptoms(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                                           @RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,//Nombre de la fuente "wikipedia"
                                                           @RequestParam(value = "version") @Valid @NotBlank @NotNull @NotEmpty String version,
                                                           @RequestParam(value = "validated", required = false, defaultValue = "true") boolean validated,
                                                           @RequestParam(value = "excludeSemanticTypes", required = false, defaultValue = "") String excludeSemanticTypes,
                                                           @RequestParam(value = "forceSemanticTypes", required = false, defaultValue = "") String forceSemanticTypes,
                                                           @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
                                                           HttpServletRequest httpRequest, Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        CommonDisnetConceptsResponse response = new CommonDisnetConceptsResponse();
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(), device);
        //</editor-fold>
        List<ApiResponseError> errorsFound = new ArrayList<>();
        List<Parameter> parameters = new ArrayList<>();
        List<DisnetConcept> symptoms = new ArrayList<>();
        //Se forma la respuesta
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());

        response.setDisnetConceptsCount(symptoms.size());
        response.setDisnetConcepts(symptoms);
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()){
            try {
                Date dataVersion = timeProvider.getSdf().parse(version);
                //System.out.println(String.format(" SOURCE: " + source + " VERSION: " + dataVersion + " VAL: " + validated));
                //Validar versión y fuente
                TypeSearchValidation validation = diseaseHelper.sourceAndVersionAndSemanticTypesValidation(errorsFound, parameters, source, dataVersion, excludeSemanticTypes, forceSemanticTypes);
                if (!validation.isErrors()) {
                    //String start = timeProvider.getTimestampFormat();String end = timeProvider.getTimestampFormat();
                    String start = timeProvider.getTimestampFormat();
                    symptoms = diseaseHelper.getMostOrLessCommonDisnetConcepts(errorsFound, source, dataVersion, validated, limit, false, validation);
                    String end = timeProvider.getTimestampFormat();
                    if (symptoms != null) {
                        response.setDisnetConceptsCount(symptoms.size());
                        response.setDisnetConcepts(symptoms);
                        response.setResponseCode(HttpStatus.OK.toString());
                        response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                    } else {
                        response.setResponseCode(HttpStatus.OK.toString());
                        response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                    }
                }else{
                    response.setResponseCode(ApiErrorEnum.INVALID_PARAMETERS.getKey());
                    response.setResponseMessage(ApiErrorEnum.INVALID_PARAMETERS.getDescription());
                }
            }catch (Exception e){
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
        }else {
            response.setResponseCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.toString());
            response.setResponseMessage(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.getReasonPhrase());
        }
        response.setErrorsFound(errorsFound);

        return response;
    }


    /**
     * @param token
     * @param source
     * @param version
     * @param diseaseName
     * @param matchExactName
     * @param limit
     * @param httpRequest
     * @param device
     * @return
     * @throws Exception
     */
    @RequestMapping(path = { "/query/searchByDiseaseName" },//disease name OKOK
            method = RequestMethod.GET,
            params = {"token", "source", "version", "diseaseName"})
    public DisnetConceptsResponse searchByDiseaseName(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                                     @RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,//Nombre de la fuente "wikipedia"
                                                     @RequestParam(value = "version") @Valid @NotBlank @NotNull @NotEmpty String version,
                                                     @RequestParam(value = "diseaseName") @Valid @NotBlank @NotNull @NotEmpty String diseaseName,
                                                     @RequestParam(value = "matchExactName", required = false, defaultValue = "false") boolean matchExactName,
                                                     @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
                                                     HttpServletRequest httpRequest, Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        DisnetConceptsResponse response = new DisnetConceptsResponse();
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(), device);
        //</editor-fold>
        List<ApiResponseError> errorsFound = new ArrayList<>();
        List<Parameter> parameters = new ArrayList<>();
        List<Disease> diseases = new ArrayList<>();
        //Se forma la respuesta
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());

        response.setDiseaseCount(diseases.size());
        response.setDiseaseList(diseases);
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()){
            try {
                Date dataVersion = timeProvider.getSdf().parse(version);
                //System.out.println(String.format(" SOURCE: " + source + " VERSION: " + dataVersion + " VAL: " + validated));
                //Validar versión y fuente
                TypeSearchValidation validation = diseaseHelper.sourceAndVersionAndDiseaseNameValidation(errorsFound, parameters, source, dataVersion, diseaseName, matchExactName);
                if (!validation.isErrors()) {
                    //String start = timeProvider.getTimestampFormat();String end = timeProvider.getTimestampFormat();
                    String start = timeProvider.getTimestampFormat();
                    diseases = diseaseHelper.getDiseasesWithTheirCodes(errorsFound, source, dataVersion, diseaseName, matchExactName);
                    String end = timeProvider.getTimestampFormat();
                    if (diseases != null) {
                        response.setDiseaseCount(diseases.size());
                        response.setDiseaseList(diseases);
                        response.setResponseCode(HttpStatus.OK.toString());
                        response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                    } else {
                        response.setResponseCode(HttpStatus.NOT_FOUND.toString());
                        response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                    }
                }else{
                    response.setResponseCode(ApiErrorEnum.INVALID_PARAMETERS.getKey());
                    response.setResponseMessage(ApiErrorEnum.INVALID_PARAMETERS.getDescription());
                }
            }catch (Exception e){
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
        }else {
            response.setResponseCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.toString());
            response.setResponseMessage(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.getReasonPhrase());
        }
        response.setErrorsFound(errorsFound);

        return response;
    }


    @RequestMapping(path = { "/query/metadata" },//disease name OKOK
            method = RequestMethod.GET,
            params = {"token", "source", "version"})
    public ConfigurationResponse metadata(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                                      @RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,//Nombre de la fuente "wikipedia"
                                                      @RequestParam(value = "version") @Valid @NotBlank @NotNull @NotEmpty String version,
                                                      HttpServletRequest httpRequest, Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        ConfigurationResponse response = new ConfigurationResponse();
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(), device);
        //</editor-fold>
        List<ApiResponseError> errorsFound = new ArrayList<>();
        List<Parameter> parameters = new ArrayList<>();
        List<Configuration> configurations = new ArrayList<>();
        //Se forma la respuesta
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());

        response.setConfigurationsCount(configurations.size());
        response.setConfigurationList(configurations);
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()){
            try {
                Date dataVersion = timeProvider.getSdf().parse(version);
                //System.out.println(String.format(" SOURCE: " + source + " VERSION: " + dataVersion + " VAL: " + validated));
                //Validar versión y fuente
                TypeSearchValidation validation = diseaseHelper.sourceAndVersionValidation(errorsFound, parameters, source, dataVersion);
                if (!validation.isErrors()) {
                    //String start = timeProvider.getTimestampFormat();String end = timeProvider.getTimestampFormat();
                    String start = timeProvider.getTimestampFormat();
                    configurations = diseaseHelper.getConfigurationList(errorsFound, source, dataVersion);
                    String end = timeProvider.getTimestampFormat();
                    if (configurations != null) {
                        response.setConfigurationsCount(configurations.size());
                        response.setConfigurationList(configurations);
                        response.setResponseCode(HttpStatus.OK.toString());
                        response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                    } else {
                        response.setResponseCode(HttpStatus.NOT_FOUND.toString());
                        response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                        common.saveQueryRuntime(responseFather.getInfoToken(), start, end);
                    }
                }else{
                    response.setResponseCode(ApiErrorEnum.INVALID_PARAMETERS.getKey());
                    response.setResponseMessage(ApiErrorEnum.INVALID_PARAMETERS.getDescription());
                }
            }catch (Exception e){
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
        }else {
            response.setResponseCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.toString());
            response.setResponseMessage(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.getReasonPhrase());
        }
        response.setErrorsFound(errorsFound);

        return response;
    }

}
