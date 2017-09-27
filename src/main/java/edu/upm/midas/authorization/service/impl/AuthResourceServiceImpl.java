package edu.upm.midas.authorization.service.impl;

import edu.upm.midas.authorization.client.AuthClient;
import edu.upm.midas.authorization.model.ValidationResponse;
import edu.upm.midas.authorization.service.AuthResourceService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
