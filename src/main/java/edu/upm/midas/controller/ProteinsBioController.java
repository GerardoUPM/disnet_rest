package edu.upm.midas.controller;

import edu.upm.midas.client_modules.authorization.token.service.TokenAuthorization;
import edu.upm.midas.common.util.Common;
import edu.upm.midas.common.util.TimeProvider;
import edu.upm.midas.enums.ApiErrorEnum;
import edu.upm.midas.model.jpa.PathwayBio;
import edu.upm.midas.model.jpa.ProteinBio;
import edu.upm.midas.model.response.ApiResponseError;
import edu.upm.midas.model.response.ResponseFather;
import edu.upm.midas.model.response.bio.GeneBiobyIdResponse;
import edu.upm.midas.model.response.bio.PathwayResponse;
import edu.upm.midas.model.response.bio.ProteinBioResponse;
import edu.upm.midas.model.response.bio.ProteinsBioResponse;
import edu.upm.midas.repository.jpa.GenesBioRepository;
import edu.upm.midas.repository.jpa.ProteinsBioRepository;
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
//
@RestController
@RequestMapping("${my.service.rest.request.mapping.general.url}")
public class ProteinsBioController {

    @Autowired
    private ProteinsBioRepository proteinsBioRepository;

    @Autowired
    private TokenAuthorization tokenAuthorization;

    @Autowired
    private ErrorService errorService;

    @Autowired
    private TimeProvider timeProvider;

    @Autowired
    private Common common;

    @RequestMapping(path = { "/bio/proteins" },
            method = RequestMethod.GET,
            params = {"token"})
    public ProteinsBioResponse getProteins(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
                                      HttpServletRequest httpRequest, Device device){
        ResponseFather responseFather = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getMethod(), httpRequest.getRequestURL().toString(),device);
        List<ApiResponseError> errorsFound = new ArrayList<>();
        ProteinsBioResponse response = new ProteinsBioResponse();
        response.setAuthorized(responseFather.isAuthorized());
        response.setAuthorizationMessage(responseFather.getAuthorizationMessage());
        response.setToken(responseFather.getToken());
        //Respuesta hecha
        if (response.isAuthorized()) {
            try {
                String start = timeProvider.getTimestampFormat();
                List<ProteinBio> proteins = proteinsBioRepository.findProteins();
                String end = timeProvider.getTimestampFormat();
                if (proteins.size() > 0){
                    response.setProteinBioList(proteins);
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
                e.printStackTrace();
                response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
                response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                errorService.insertInternatServerError(errorsFound, e, true);
            }
        }
        return response;
    }

    @RequestMapping(path = { "/bio/proteins/{id}/genes" },
            method = RequestMethod.GET,
            params = {"token"})
    public GeneBiobyIdResponse getGenesBy(@RequestParam(value = "token") @Valid @NotBlank @NotNull @NotEmpty String token,
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
                List<Integer> genes = proteinsBioRepository.findGenesById(id);
                String end = timeProvider.getTimestampFormat();
                if (genes.size() > 0) {
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

}
