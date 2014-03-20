package au.com.mitchhaley.fishjournal.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mitch on 8/03/14.
 */
public class DateTimeHelper {

    public static final String dateTimeDisplayFormat = "dd/MM/yyyy hh:mm";

    /**
     * @param date
     *            date in String
     * @param toFormat
     *            format to which you want to convert your <b>date</b> eg: if
     *            required format is 31 July 2011 then the toFormat should be
     *            <b>d MMMM yyyy</b>
     * @return formatted date
     */
    public static String convertDate(Date date,
                                     String toFormat) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(toFormat);
            return simpleDateFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String formatDate(long timeInMills, String toFormat) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(toFormat);
            Date date = new Date(timeInMills);
            return simpleDateFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

}
