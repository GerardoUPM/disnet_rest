package edu.upm.midas.common.util;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 * Created by gerardo on 04/05/2017.
 *
 * @author Gerardo Lagunes G. ${EMAIL}
 * @version ${<VERSION>}
 * @project ExtractionInformationDiseasesWikipedia
 * @className CurrentDate
 * @see
 */
@Service("date")
public class TimeProvider {

    private DateFormat sdf;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private DateTimeFormatter dtfsss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");

    public Date getNow(){
        //return new Date(117, 05, 29);
        return new Date(new java.util.Date().getTime());
    }

    public long getTimestampNumber(){
        return new Timestamp( System.currentTimeMillis() ).getTime();
    }

    public String getTime(){
        return String.format(new java.util.Date().toString(), dtf);
    }

    public String getTimeFormat(){
        return String.format(new java.util.Date().toString(), dtf);
    }

    public Timestamp getTimestamp(){return new Timestamp(System.currentTimeMillis());}

    public String getTimestampFormat() throws ParseException {
        java.util.Date date = new java.util.Date();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS").format(date);
    }


    public DateFormat getSdf() {
        return this.sdf = new SimpleDateFormat("yyyy-MM-dd");
    }

    public void setSdf(DateFormat sdf) {
        this.sdf = sdf;
    }

    public DateTimeFormatter getDtf() {
        return dtf;
    }

    public void setDtf(DateTimeFormatter dtf) {
        this.dtf = dtf;
    }

    public String dateFormatyyyMMdd(java.util.Date date){
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public String getNowFormatyyyyMMdd(){
        //return new Date(117, 05, 29);
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(new java.util.Date().getTime()));
    }

    public java.util.Date stringToDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }

}
