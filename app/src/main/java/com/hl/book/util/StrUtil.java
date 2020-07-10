package com.hl.book.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述:字符串帮助类
 * 作者:Leon
 * 时间:2017/11/17 0017
 */
public class StrUtil {

	/**
	 * 
	 * @return
	 */
	public static boolean isEmpty(String vStr) {
		return null == vStr || "".equals(vStr);
	}

	/**
	 * 
	 * @return
	 */
	public static int nullToInt0(Object vStr) {
		int str = 0;
		String v = StrUtil.nullToStr(vStr);
		if ("".equals(v)) {
			return str;
		}
		try {
			str = Integer.valueOf(v);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	/**
	 *
	 * @return
	 */
	public static int nullToIntf1(Object vStr) {
		int str = -1;
		String v = StrUtil.nullToStr(vStr);
		if ("".equals(v)) {
			return str;
		}
		try {
			str = Integer.valueOf(v);
		} catch (Exception e) {
			e.printStackTrace();
			return str;
		}
		return str;
	}
	/**
	 *
	 * @return
	 */
	public static float nullToFloat(Object vStr) {
		float str = 0;
		String v = StrUtil.nullToStr(vStr);
		if ("".equals(v)) {
			return str;
		}
		try {
			str = Float.valueOf(v);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 
	 * @return
	 */
	public static Long nullToLong(Object vStr) {
		Long str = 0L;
		String v = StrUtil.nullToStr(vStr);
		if ("".equals(v)) {
			return str;
		}
		try {
			str = Long.valueOf(v);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 
	 * @return
	 */
	public static Double nullToDouble(Object vStr) {
		Double str = 0.00;
		String v = StrUtil.nullToStr(vStr);
		if ("".equals(v)) {
			return str;
		}
		try {
			str = Double.valueOf(v);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 
	 * @return
	 */
	public static boolean nullToBoolean(Object vStr) {
		boolean str = false;
		String v = StrUtil.nullToStr(vStr);
		if ("".equals(v)) {
			return str;
		}
		try {
			str = Boolean.valueOf(v);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static String nullToStr(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString().trim();
	}
	/**
	 *
	 * @param obj
	 * @return
	 */
	public static String nullToStr(String obj, String defout) {
		if (isEmpty(obj)) {
			return defout;
		}
		return obj.toString().trim();
	}
	/**
	 *
	 * @param obj
	 * @return
	 */
	public static String nullToStrIni0(Object obj) {
		if (obj == null) {
			return "0";
		}
		if (isEmpty(obj.toString().trim())){
			return "0";
		}
		return obj.toString().trim();
	}

	public static int StringToInt(String s) {
		int tmp = 0;
		if (s == null)
			return 0;
		try {
			tmp = Integer.parseInt(s);
		} catch (Exception e) {
			tmp = 0;
		}
		return tmp;
	}

	public static float StringToFloat(String s) {
		float tmp = 0;
		if (s == null)
			return 0;
		try {
			tmp = Float.parseFloat(s);
		} catch (Exception e) {
			tmp = 0;
		}
		return tmp;
	}

	/**
	 * 
	 * @param format
	 * @return
	 */
	public static BigDecimal formatBigDecimal(Number bd, String format) {
		DecimalFormat df = new DecimalFormat(format);
		return new BigDecimal(df.format(bd));
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static BigDecimal nullToBigDecimal(Object obj) {
		if ("".equals(StrUtil.nullToStr(obj))) {
			obj = "0.00";
		}
		BigDecimal bd = new BigDecimal(obj.toString());
		return bd;
	}

	public static String encode(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String decode(String str) {
		try {
			return URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}

	public static boolean isUrl(String email) {

		Pattern pattern = Pattern.compile("^(https?://)?([a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+)+(/*[A-Za-z0-9/\\-_&:?\\+=//.%]*)*");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static boolean isEmail(String email) {
		Pattern pattern = Pattern.compile( "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");
		Matcher matcher = pattern.matcher(email.trim());
		return matcher.matches();
	}

	public static boolean isPhone(String phone) {
		Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(16[0-9])|(18[0-9])|(17[0-9]|(19[0-9])))\\d{8}$");
		Matcher matcher = pattern.matcher(phone);
		return matcher.matches();
	}

	public static boolean isBlank(String s) {
		if (s == null) {
			return true;
		}
		return Pattern.matches("\\s*", s);
	}

	public static boolean isContainChinese(String chineseStr) {
		char[] charArray = chineseStr.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if ((charArray[i] >= 0x4e00) && (charArray[i] <= 0x9fbb)) {
				return true;
			}
		}
		return false;
	}
	public static byte[] hex2byte(String str) { // 字符串转二进制
		if (str == null)
			return null;
		str = str.trim();
		int len = str.length();
		if (len == 0 || len % 2 == 1)
			return null;

		byte[] b = new byte[len / 2];
		try {
			for (int i = 0; i < str.length(); i += 2) {
				b[i / 2] = (byte) Integer
						.decode("0x" + str.substring(i, i + 2)).intValue();
			}
			return b;
		} catch (Exception e) {
			return null;
		}
	}
/*
	public static ArrayList<String> getPicPathListFromContent(String content) {
		ArrayList<String> list = new ArrayList<String>();
		Matcher m = Pattern.compile("\\<media (.*?) />").matcher(content);
		while (m.find()) {
			list.add(m.group(1));
		}
		return list;
	}
*/

	public static String getImgNameFromUrl(String imgUrl) {
		if (imgUrl == null) {
			return null;
		}
		String[] strs = imgUrl.split("/");
		return strs[strs.length - 1];
	}
	public static String getImgUrlByTag(String content) {
		if (StrUtil.isEmpty(content)||content.length()<2) {
			return "";
		}
		if (content.startsWith("http"))return content;
		return content.substring(content.indexOf("=")+1,content.length()-1);
	}
	public static String format2f(double d) {
		String num = "" ;
		DecimalFormat format = new DecimalFormat("0.00");
		try {
			num = format.format(d) ;
		}catch (Exception e){

		}
		return num;
	}
	public static String format1f(double d) {
		String num = "" ;
		DecimalFormat format = new DecimalFormat("0.0");
		try {
			num = format.format(d) ;
		}catch (Exception e){

		}
		if (num.contains(".0"))num = num.replace(".0","") ;
		return num;
	}
	public static String format2f(int d) {
		String num = "" ;
		DecimalFormat format = new DecimalFormat("00");
		try {
			num = format.format(d) ;
		}catch (Exception e){

		}
		return num;
	}
	private static final String[] mChina = {"一","二","三","四","五","六","七","八","九","十"} ;
	public static String getChinaNum(int index){
		String result = mChina[0] ;
		if (index>=0&&index<=9){
			result = mChina[index] ;
		}
		return result ;
	}

	public static boolean isSame(String str1, String... str2){
		if (StrUtil.isEmpty(str1))return false;
		if (str2.length==0)return false ;
		for (int i = 0; i < str2.length ; i++) {
			if (str1.equals(str2[i]))return true ;
		}
		return false ;
	}

}
