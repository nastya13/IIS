/**
 * 
 */
package testJava;

import info.debatty.java.stringsimilarity.Levenshtein;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Анастасия
 *
 */
public class Main {

	private static String getKey(String[] words, String[] dict, double accuracy) {
		Levenshtein l = new Levenshtein();
		if (accuracy > 0)
			for (int i = 0; i < dict.length; i++) {
				for (int j = 1; j < words.length; j++) {
					String str3="",str2="",str="";
					if(j+2 <words.length) str3 = words[j] + " " + words[j + 1] + " " + words[j + 2];
					if (j+1<words.length) str2 = words[j] + " " + words[j + 1];
					str = words[j];
					if (str3!=null && l.similarity(str3, dict[i]) > 0.7)
						return dict[i];
					if (str2!=null && l.similarity(str2, dict[i]) > 0.7)
						return dict[i];	  
					if (str!=null && l.similarity(str, dict[i]) > 0.7)
					
						return dict[i];
				}
			}
		return null;
	}
	private static String getKey(String word, String[] dict, double accuracy)
	{
		Levenshtein l = new Levenshtein();
		if (accuracy>0)
		for (int i=0;i<dict.length;i++)
		{
			String s = dict[i].substring(0, dict[i].length()-1);
			if (l.similarity(s, word) >= accuracy)
				return s;
		}
		return null;
	}
	private static String checkValue(String word, Map<String, String[]> m) {
		String[] val;
		Levenshtein l = new Levenshtein();
		for (String key : m.keySet()) {
			val = m.get(key);
			for (int i = 0; i < val.length; i++)
				if (l.similarity(word, val[i])>0.7)
					return key;
		}
		return null;
	}

	private static Map<String,String[]> createMap(String[] str)
	{
		Map<String,String[]> map = new HashMap<String, String[]>();
		for (int i = 0; i < str.length; i += 2) {
			String[] arr = str[i + 1].split(",");
			for (int j = 0; j < arr.length; j++){
				arr[j] = (arr[j].substring(0, arr[j].length())).trim();
			}

			map.put(str[i], arr);
		}
		return map;
	}
	
	private static String getKeySim(Map<String, String[]> map, String[] ask)
	{
		String[] val;
		for (String key : map.keySet()) 
		{
			val = map.get(key);
			String k = getKey(ask, val, 0.7);
			if (k!=null)
			{
				return key;
			}
		}
		return null;
	}
	
	private static String getKeySim(Map<String, String[]> map, String[] ask, String kkk)
	{
		String[] val;
		for (String key : map.keySet()) 
		{
			if (kkk.equals(key))
				{
					val = map.get(key);
					String k = getKey(ask, val, 0.7);
					if (k!=null)
					{
						return k;
					}
				}
		}
		return null;
	}
	
	private static String findInMap(Map<String, String[]> map, String ask)
	{
		String[] val;
		Levenshtein l = new Levenshtein();
		for (String key : map.keySet()) 
		{
			val = map.get(key);
			for (int i=0;i<val.length;i++)
				if (l.similarity(ask, val[i])>0.7) return key;
		}
		return null;
	}
	public static void main(String[] args) throws FileNotFoundException {
		// get ask on words
		String ask = "Кто знает Java и не смотрел BlaBla";
		String[] askWords = ask.split(" ");
		String questWord = askWords[0];
		List<String> condition = new ArrayList<String>();
		Levenshtein l = new Levenshtein();
		String lab = null;
		String lang = null;
		String film = null;
		String sport = null;
		String os = null;
		int num = -1;
		for (int i=0;i<askWords.length;i++)
			if (askWords[i].equals("не")) num = i;
		

		// get content of file with name of Man
		String contentMans = new Scanner(new File("src/testJava/mans.txt")).useDelimiter("\\Z").next();
		String[] dictionaryMans = contentMans.split("\n");

		// get content of file with name of Labs
		String contentLabs = (new Scanner(new File("src/testJava/labs.txt")).useDelimiter("\\Z").next()).replaceAll("(\r\n)", " ");
		String[] dictionaryLabs = contentLabs.split(";");
		Map<String, String[]> mapLabs = createMap(dictionaryLabs);
		lab = getKeySim(mapLabs,askWords);
		
		// get content of file with langs
		String contentLangs = (new Scanner(new File("src/testJava/lang.txt")).useDelimiter("\\Z").next()).replaceAll("(\r\n)", " ");
		String[] dictionaryLangs = contentLangs.split(";");
		Map<String, String[]> mapLang = createMap(dictionaryLangs);
		lang = getKeySim(mapLang, askWords);
		
		// get content of file with aliases 
		String contentAlias = (new Scanner(new File("src/testJava/alias.txt")).useDelimiter("\\Z").next()).replaceAll("(\r\n)", " ");
		String[] dictionaryAlias = contentAlias.split(";");
		Map<String, String[]> mapAlias = createMap(dictionaryAlias);

		
		//get content of file with name of interests
		String contentInter = (new Scanner(new File("src/testJava/inter.txt")).useDelimiter("\\Z").next()).replaceAll("(\r\n)", " ");
		String[] dictionaryInter = contentInter.split(";");
		Map<String, String[]> mapInter = createMap(dictionaryInter);
		film = getKeySim(mapInter, askWords, "Film");
		
		for (int i = 1; i < askWords.length; i++) {
			String alias = checkValue(askWords[i], mapAlias);
			if (i == num) condition.add("not");
			if (alias != null) 
			{
				if (alias.equals("ItisLabs")&& lab != null) 
				{
					condition.add(alias + " " + lab);
					i += lab.split(" ").length;
					lab = null;
				}
				else if (alias.equals("lang") && lang!=null)
					{
						condition.add("programmingLang " + lang);
					}
				else if (alias.equals("Film") && lang!=null)
				{
					condition.add("interests Film " + film);
					if (askWords.equals(film)) i++;
				}
					else if (alias.equals("knows"))
					{
						if (lang!=null)
						{
							condition.add(alias +" " + lang);
							i++;
						}else if ((i+2)<askWords.length-1 && getKey(askWords[i+2],dictionaryMans,0.7)!=null)
						{
							condition.add(alias +" " + askWords[i+2]);
							i+=2;
						}
					}
			} else if (alias != null && !alias.equals("ItisLabs")) {
				condition.add(alias);
			} else {
				if (getKey(askWords[i], dictionaryMans, 0.7) != null)
					condition.add("Person " + getKey(askWords[i], dictionaryMans, 0.7));
				if (getKey(askWords[i], dictionaryLabs, 0.75) != null)
					condition.add("ItisLabs " + findInMap(mapLabs,askWords[i]));
				if (getKey(askWords[i], dictionaryLangs, 0.75) != null)
					condition.add("ProgrammingLang " + findInMap(mapLang,askWords[i]));
				if (askWords[i].equals("и")) condition.add("and");
			}
		}
		System.out.print(questWord + " ");
		System.out.println(condition.toString());
		System.out.println(film);

	}

	// System.out.println(l.distanceAbsolute("My string", "My $tring"));
}
