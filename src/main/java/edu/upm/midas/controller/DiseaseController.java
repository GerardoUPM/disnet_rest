package edu.upm.midas.controller;

import edu.upm.midas.client_modules.authorization.token.service.TokenAuthorization;
import edu.upm.midas.constants.Constants;
import edu.upm.midas.service.jpa.helperNative.DiseaseHelperNative;
import edu.upm.midas.common.util.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by gerardo on 28/08/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className VersionController
 * @see
 *
 */
@RestController
@RequestMapping("${my.service.rest.request.mapping.general.url}")
public class DiseaseController {

    @Autowired
    private TimeProvider timeProvider;
    @Autowired
    private DiseaseHelperNative diseaseService;
    @Autowired
    private TokenAuthorization tokenAuthorization;
    @Autowired
    private Constants constants;


/*
    @RequestMapping(path = { "/{disease}/findingsXXXXX" },//disease name
                    method = RequestMethod.GET,
                    params = {"source", "version"})
    public DisnetConceptsResponse getFindings(@PathVariable(value = "disease") String disease,
                                              @RequestParam(value = "source") String source,
                                              @RequestParam(value = "version") String version,
                                              @RequestParam(value = "validated", required = false, defaultValue = "true") boolean validated,
                                              HttpServletRequest httpRequest,
                                              Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        String token = httpRequest.getHeader(constants.HEADER_PARAM_TOKEN_NAME);
        System.out.println("token en el header: " + token);
        DisnetConceptsResponse disnetConceptsResponse = new DisnetConceptsResponse();
        */
/*ValidationResponse validationResponse = tokenAuthorization.validateService(token, httpRequest.getQueryString(), httpRequest.getRequestURL().toString(), device);
        //</editor-fold>
        //Se forma la respuesta
        disnetConceptsResponse.setAuthorization( validationResponse.isAuthorized() );
        disnetConceptsResponse.setAuthorizationMessage( validationResponse.getMessage() );
        disnetConceptsResponse.setToken( token );
        //Si la autorización es exitosa se completa la respuesta
        if (validationResponse.isAuthorized()){
            Date dataVersion = timeProvider.getSdf().parse(version);
            System.out.println(String.format("DIS: " + disease + " SOURCE: " + source + " VERSION: " + dataVersion + " VAL: " + validated));
            return diseaseService.getDisnetConcepts(source, dataVersion, disease, validated, disnetConceptsResponse);
        }*//*

        return disnetConceptsResponse;
    }
*/

}
