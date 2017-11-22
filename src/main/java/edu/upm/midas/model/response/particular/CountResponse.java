package edu.upm.midas.model.response.particular;
import edu.upm.midas.model.response.ResponseFather;

/**
 * Created by gerardo on 22/11/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project disnet_rest
 * @className CountResponse
 * @see
 */
public class CountResponse extends ResponseFather{

    private int count;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
