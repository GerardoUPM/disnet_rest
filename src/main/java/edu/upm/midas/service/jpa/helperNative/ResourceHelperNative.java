package edu.upm.midas.service.jpa.helperNative;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.upm.midas.service.jpa.ResourceService;
import edu.upm.midas.common.util.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by gerardo on 15/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className ResourceHelper
 * @see
 */
@Service
public class ResourceHelperNative {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private Common common;

    private static final Logger logger = LoggerFactory.getLogger(ResourceHelperNative.class);
    @Autowired
    ObjectMapper objectMapper;






}
