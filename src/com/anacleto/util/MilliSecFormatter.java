package com.anacleto.util;

import java.text.NumberFormat;

import org.apache.log4j.Logger;

import com.anacleto.base.Logging;

public class MilliSecFormatter {

	private static int  SEC  = 1000;
	private static int  MIN  = 60 * SEC;
	private static int  HOUR = 60 * MIN;
	private static long DAY  = 24L * HOUR;
	private static Logger log = Logging.getUserEventsLogger();
	
	public static String toString(long timeInMillis) {
		
		NumberFormat nf = NumberFormat.getInstance(); 
		nf.setMinimumIntegerDigits(2);

		StringBuffer sb = new StringBuffer();
		int days = (int) ( timeInMillis / DAY );
		sb.append(days);
		
		int remdr = (int) ( timeInMillis % DAY);
		
		int hours = (int) (remdr / HOUR);
		remdr = (int) (remdr % HOUR);
		sb.append(" " + nf.format(hours));

		int minutes = remdr / ( MIN );
		remdr = remdr % ( MIN );
		sb.append(":" + nf.format(minutes));
		
		int seconds = remdr / SEC;
		sb.append(":" + nf.format(seconds));

		int ms = remdr % SEC;
		if(ms > 0) {
			nf.setMinimumIntegerDigits(3);
			sb.append("." + nf.format(ms));
		} else {
			sb.append("." + ms);
		}
		
		return sb.toString();
	}
}
