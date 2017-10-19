package edu.upm.midas.controller;

import edu.upm.midas.authorization.model.ValidationResponse;
import edu.upm.midas.authorization.token.service.TokenAuthorization;
import edu.upm.midas.constants.Constants;
import edu.upm.midas.data.relational.service.helperNative.SourceHelperNative;
import edu.upm.midas.model.response.sources.SourcesResponse;
import edu.upm.midas.model.response.sources.VersionsResponse;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
@RequestMapping("/api/sources")
public class SourceController {

    @Autowired
    private SourceHelperNative sourceService;
    @Autowired
    private TokenAuthorization tokenAuthorization;
    @Autowired
    private Constants constants;


    //En el header cada petición se debe enviar el token de validación
    @RequestMapping(path = { "/" },
            method = RequestMethod.GET)
    public SourcesResponse getSources(HttpServletRequest httpRequest, Device device) throws Exception {
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        String token = httpRequest.getHeader(constants.HEADER_PARAM_TOKEN_NAME);
        System.out.println("token en el header: " + token);
        ValidationResponse validationResponse = tokenAuthorization.validateService(token, httpRequest.getServletPath(), httpRequest.getServletPath(), device);
        //</editor-fold>
        //Se forma la respuesta
        SourcesResponse sourcesResponse = new SourcesResponse();
        sourcesResponse.setAuthorization( validationResponse.isAuthorized() );
        sourcesResponse.setAuthorizationMessage( validationResponse.getMessage() );
        sourcesResponse.setToken( token );
        //Si la autorización es exitosa se completa la respuesta
        if (validationResponse.isAuthorized())
            sourcesResponse.setSources( sourceService.getSources() );

        return sourcesResponse;
    }


    @RequestMapping(path = { "/{source}/versions" },
            method = RequestMethod.GET)
    public VersionsResponse getVersions(@PathVariable(value = "source", required = true) @Valid @NotBlank @NotNull @NotEmpty String source,
                                    HttpServletRequest httpRequest, Device device) throws Exception {
        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //gson.toJson(conceptList)
        //<editor-fold desc="PROCESO DE AUTORIZACIÓN">
        String token = httpRequest.getHeader(constants.HEADER_PARAM_TOKEN_NAME);
        System.out.println("token en el header: " + token);
        ValidationResponse validationResponse = tokenAuthorization.validateService(token, httpRequest.getServletPath(), httpRequest.getServletPath(), device);
        //</editor-fold>
        //Se forma la respuesta
        VersionsResponse versionsResponse = new VersionsResponse();
        versionsResponse.setAuthorization( validationResponse.isAuthorized() );
        versionsResponse.setAuthorizationMessage( validationResponse.getMessage() );
        versionsResponse.setToken( token );

        //Si la autorización es exitosa se completa la respuesta
        if (validationResponse.isAuthorized())
            versionsResponse.setVersions( sourceService.getVersions( source ) );

        return versionsResponse;
    }

}
