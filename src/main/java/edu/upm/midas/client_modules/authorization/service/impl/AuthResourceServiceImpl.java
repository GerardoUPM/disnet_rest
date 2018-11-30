package edu.upm.midas.client_modules.authorization.service.impl;

import edu.upm.midas.client_modules.authorization.client.AuthClient;
import edu.upm.midas.client_modules.authorization.model.UpdateQueryRuntimeRequest;
import edu.upm.midas.client_modules.authorization.model.ValidationResponse;
import edu.upm.midas.client_modules.authorization.service.AuthResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Created by gerardo on 17/08/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project eidw
 * @className TvpResourceServiceImpl
 * @see
 */
@Service
public class AuthResourceServiceImpl implements AuthResourceService {

    @Autowired
    private AuthClient authClient;

    @Override
    public ValidationResponse validationServiceByToken(String token) {
        return authClient.validationServiceByToken( token );
    }

    @Override
    public HttpStatus updateQueryRunTime(UpdateQueryRuntimeRequest request) {
        return authClient.updateQueryRunTime(request);
    }
}
