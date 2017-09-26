package edu.upm.midas.data.relational.service.helperNative;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.upm.midas.data.relational.service.SourceService;
import edu.upm.midas.common.util.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 12/06/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project edu.upm.midas
 * @className SourceHelper
 * @see
 */
@Service
public class SourceHelperNative {

    @Autowired
    private SourceService sourceService;
    @Autowired
    private UrlHelperNative urlHelperNative;
    @Autowired
    private Common common;

    private static final Logger logger = LoggerFactory.getLogger(SourceHelperNative.class);
    @Autowired
    ObjectMapper objectMapper;


    /**
     * @return
     */
    public List<String> getSources(){
        List<String> sources = new ArrayList<>();
        List<Object[]> sourceAllNativeList = sourceService.findAllNative();
        if (sourceAllNativeList  != null) {
            for (Object[] source : sourceAllNativeList) {
                sources.add((String) source[0]);
            }
        }
        return sources;
    }


    /**
     * @param source
     * @return
     */
    public List<String> getVersions(String source) {
        List<String> versions = new ArrayList<>();
        List<Date> versionAllNativeList = sourceService.findAllVersionsNative(source);
        if (versionAllNativeList != null){
            for (Date version : versionAllNativeList) {
                versions.add(version.toString());
            }
        }
        return versions;
    }


}
