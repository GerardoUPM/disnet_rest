package edu.upm.midas.model.response.validations;
/**
 * Created by gerardo on 01/12/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className Validation
 * @see
 */
public class Validation {

    private boolean empty;
    private boolean found;
    private boolean internalError;


    public Validation() {
        this.empty = true;
        this.found = false;
        this.internalError = true;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public boolean isInternalError() {
        return internalError;
    }

    public void setInternalError(boolean internalError) {
        this.internalError = internalError;
    }
}
