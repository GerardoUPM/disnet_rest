package edu.upm.midas.data.relational.service.helperNative;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.upm.midas.data.relational.service.SemanticTypeService;
import edu.upm.midas.common.util.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by gerardo on 20/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className SemanticTypeHelper
 * @see
 */
@Service
public class SemanticTypeHelperNative {

    @Autowired
    private SemanticTypeService semanticTypeService;

    @Autowired
    private Common common;

    private static final Logger logger = LoggerFactory.getLogger(SemanticTypeHelperNative.class);
    @Autowired
    ObjectMapper objectMapper;







}
