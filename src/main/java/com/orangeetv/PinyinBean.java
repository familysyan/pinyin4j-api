package com.orangeetv;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import net.sourceforge.pinyin4j.*;
import net.sourceforge.pinyin4j.format.*;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

@ManagedBean
@RequestScoped
public class PinyinBean implements Serializable {
	private String value = "";

	public String getResult() {
		HanyuPinyinOutputFormat format1 = new HanyuPinyinOutputFormat();
		format1.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
		format1.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
		
		HanyuPinyinOutputFormat format2 = new HanyuPinyinOutputFormat();
		format2.setToneType(HanyuPinyinToneType.WITHOUT_TONE); 

    String value1 = getStringPinYin(value, format1);
    String value2 = getStringPinYin(value, format2);
		String result = "['" + value + "','" + value1 + "','" + value2 + "']";
    return result;
	}

	public void setValue(String value) {
		try {
			this.value = new String(value.getBytes("iso-8859-1"),"utf-8") ;
		} catch (UnsupportedEncodingException e) {
		  this.value = "encoding_error";
		}
	}

	public String getValue() {
		return this.value;
	}

	// 转换单个字符
	public String getCharacterPinYin(char c, HanyuPinyinOutputFormat format) {
		String[] pinyin = null;
		try {
			pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			return null;
		}

		// 如果c不是汉字，toHanyuPinyinStringArray会返回null
		if (pinyin == null)
			return null;

		// 只取一个发音，如果是多音字，仅取第一个发音
		return pinyin[0];
	}

	// 转换一个字符串
	public String getStringPinYin(String str, HanyuPinyinOutputFormat format) {
		StringBuilder sb = new StringBuilder();
		String tempPinyin = null;
		for (int i = 0; i < str.length(); ++i) {
			tempPinyin = getCharacterPinYin(str.charAt(i), format);
			if (tempPinyin == null) {
				// 如果str.charAt(i)非汉字，则保持原样
				sb.append(str.charAt(i));
			} else {
			  if (i != 0) {
				  sb.append(" ");
			  }
				sb.append(tempPinyin);
			}
		}
		return sb.toString();
	}
}
