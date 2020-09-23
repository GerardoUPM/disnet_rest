package edu.upm.midas.controller;

import edu.upm.midas.client_modules.authorization.token.service.TokenAuthorization;
import edu.upm.midas.common.util.Common;
import edu.upm.midas.common.util.TimeProvider;
import edu.upm.midas.enums.ApiErrorEnum;
import edu.upm.midas.model.jpa.GeneBioWithoutId;
import edu.upm.midas.model.jpa.GeneBio;
import edu.upm.midas.model.response.ApiResponseError;
import edu.upm.midas.model.response.ResponseFather;
import edu.upm.midas.model.response.bio.*;
import edu.upm.midas.repository.jpa.GenesBioRepository;
import edu.upm.midas.service.error.ErrorService;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${my.service.rest.request.mapping.general.url}")
public class GenesBioController {

    @Autowired
    private GenesBioRepository genesBioRepository;

    @Autowired
    private TokenAuthorization tokenAuthorization;

    @Autowired
    private ErrorService errorService;

    @Autowired
    private TimeProvider timeProvider;

    @Autowired
    private Common common;



    @RequestMapping(path = { "/bio/genes" },
            method = RequestMethod.GET,
            params = {"token"})
    public GenesBioResponse getGenes(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                     HttpServletRequest httpRequest, Device device){
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(),device);
        List<ApiResponseError> errorsFound = new ArrayList<>();
        GenesBioResponse response = new GenesBioResponse();
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());
        //Respuesta hecha
        if (response.isAuthorized()) {
            try {
                String start = timeProvider.getTimestampFormat();
                List<GeneBio> genes = genesBioRepository.findGenes();
                String end = timeProvider.getTimestampFormat();
                if (genes.size() > 0){
                    response.setGenes(genes);
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
                            "No genes found it.",
                            "No genes were found in the DISNET database.",
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

    @RequestMapping(path = { "/bio/genes/{id}" },
            method = RequestMethod.GET,
            params = {"token"})
    public GeneBioResponse getGeneById(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                        @PathVariable("id") @Valid @NotBlank @NotNull @NotEmpty String id,
                                        HttpServletRequest httpRequest, Device device){
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(),device);
        List<ApiResponseError> errorsFound = new ArrayList<>();
        GeneBioResponse response = new GeneBioResponse();
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());
        //Respuesta hecha
        if (response.isAuthorized()) {
            try {
                String start = timeProvider.getTimestampFormat();
                GeneBio gene = genesBioRepository.findGeneById(id);
                String end = timeProvider.getTimestampFormat();
                if (gene != null){
                    GeneBioWithoutId geneWithoutId = new GeneBioWithoutId(gene.getGene_name(), gene.getGene_symbol());
                    response.setGeneBio(geneWithoutId);
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
                            "No genes found it.",
                            "No genes were found in the DISNET database for this gene id.",
                            true,
                            null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                errorService.insertInternatServerError(errorsFound, e, true);
            }
        }
        return response;
    }

    @RequestMapping(path = { "/bio/genes/{id}/diseases" },
            method = RequestMethod.GET,
            params = {"token"})
    public DiseasesByIdResponse getDiseaseByGenes(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                                  @PathVariable("id") @Valid @NotBlank @NotNull @NotEmpty String id,
                                                  HttpServletRequest httpRequest, Device device){
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(),device);
        List<ApiResponseError> errorsFound = new ArrayList<>();
        DiseasesByIdResponse response = new DiseasesByIdResponse();
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());
        //Respuesta hecha
        if (response.isAuthorized()) {
            try {
                String start = timeProvider.getTimestampFormat();
                List<String> genes = genesBioRepository.findDiseseasesByGenes(id);
                String end = timeProvider.getTimestampFormat();
                if (genes.size() > 0) {
                    response.setDiseases_Ids(genes);
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
                            "No diseases found it.",
                            "No findProteinsByGenes were found in the DISNET database for this gene.",
                            true,
                            null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                errorService.insertInternatServerError(errorsFound, e, true);
            }
        }
        return response;
    }

    @RequestMapping(path = { "/bio/genes/{id}/proteins" },
            method = RequestMethod.GET,
            params = {"token"})
    public ProteinsByIdResponse getProteinsByGenes(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                                   @PathVariable("id") @Valid @NotBlank @NotNull @NotEmpty String id,
                                                   HttpServletRequest httpRequest, Device device){
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(),device);
        List<ApiResponseError> errorsFound = new ArrayList<>();
        ProteinsByIdResponse response = new ProteinsByIdResponse();
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());
        //Respuesta hecha
        if (response.isAuthorized()) {
            try {
                String start = timeProvider.getTimestampFormat();
                List<String> proteins = genesBioRepository.findProteinsByGenes(id);
                String end = timeProvider.getTimestampFormat();
                if (proteins.size() > 0) {
                    response.setProteins_Ids(proteins);
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
                            "No proteins found it.",
                            "No proteins were found in the DISNET database for this gene.",
                            true,
                            null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                errorService.insertInternatServerError(errorsFound, e, true);
            }
        }
        return response;
    }

    @RequestMapping(path = { "/bio/genes/{id}/pathways" },
            method = RequestMethod.GET,
            params = {"token"})
    public PathwayByIdResponse getPathWayByGenes(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                                 @PathVariable("id") @Valid @NotBlank @NotNull @NotEmpty String id,
                                                 HttpServletRequest httpRequest, Device device){
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(),device);
        List<ApiResponseError> errorsFound = new ArrayList<>();
        PathwayByIdResponse response = new PathwayByIdResponse();
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());
        //Respuesta hecha
        if (response.isAuthorized()) {
            try {
                String start = timeProvider.getTimestampFormat();
                List<String> pathways = genesBioRepository.findPathwayByGenes(id);
                String end = timeProvider.getTimestampFormat();
                if (pathways.size() > 0) {
                    response.setPathways_Ids(pathways);
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
                            "No pathways found it.",
                            "No pathways were found in the DISNET database for this gene.",
                            true,
                            null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                errorService.insertInternatServerError(errorsFound, e, true);
            }
        }
        return response;
    }

    @RequestMapping(path = { "/bio/genes/{id}/variants" },
            method = RequestMethod.GET,
            params = {"token"})
    public VariantBiobyIdResponse getVariantsByGenes(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                                     @PathVariable("id") @Valid @NotBlank @NotNull @NotEmpty String id,
                                                     HttpServletRequest httpRequest, Device device){
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(),device);
        List<ApiResponseError> errorsFound = new ArrayList<>();
        VariantBiobyIdResponse response = new VariantBiobyIdResponse();
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());
        //Respuesta hecha
        if (response.isAuthorized()) {
            try {
                String start = timeProvider.getTimestampFormat();
                List<String> variants = genesBioRepository.findVariantsByGenes(id);
                String end = timeProvider.getTimestampFormat();
                if (variants.size() > 0) {
                    response.setVariant_Id(variants);
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
                            "No variants found it.",
                            "No variants were found in the DISNET database for this gene.",
                            true,
                            null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                errorService.insertInternatServerError(errorsFound, e, true);
            }
        }
        return response;
    }
}
