package edu.upm.midas.service.jpa.helperNative;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.upm.midas.service.jpa.HasSymptomService;
import edu.upm.midas.service.jpa.SymptomService;
import edu.upm.midas.common.util.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by gerardo on 19/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className SemanticTypeHelper
 * @see
 */
@Service
public class SymptomHelperNative {


    @Autowired
    private SymptomService symptomService;
    @Autowired
    private SemanticTypeHelperNative semanticTypeHelperNative;
    @Autowired
    private HasSymptomService hasSymptomService;

    @Autowired
    private Common common;

    private static final Logger logger = LoggerFactory.getLogger(SymptomHelperNative.class);
    @Autowired
    ObjectMapper objectMapper;





}
