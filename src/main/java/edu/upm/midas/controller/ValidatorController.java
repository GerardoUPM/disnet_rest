package edu.upm.midas.controller;
import edu.upm.midas.constants.Constants;
import edu.upm.midas.service.validator.Validator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * Created by gerardo on 30/05/2018.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className ValidatorController
 * @see
 */
@RestController
@RequestMapping("${my.service.rest.request.mapping.general.url}")
public class ValidatorController {

    @Autowired
    private Validator validator;


    /**
     * @param source
     * @return
     */
    @RequestMapping(path = { "/validator" },// OKOK
            method = RequestMethod.GET,
            params = {"source"})
    public String validator(@RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source) throws IOException, InvalidFormatException {

        if (source.equals(Constants.WIKIPEDIA_SOURCE))
            validator.validator(Constants.WIKIPEDIA_VALIDATION_FOLDER, 1);
        else if (source.equals(Constants.PUBMED_SOURCE))
            validator.validator(Constants.PUBMED_VALIDATION_FOLDER, 0);
        else
            System.out.println("Invalid option (source)");

        return "Successful validation";
    }

}
