package org.mlink.iwm.util;
import java.util.*;
import java.sql.Timestamp;
import java.text.*;


public class DateUtil {
	final static long daylen = 86400 * 1000;
	static long epoch = 0l;
	static int epochDow =0;
	final static SimpleDateFormat SHORT_DATE_TIME_FORMAT =
		new SimpleDateFormat(Config.getProperty(Config.TIME_PATTERN));
	final static SimpleDateFormat SHORT_DATE_FORMAT =
		new SimpleDateFormat(Config.getProperty(Config.DATE_PATTERN));
	final static SimpleDateFormat SHORT_DATE_DAY_FORMAT =
		new SimpleDateFormat(Config.getProperty("day.pattern"));
	static {
		try {
			// This ain't fast, but it sure is clear.
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse("1970-01-01");
			epoch = date.getTime();
			// Yes, unix fans, time started on a thursday.
			epochDow = 4;
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	public static final String displayShortDateTime(java.util.Date d) {
		return SHORT_DATE_TIME_FORMAT.format(d);
	}
	public static final String displayShortDateTime(java.sql.Date d) {
		return displayShortDateTime(new Date(d.getTime()));
	}
	public static final String displayShortDate(java.util.Date d) {
		return SHORT_DATE_FORMAT.format(d);
	}
	public static final String displayShortDate(java.sql.Date d) {
		return SHORT_DATE_FORMAT.format(d);
	}
	public static final String displayShortDateDay(java.util.Date d) {
		return SHORT_DATE_DAY_FORMAT.format(d);
	}
	public static final String displayShortDateDay(java.sql.Date d) {
		return SHORT_DATE_DAY_FORMAT.format(d);
	}
	public static final java.sql.Date parseDate(String s) throws ParseException {
		java.util.Date d = SHORT_DATE_TIME_FORMAT.parse(s);
		return new java.sql.Date(d.getTime());
	}
	public static final java.sql.Date parseShortDate(String s) throws ParseException {
		java.util.Date d = SHORT_DATE_FORMAT.parse(s);
		return new java.sql.Date(d.getTime());
	}
	public static final java.sql.Date sqlDate(Timestamp t) {
		return new java.sql.Date(t.getTime());
	}
	
	static final int uniJulian(Date d) {
		return (int)((d.getTime()-epoch)/daylen);
	}
	static final Date unUniJulian(long uniJulian){
		return new Date(epoch+uniJulian*daylen);
	};
	static final class SkipCounter {
		final boolean[] mDays = new boolean[7];
		int mFirst;
		final int mLast;
		SkipCounter(Date first, Date last, boolean[] days){
			mFirst = uniJulian(first);
			System.out.println("mFirst="+mFirst+"("+first+")");
			mLast = uniJulian(last)-1;
			boolean valid = false;
			for ( int i = 0; i<7; i++ ) {
				valid = valid || (mDays[(i+4)%7] = days[i]);
			}
			if ( !valid )
				mFirst=mLast;
			else
				next();
		}
		Date next() {
			Date res = unUniJulian(mFirst);
			while ( !mDays[(++mFirst)%7] ) {
				continue;
			}
			return res;
		}
		boolean hasNext() {
			return mFirst < mLast;
		}
	}
	public static boolean[] getMap ( int value ) {
		System.out.println("value="+value);
		boolean[] result = new boolean[7];
		int mask = 1;
		int count=0;
		for ( int i = 0; i < 7; i++) {
			if ( result[i] = (value&mask)!=0 ) {
				count++;
			};
			mask*=2;
		}
		System.out.println("count="+count);
		return result;
	};
	public static void main ( String[] args ){
		boolean[] days = new boolean[7];
		Date f = new Date(epoch);
		System.out.println(f);
		System.out.println(f.getTime());
		System.out.println(uniJulian(f));
		System.out.println(unUniJulian(uniJulian(f)));
  		Date l = new Date(epoch+21*daylen);
		System.out.println(l);
		System.out.println(uniJulian(l));
		System.out.println(unUniJulian(uniJulian(f)));
		int [] ints = new int[] {
			1, 255, 3, 4, 127
		};
		for ( int i = 0; i < ints.length; i++ ) {
			SkipCounter sc = new SkipCounter(f,l,getMap(ints[i]));
			while(sc.hasNext()) {
				System.out.println(sc);
				System.out.println("COUNTER: "+sc.next());
			}
		}
	 }
	
	public static Date removeTime(Date d) {
	    if(d == null) {
	    	//throw new IllegalArgumentException("The argument 'date' cannot be null.");
	    	return null;
	    }
	    
    	String s = displayShortDate(d);
    	try {
			return SHORT_DATE_FORMAT.parse(s);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static String convertMinsToHMMfotmat(Integer mins){
		String hrsStr = Integer.toString(mins/60);
		Integer minsOp = mins%60;
		//String minsStr = Integer.toString(minsOp);
		
		String s = String.format("%02d", minsOp);
		return hrsStr+":"+s;
	}

//   	// days must be 7 long, and runs from monday through sunday.
//   	Collection dateRange(Date firstDay, Date lastDay, boolean[] days){
//   		int dow = (epochDow+firstTime)%7;
//   		while(!days[dow]) {
//   			++firstTime;
//   			switch(++dow){
//   				case 7: dow=0; break;
//   				case 4: return Collections.EMPTY_LIST;
//   				default: continue;
//   			}
//   		}
//   		if ( lastTime <= firstTime )
//   			return Collections.EMPTY_LIST;
//
//   		Date[] list = new Date[(int(6+lastTime-firstTime)/7)*setbits(days)];
//   	}
}
