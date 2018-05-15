package edu.upm.midas.data.relational.service.helperNative;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.upm.midas.data.relational.service.HasTextService;
import edu.upm.midas.data.relational.service.TextService;
import edu.upm.midas.common.util.Common;
import edu.upm.midas.common.util.UniqueId;
import edu.upm.midas.model.Disease;
import edu.upm.midas.model.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 14/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className TextHelper
 * @see
 */
@Service
public class TextHelperNative {

    @Autowired
    private TextService textService;
    @Autowired
    private HasTextService hasTextService;

    @Autowired
    private DocumentHelperNative documentHelperNative;
    @Autowired
    private UrlHelperNative urlHelperNative;

    @Autowired
    private UniqueId uniqueId;
    @Autowired
    private Common common;

    private static final Logger logger = LoggerFactory.getLogger(TextHelperNative.class);
    @Autowired
    ObjectMapper objectMapper;


    public List<Text> excelExport(String sourceName, Date version, int textCount){
        return null;
    }




}
