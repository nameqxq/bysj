package com.quxiqi.bysj.util;

import java.io.Reader;
import java.util.Map;
import java.util.Random;

import oracle.sql.CLOB;

public class StringUtil {
	
	public static String getString(Map<?,?> valueMap, String keyName) {
		if (valueMap == null) {
			return "";
		} else {
			return valueMap.get(keyName) == null ? "" : valueMap.get(keyName).toString().trim();
		}

	}

	/**
	 * 去除目标对象的null数据,返回一个绝对空的字符串
	 * 
	 * @param target
	 * @return String
	 */
	public static String dnvString(Object target) {
		String str = String.valueOf(target).trim();
		return isNull(str) ? "": str.replace("{}", "");
	}

	public static boolean isNull(String str) {
		if (null == str)
			return true;
		return "".equals(str) || "null".equals(str);
	}

	public static boolean isNotNull(Object str) {
		return (null != str) && (!"".equals(str.toString().trim()));
	}

	/**
	 * 返回指定个数的随机数字字符串
	 * 
	 * @param n
	 * @return
	 */
	public static String getRandNumberStr(int n) {
		Random rnd = new Random();
		String pass = "0";
		int x = rnd.nextInt(9);

		while (x == 0) {
			x = rnd.nextInt(9);
		}
		pass = String.valueOf(x);
		for (int i = 1; i < n; ++i) {
			pass = pass + String.valueOf(rnd.nextInt(9));
		}
		return pass;
	}

	/**
	 * 随机返回数字+字母，一般用于密码生成
	 * 
	 * @param n
	 * @return
	 */
	public static String getRandomStr(int n) {
		String[] allStr = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "d", "e", "f", "g", "h", "i", "j", "k", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
		Random rnd = new Random();
		String randomStr = "";

		for (int i = 0; i < n; i++) {
			int strIndex = rnd.nextInt(allStr.length);
			if (strIndex >= allStr.length) {
				strIndex = allStr.length - 1;
			}

			randomStr = randomStr + String.valueOf(allStr[strIndex]);
		}

		return randomStr;
	}
	
	 public static String Clob2String(CLOB clob) {// Clob转换成String 的方法
		  String content = null;
		  StringBuffer stringBuf = new StringBuffer();
		  try {
		   int length = 0;
		   Reader inStream = clob.getCharacterStream(); // 取得大字侧段对象数据输出流
		   char[] buffer = new char[10];
		   while ((length = inStream.read(buffer)) != -1) // 读取数据库 //每10个10个读取
		   {
		    for (int i = 0; i < length; i++) {
		     stringBuf.append(buffer[i]);
		    }
		   }
		 
		   inStream.close();
		   content = stringBuf.toString();
		  } catch (Exception ex) {
		   System.out.println("ClobUtil.Clob2String:" + ex.getMessage());
		  }
		  return content;
		 }
}
