package edu.upm.midas.data.relational.service.helperNative;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.upm.midas.data.relational.service.DocumentService;
import edu.upm.midas.common.util.Common;
import edu.upm.midas.common.util.UniqueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by gerardo on 14/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className DocumentHelper
 * @see
 */
@Service
public class DocumentHelperNative {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UrlHelperNative urlHelperNative;
    @Autowired
    private DocumentHelperNative documentHelperNative;

    @Autowired
    private UniqueId uniqueId;
    @Autowired
    private Common common;

    private static final Logger logger = LoggerFactory.getLogger(DocumentHelperNative.class);
    @Autowired
    ObjectMapper objectMapper;



}
