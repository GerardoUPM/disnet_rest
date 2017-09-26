package edu.upm.midas.controller;

import edu.upm.midas.data.relational.service.helperNative.SourceHelperNative;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
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


    @RequestMapping(path = { "/" },
            method = RequestMethod.GET)
    public List<String> getSources(HttpServletRequest httpRequest) throws Exception {
        return sourceService.getSources();
    }


    @RequestMapping(path = { "/{source}/versions" },
            method = RequestMethod.GET)
    public List<String> getVersions(@PathVariable(value = "source", required = true) @Valid @NotBlank @NotNull @NotEmpty String source,
                                    HttpServletRequest httpRequest) throws Exception {
        return sourceService.getVersions( source );
    }

}
