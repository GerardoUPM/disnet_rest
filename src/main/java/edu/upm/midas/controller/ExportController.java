package edu.upm.midas.controller;

import edu.upm.midas.common.util.TimeProvider;
import edu.upm.midas.data.relational.service.helperNative.DiseaseHelperNative;
import edu.upm.midas.export.excel.Excel;
import edu.upm.midas.model.Disease;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by gerardo on 22/12/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className ExportController
 * @see
 */
@RestController
@RequestMapping("${my.service.rest.request.mapping.general.url}")
public class ExportController {

    @Autowired
    private DiseaseHelperNative diseaseHelperNative;
    @Autowired
    private Excel excel;
    @Autowired
    private TimeProvider timeProvider;

    @RequestMapping(path = { "/export/diseaseList" },//disease name OKOK
            method = RequestMethod.GET,
            params = {"source", "version", "symptomsNumber"})
    public String diseaseList(@RequestParam(value = "symptomsNumber") @Valid @NotBlank @NotNull @NotEmpty int symptomsNumber,
                            @RequestParam(value = "source") @Valid @NotBlank @NotNull @NotEmpty String source,//Nombre de la fuente "wikipedia"
                            @RequestParam(value = "version") @Valid @NotBlank @NotNull @NotEmpty String version,
                            HttpServletRequest httpRequest,
                            Device device,
                            Model model) throws Exception {

        Date dataVersion = timeProvider.getSdf().parse(version);
        List<Disease> diseases = diseaseHelperNative.excelExport(source, dataVersion, symptomsNumber);
        if (diseases != null){
            int count = 1;
            for (Disease disease: diseases) {
                System.out.println("Disease ("+count+") " + disease.getName());
                excel.buildExcelDocument(disease);
                count++;
            }
        }

        return "Succes export";
    }
}
