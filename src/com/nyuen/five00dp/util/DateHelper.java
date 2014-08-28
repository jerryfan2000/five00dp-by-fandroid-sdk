package com.nyuen.five00dp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper
{
    private static SimpleDateFormat iso8601formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    private static SimpleDateFormat exifHeaderFormatter = new SimpleDateFormat("MMMM dd yyyy");
    
    
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;
    
    public static String getHeaderDate(String date) {
        Date parsedDate = parseISO8601(date);
        return exifHeaderFormatter.format(parsedDate);
    }
    
    public static Date parseISO8601(String date)
    {
        try {
            Date result = iso8601formatter.parse(date);
            return result;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String DateDifference(String date) {
        Date parsedDate = parseISO8601(date);
        long ms = System.currentTimeMillis() - parsedDate.getTime();
                
        StringBuffer text = new StringBuffer("");
        if (ms > DAY) {
          text.append(ms / DAY);
          if(ms / DAY <= 1)
              text.append(" day ");
          else
              text.append(" days ");
          ms %= DAY;
        }else if (ms > HOUR) {
          text.append("About ");
          text.append(ms / HOUR);
          if(ms / HOUR <= 1)
              text.append(" hour ");
          else
              text.append(" hours ");
          ms %= HOUR;
        }else if (ms > MINUTE) {
          text.append(ms / MINUTE);
          if(ms / MINUTE <= 1)
              text.append(" minute ");
          else
              text.append(" minutes ");
          ms %= MINUTE;
        }else if (ms > SECOND) {
          text.append("Less than a minute ");
          ms %= SECOND;
        }
        
        text.append("ago");
        
        return text.toString();
    }
    
}
