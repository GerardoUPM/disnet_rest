package edu.upm.midas.model.response.validations;
/**
 * Created by gerardo on 04/12/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className CodeAndTypeCodeValidation
 * @see
 */
public class CodeAndTypeCodeValidation extends Validation {

    boolean codeEmpty;
    boolean typeCodeEmpty;
    boolean foundCode;
    boolean foundTypeCode;


    public boolean isCodeEmpty() {
        return codeEmpty;
    }

    public void setCodeEmpty(boolean codeEmpty) {
        this.codeEmpty = codeEmpty;
    }

    public boolean isTypeCodeEmpty() {
        return typeCodeEmpty;
    }

    public void setTypeCodeEmpty(boolean typeCodeEmpty) {
        this.typeCodeEmpty = typeCodeEmpty;
    }

    public boolean isFoundCode() {
        return foundCode;
    }

    public void setFoundCode(boolean foundCode) {
        this.foundCode = foundCode;
    }

    public boolean isFoundTypeCode() {
        return foundTypeCode;
    }

    public void setFoundTypeCode(boolean foundTypeCode) {
        this.foundTypeCode = foundTypeCode;
    }

    @Override
    public String toString() {
        return "CodeAndTypeCodeValidation{" +
                "codeEmpty=" + codeEmpty +
                ", typeCodeEmpty=" + typeCodeEmpty +
                ", foundCode=" + foundCode +
                ", foundTypeCode=" + foundTypeCode +
                '}';
    }
}
