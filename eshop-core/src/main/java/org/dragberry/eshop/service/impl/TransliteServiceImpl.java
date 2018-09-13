package org.dragberry.eshop.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.dragberry.eshop.service.TransliteService;
import org.springframework.stereotype.Service;

@Service
public class TransliteServiceImpl implements TransliteService {

	public static String cyr2lat(char ch){
		switch (ch){
			case 'А': return "A";
			case 'Б': return "B";
			case 'В': return "V";
			case 'Г': return "G";
			case 'Д': return "D";
			case 'Е': return "E";
			case 'Ё': return "JO";
			case 'Ж': return "ZH";
			case 'З': return "Z";
			case 'И': return "I";
			case 'Й': return "Y";
			case 'К': return "K";
			case 'Л': return "L";
			case 'М': return "M";
			case 'Н': return "N";
			case 'О': return "O";
			case 'П': return "P";
			case 'Р': return "R";
			case 'С': return "S";
			case 'Т': return "T";
			case 'У': return "U";
			case 'Ф': return "F";
			case 'Х': return "H";
			case 'Ц': return "C";
			case 'Ч': return "CH";
			case 'Ш': return "SH";
			case 'Щ': return "SCH";
			case 'Ъ': return "J";
			case 'Ы': return "Y";
			case 'Ь': return "";
			case 'Э': return "E";
			case 'Ю': return "JU";
			case 'Я': return "JA";
			default: return String.valueOf(ch);
		}
	}
	
	public static String cyr2lat(String str){
		StringBuilder sb = new StringBuilder(str.length()*2);
		for(char ch: str.toUpperCase().toCharArray()){
			sb.append(cyr2lat(ch));
		}
		return sb.toString();
	}
	
	@Override
	public String translite(String source) {
		return source != null ? cyr2lat(source) : StringUtils.EMPTY;
	}
	
	@Override
	public String transformToId(String source) {
		if (source != null) {
			return translite(source).toLowerCase().replaceAll("[^a-zA-Z0-9]+", "-");
		}
		return StringUtils.EMPTY;
	}

	public static void main(String[] args) {
		TransliteServiceImpl impl = new TransliteServiceImpl();
		System.out.println(impl.transformToId("bgtaбвгдеёЁжзийклмнопрстуфхцчшщъыьэюя"));
	}
}
