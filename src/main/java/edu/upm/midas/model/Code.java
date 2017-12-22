package edu.upm.midas.model;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by gerardo on 29/11/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className Code
 * @see
 */
public class Code {

    private String code;
    private String typeCode;//Es el resource name


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
}
