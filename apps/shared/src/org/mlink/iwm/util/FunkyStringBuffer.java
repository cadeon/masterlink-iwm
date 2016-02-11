package org.mlink.iwm.util;
    
//string buffer used to eliminate unprintable chars
public class FunkyStringBuffer {
	private StringBuffer sb = new StringBuffer();
    
	public void setLength(int i) {
		sb.setLength(i);
	}
	public void append(String s) {
		// FIXME:This is a hack to avoid dane-bramage below
		char[] chars = s.toCharArray();
		int length = chars.length;
		if ( length < 2 ) {
			sb.append(s);
			return;
		};
		for (int i = 0; i < length-2; i++) {
			if (canDisplay(chars[i], chars[i+1])) {
					sb.append(chars[i]);
			}
		}
		sb.append(chars[length-2]);
		sb.append(chars[length-1]);
	}

	public boolean canDisplay(char c, char next) {
		// we print '||' as '|' and eliminate unprintable chars
	   if (c == '|' && next == '|')	return false;
	   return (c >0x1F && c < 0x7F); 
	}

	public String toString() {
		return sb.toString();
	}
	
	public String funkify(String s) {  
        if (s == null || s.length() == 0) return "";      
        this.setLength(0);
        this.append(s);
        return this.toString();
	}
}