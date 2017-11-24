package edu.upm.midas.controller;

import edu.upm.midas.authorization.token.service.TokenAuthorization;
import edu.upm.midas.common.util.TimeProvider;
import edu.upm.midas.constants.Constants;
import edu.upm.midas.data.relational.entities.edsssdb.Disease;
import edu.upm.midas.data.relational.service.helperNative.DiseaseHelperNative;
import edu.upm.midas.data.relational.service.helperNative.SourceHelperNative;
import edu.upm.midas.model.DiseaseSymptoms;
import edu.upm.midas.model.Finding;
import edu.upm.midas.model.SymptomWithCount;
import edu.upm.midas.model.response.ResponseFather;
import edu.upm.midas.model.response.particular.*;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
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
    private Constants constants;


    //En el header cada petición se debe enviar el token de validación
    @RequestMapping(path = { "/query/sourceList" },
                    method = RequestMethod.GET,
                    params = {"token"})
    public SourcesResponse sourceList(/*@PathVariable(value = "operation") @Valid @NotBlank @NotNull @NotEmpty String operation,*/
                                      @RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                      HttpServletRequest httpRequest, Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        //ANTES SE COGIA EL TOKEN DEL HEADER String token = httpRequest.getHeader(constants.HEADER_PARAM_TOKEN_NAME);
        //System.out.println("token en el header: " + token);
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getRequestURL().toString(), device);
        SourcesResponse response = new SourcesResponse();
        //Se forma la respuesta
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());
        //</editor-fold>
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()) {
            try {
                List<String> sources = sourceHelper.getSources();
                if (sources != null) {
                    response.setSources(sources);
                    response.setResponseCode(HttpStatus.OK.value());
                    response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                } else {
                    response.setResponseCode(HttpStatus.NOT_FOUND.value());
                    response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                }
            }catch (Exception e){
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
        }else {
            response.setResponseCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value());
            response.setResponseMessage(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.getReasonPhrase());
        }

        return response;
    }


    @RequestMapping(path = { "/query/versionList" },
                    method = RequestMethod.GET,
                    params = {"token", "source"})
    public VersionsResponse versionList(/*@PathVariable(value = "operation") @Valid @NotBlank @NotNull @NotEmpty String operation,*/
                                        @RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                        @RequestParam(value = "source", required = true) @Valid @NotBlank @NotNull @NotEmpty String source,
                                    HttpServletRequest httpRequest, Device device) throws Exception {
        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //gson.toJson(conceptList)
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        //String token = httpRequest.getHeader(constants.HEADER_PARAM_TOKEN_NAME);
        VersionsResponse response = new VersionsResponse();
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getRequestURL().toString(), device);
        //</editor-fold>
        //Se forma la respuesta
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()) {
            try {
                List<String> versions = sourceHelper.getVersions(source);
                if (versions != null) {
                    response.setVersions(versions);
                    response.setResponseCode(HttpStatus.OK.value());
                    response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                } else {
                    response.setResponseCode(HttpStatus.NOT_FOUND.value());
                    response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                }
            }catch (Exception e){
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
        }else {
            response.setResponseCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value());
            response.setResponseMessage(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.getReasonPhrase());
        }
        return response;
    }


    @RequestMapping(path = { "/query/diseaseList" },//disease name
            method = RequestMethod.GET,
            params = {"token", "source", "version"})
    public DiseaseListResponse diseaseList(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                           @RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,//Nombre de la fuente "wikipedia"
                                           @RequestParam(value = "version") @Valid @NotBlank @NotNull @NotEmpty String version,
                                           HttpServletRequest httpRequest, Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        DiseaseListResponse response = new DiseaseListResponse();
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getRequestURL().toString(), device);
        //</editor-fold>
        //Se forma la respuesta
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()){
            Date dataVersion = timeProvider.getSdf().parse(version);
            System.out.println(String.format(" SOURCE: " + source + " VERSION: " + dataVersion));
            try {
                List<edu.upm.midas.model.Disease> diseaseList = diseaseHelper.diseaseList(source, dataVersion);
                if (diseaseList != null) {
                    response.setDiseaseList(diseaseList);
                    response.setSize(diseaseList.size());
                    response.setResponseCode(HttpStatus.OK.value());
                    response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                } else {
                    response.setResponseCode(HttpStatus.NOT_FOUND.value());
                    response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                }
            }catch (Exception e){
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
        }else {
            response.setResponseCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value());
            response.setResponseMessage(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.getReasonPhrase());
        }
        return response;
    }


    @RequestMapping(path = { "/query/findingList" },//disease name
            method = RequestMethod.GET,
            params = {"token", "source", "version", "disease"})
    public SymptomsResponse findingList(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                        @RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,//Nombre de la fuente "wikipedia"
                                        @RequestParam(value = "version") @Valid @NotBlank @NotNull @NotEmpty String version,
                                        @RequestParam(value = "disease") @Valid @NotBlank @NotNull @NotEmpty String disease,
                                        @RequestParam(value = "validated", required = false, defaultValue = "true") boolean validated,
                                        HttpServletRequest httpRequest, Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        SymptomsResponse response = new SymptomsResponse();
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getRequestURL().toString(), device);
        //</editor-fold>
        //Se forma la respuesta
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()){
            Date dataVersion = timeProvider.getSdf().parse(version);
            System.out.println(String.format("DIS: " + disease + " SOURCE: " + source + " VERSION: " + dataVersion + " VAL: " + validated));
            try {
                List<Finding> findingList = diseaseHelper.getFindings(source, dataVersion, disease, validated);
                if (findingList != null) {
                    response.setDisease(disease);
                    response.setFindings(findingList);
                    response.setSize(findingList.size());
                    response.setResponseCode(HttpStatus.OK.value());
                    response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                } else {
                    response.setResponseCode(HttpStatus.NOT_FOUND.value());
                    response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                }
            }catch (Exception e){
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
        }else {
            response.setResponseCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value());
            response.setResponseMessage(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.getReasonPhrase());
        }
        return response;
    }


    @RequestMapping(path = { "/query/numberDisease" },//disease name
            method = RequestMethod.GET,
            params = {"token", "source", "version"})
    public CountResponse numberDisease(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                     @RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,//Nombre de la fuente "wikipedia"
                                     @RequestParam(value = "version") @Valid @NotBlank @NotNull @NotEmpty String version,
                                     HttpServletRequest httpRequest, Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        CountResponse response = new CountResponse();
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getRequestURL().toString(), device);
        //</editor-fold>
        //Se forma la respuesta
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()){
            Date dataVersion = timeProvider.getSdf().parse(version);
            System.out.println(String.format(" SOURCE: " + source + " VERSION: " + dataVersion));
            try {
                BigInteger numberOfDiseases = diseaseHelper.getNumberDiseases(source, dataVersion);
                if (numberOfDiseases != null) {
                    response.setCount(numberOfDiseases.intValue());
                    response.setResponseCode(HttpStatus.OK.value());
                    response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                } else {
                    response.setResponseCode(HttpStatus.NOT_FOUND.value());
                    response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                }
            }catch (Exception e){
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() + e.getMessage());
            }
        }else {
            response.setResponseCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value());
            response.setResponseMessage(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.getReasonPhrase());
        }
        return response;
    }


    @RequestMapping(path = { "/query/diseaseWithMoreSymptoms" },//disease name
            method = RequestMethod.GET,
            params = {"token", "source", "version"})
    public DiseaseSymptomsResponse diseaseWithMoreSymptoms(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                        @RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,//Nombre de la fuente "wikipedia"
                                        @RequestParam(value = "version") @Valid @NotBlank @NotNull @NotEmpty String version,
                                        @RequestParam(value = "validated", required = false, defaultValue = "true") boolean validated,
                                        @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
                                        HttpServletRequest httpRequest, Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        DiseaseSymptomsResponse response = new DiseaseSymptomsResponse();
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getRequestURL().toString(), device);
        //</editor-fold>
        //Se forma la respuesta
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()){
            Date dataVersion = timeProvider.getSdf().parse(version);
            System.out.println(String.format(" SOURCE: " + source + " VERSION: " + dataVersion + " VAL: " + validated+ " LIMIT: " + limit));
            try {
                List<DiseaseSymptoms> diseasesWithMoreFindings = diseaseHelper.getDiseasesWithMoreFindings(source, dataVersion, validated, limit);
                if (diseasesWithMoreFindings != null) {
                    response.setSize(diseasesWithMoreFindings.size());
                    response.setDiseaseList(diseasesWithMoreFindings);
                    response.setResponseCode(HttpStatus.OK.value());
                    response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                } else {
                    response.setResponseCode(HttpStatus.NOT_FOUND.value());
                    response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                }
            }catch (Exception e){
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
        }else {
            response.setResponseCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value());
            response.setResponseMessage(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.getReasonPhrase());
        }
        return response;
    }


    @RequestMapping(path = { "/query/diseaseWithFewerSymptoms" },//disease name
            method = RequestMethod.GET,
            params = {"token", "source", "version"})
    public DiseaseSymptomsResponse diseaseWithFewerSymptoms(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                                           @RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,//Nombre de la fuente "wikipedia"
                                                           @RequestParam(value = "version") @Valid @NotBlank @NotNull @NotEmpty String version,
                                                           @RequestParam(value = "validated", required = false, defaultValue = "true") boolean validated,
                                                           @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
                                                           HttpServletRequest httpRequest, Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        DiseaseSymptomsResponse response = new DiseaseSymptomsResponse();
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getRequestURL().toString(), device);
        //</editor-fold>
        //Se forma la respuesta
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()){
            Date dataVersion = timeProvider.getSdf().parse(version);
            System.out.println(String.format(" SOURCE: " + source + " VERSION: " + dataVersion + " VAL: " + validated));
            try {
                List<DiseaseSymptoms> diseasesWithMoreFindings = diseaseHelper.getDiseasesWithFewerFindings(source, dataVersion, validated, limit);
                if (diseasesWithMoreFindings != null) {
                    response.setSize(diseasesWithMoreFindings.size());
                    response.setDiseaseList(diseasesWithMoreFindings);
                    response.setResponseCode(HttpStatus.OK.value());
                    response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                } else {
                    response.setResponseCode(HttpStatus.NOT_FOUND.value());
                    response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                }
            }catch (Exception e){
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
        }else {
            response.setResponseCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value());
            response.setResponseMessage(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.getReasonPhrase());
        }
        return response;
    }


    @RequestMapping(path = { "/query/mostCommonSymptoms" },//disease name
            method = RequestMethod.GET,
            params = {"token", "source", "version"})
    public CommonFindingsResponse mostCommonSymptoms(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                                     @RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,//Nombre de la fuente "wikipedia"
                                                     @RequestParam(value = "version") @Valid @NotBlank @NotNull @NotEmpty String version,
                                                     @RequestParam(value = "validated", required = false, defaultValue = "true") boolean validated,
                                                     @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
                                                     HttpServletRequest httpRequest, Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        CommonFindingsResponse response = new CommonFindingsResponse();
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getRequestURL().toString(), device);
        //</editor-fold>
        //Se forma la respuesta
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()){
            Date dataVersion = timeProvider.getSdf().parse(version);
            System.out.println(String.format(" SOURCE: " + source + " VERSION: " + dataVersion + " VAL: " + validated));
            try {
                List<SymptomWithCount> symptoms = diseaseHelper.getMostCommonSymptoms(source, dataVersion, validated, limit);
                if (symptoms != null) {
                    response.setSize(symptoms.size());
                    response.setSymptomList(symptoms);
                    response.setResponseCode(HttpStatus.OK.value());
                    response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                } else {
                    response.setResponseCode(HttpStatus.NOT_FOUND.value());
                    response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                }
            }catch (Exception e){
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
        }else {
            response.setResponseCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value());
            response.setResponseMessage(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.getReasonPhrase());
        }
        return response;
    }


    @RequestMapping(path = { "/query/lessCommonSymptoms" },//disease name
            method = RequestMethod.GET,
            params = {"token", "source", "version"})
    public CommonFindingsResponse lessCommonSymptoms(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                                     @RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,//Nombre de la fuente "wikipedia"
                                                     @RequestParam(value = "version") @Valid @NotBlank @NotNull @NotEmpty String version,
                                                     @RequestParam(value = "validated", required = false, defaultValue = "true") boolean validated,
                                                     @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
                                                     HttpServletRequest httpRequest, Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        CommonFindingsResponse response = new CommonFindingsResponse();
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getRequestURL().toString(), device);
        //</editor-fold>
        //Se forma la respuesta
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());
        //Si la autorización es exitosa se completa la respuesta
        if (response.isAuthorized()){
            Date dataVersion = timeProvider.getSdf().parse(version);
            System.out.println(String.format(" SOURCE: " + source + " VERSION: " + dataVersion + " VAL: " + validated));
            try {
                List<SymptomWithCount> symptoms = diseaseHelper.getLessCommonSymptoms(source, dataVersion, validated, limit);
                if (symptoms != null) {
                    response.setSize(symptoms.size());
                    response.setSymptomList(symptoms);
                    response.setResponseCode(HttpStatus.OK.value());
                    response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
                } else {
                    response.setResponseCode(HttpStatus.NOT_FOUND.value());
                    response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                }
            }catch (Exception e){
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
        }else {
            response.setResponseCode(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value());
            response.setResponseMessage(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.getReasonPhrase());
        }
        return response;
    }




}
