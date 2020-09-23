package edu.upm.midas.controller;

import edu.upm.midas.client_modules.authorization.token.service.TokenAuthorization;
import edu.upm.midas.common.util.Common;
import edu.upm.midas.common.util.TimeProvider;
import edu.upm.midas.enums.ApiErrorEnum;
import edu.upm.midas.model.jpa.VariantBio;
import edu.upm.midas.model.jpa.VariantBioWithoutId;
import edu.upm.midas.model.response.ApiResponseError;
import edu.upm.midas.model.response.ResponseFather;
import edu.upm.midas.model.response.bio.DiseasesByIdResponse;
import edu.upm.midas.model.response.bio.GeneBiobyIdResponse;
import edu.upm.midas.model.response.bio.VariantBioResponse;
import edu.upm.midas.model.response.bio.VariantsBioResponse;
import edu.upm.midas.repository.jpa.VariantsBioRepository;
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
public class VariantsBioController {

    @Autowired
    private VariantsBioRepository variantsBioRepository;

    @Autowired
    private TokenAuthorization tokenAuthorization;

    @Autowired
    private ErrorService errorService;

    @Autowired
    private TimeProvider timeProvider;

    @Autowired
    private Common common;


    @RequestMapping(path = { "/bio/variants" },
            method = RequestMethod.GET,
            params = {"token"})
    public VariantsBioResponse getVariants(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                           HttpServletRequest httpRequest, Device device) {
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(),device);
        List<ApiResponseError> errorsFound = new ArrayList<>();
        VariantsBioResponse response = new VariantsBioResponse();
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());
        //Respuesta hecha
        if (response.isAuthorized()) {
            try {
                String start = timeProvider.getTimestampFormat();
                List<VariantBio> variants = variantsBioRepository.findVariants();
                String end = timeProvider.getTimestampFormat();
                if (variants.size() > 0){
                    response.setVariantBio(variants);
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
                            "No variants were found in the DISNET database.",
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

    @RequestMapping(path = { "/bio/variants/{id}" },
            method = RequestMethod.GET,
            params = {"token"})
    public VariantBioResponse getVariants(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                           @PathVariable("id") @Valid @NotBlank @NotNull @NotEmpty String id,
                                           HttpServletRequest httpRequest, Device device) {
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(),device);
        List<ApiResponseError> errorsFound = new ArrayList<>();
        VariantBioResponse response = new VariantBioResponse();
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());
        //Respuesta hecha
        if (response.isAuthorized()) {
            try {
                String start = timeProvider.getTimestampFormat();
                VariantBio variant = variantsBioRepository.findVariantById(id);
                String end = timeProvider.getTimestampFormat();
                if (variant != null){
                    VariantBioWithoutId variantBioWithoutId = new VariantBioWithoutId(variant.getChromosome(), variant.getChrposition(), variant.getConsequence());
                    response.setVariantBio(variantBioWithoutId);
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
                            "No variants were found in the DISNET database.",
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

    @RequestMapping(path = { "/bio/variants/{id}/diseases" },
            method = RequestMethod.GET,
            params = {"token"})
    public DiseasesByIdResponse getDiseasesbyVariantId(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                          @PathVariable("id") @Valid @NotBlank @NotNull @NotEmpty String id,
                                          HttpServletRequest httpRequest, Device device) {
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
                List<String> diseases = variantsBioRepository.findDiseasesById(id);
                String end = timeProvider.getTimestampFormat();
                if (diseases != null){
                    response.setDiseases_Ids(diseases);
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
                            "No disease were found in the DISNET database for this variant id.",
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

    @RequestMapping(path = { "/bio/variants/{id}/genes" },
            method = RequestMethod.GET,
            params = {"token"})
    public GeneBiobyIdResponse getGenesbyId(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                            @PathVariable("id") @Valid @NotBlank @NotNull @NotEmpty String id,
                                            HttpServletRequest httpRequest, Device device){
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(),device);
        List<ApiResponseError> errorsFound = new ArrayList<>();
        GeneBiobyIdResponse response = new GeneBiobyIdResponse();
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());
        //Respuesta hecha
        if (response.isAuthorized()) {
            try {
                String start = timeProvider.getTimestampFormat();
                List<Integer> genes = variantsBioRepository.findGenesById(id);
                String end = timeProvider.getTimestampFormat();
                if (genes.size() > 0){
                    response.setGenes_Id(genes);
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
                            "No genes were found in the DISNET database for this variant id.",
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
