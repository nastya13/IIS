/**
 * 
 */
package testJava;
import info.debatty.java.stringsimilarity.JaroWinkler;
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

	private static String getKey(String word, String[] dict, double accuracy)
	{
		Levenshtein l = new Levenshtein();
		//System.out.println(l.similarity("Мобильные Разработки", "мобильных разработок"));
		if (accuracy>0)
		for (int i=0;i<dict.length;i++)
		{
			String s = dict[i].substring(0, dict[i].length()-1);
			if (l.similarity(s, word) >= accuracy)
				return s;
		}
		return null;
	}
	
	private static String checkAlias(String word, Map<String,String[]> m)
	{
		String[] val;
		for (String key : m.keySet()) 
		{
			//val = new String[m.get(key).length];
			val = m.get(key);
			for (int i=1;i<val.length;i++)
				if (word.contains(val[i]))
						return key;
		}
		return null;
	}
	
	public static void main(String[] args) throws FileNotFoundException 
	{
		//get ask on words
		String ask = "Кто знает Саню из лабы Мобильных Разработок";
		String[] askWords = ask.split(" ");
		String questWord = askWords[0];
		List<String> condition = new ArrayList<String>();
		String lab = null;
		String inter;
				
		
		//get content of file with name of Man
		String contentMans = new Scanner(new File("src/testJava/mans.txt")).useDelimiter("\\Z").next();
		String[] dictionaryMans = contentMans.split("\n");
		
		//get content of file with name of Man
		String contentLabs = new Scanner(new File("src/testJava/labs.txt")).useDelimiter("\\Z").next();
		String[] dictionaryLabs = contentLabs.split("\n");
		Levenshtein l = new Levenshtein();
		for (int i=0;i<dictionaryLabs.length;i++)
		{
			for (int j=1;j<askWords.length-1;j++)
			{
				String str = askWords[j]+" " +askWords[j+1];//сделать для одного слова тоже
				String str2 = askWords[j];
				if (l.similarity(str, dictionaryLabs[i])>0.75 || l.similarity(str2, dictionaryLabs[i])>0.75)
					lab = dictionaryLabs[i];
			}
		}
		//get content of file with name of Man .useDelimiter("\\Z").next()
		String contentAlias = (new Scanner(new File("src/testJava/alias.txt")).useDelimiter("\\Z").next()).replaceAll("(\r\n)", " ");
		
		
		String[] dictionaryAlias = contentAlias.split(";");
		Map<String,String[]> map = new HashMap<String, String[]>();
		for (int i=0;i<dictionaryAlias.length;i+=2)
		{
			String[] arr = dictionaryAlias[i+1].split(" ");
			for (int j=1;j<arr.length;j++)
				arr[j] = arr[j].substring(0, arr[j].length()-1);
				
			map.put(dictionaryAlias[i], arr);
		}

//	    for (Map.Entry<String, String[]> entry : map.entrySet()) {
//	        String key = entry.getKey().toString();
//	        String[] value = entry.getValue();
//	        for (int i=0;i<value.length;i++)
//	        	System.out.print("key= " + key + ", value= " + value[i] + "; ");
//	    }
				
		System.out.print(questWord + " ");
		for (int i=1; i<askWords.length; i++)
		{
			String alias = checkAlias(askWords[i], map);
			if (alias != null && lab !=null && (l.similarity(askWords[i+1]+askWords[i+2], lab)>0.7||l.similarity(askWords[i+1], lab)>0.7))
			{
				if (alias.equals("ItisLabs"))
				{
					condition.add(alias + " " +  lab);
					i+=lab.length();
					lab = null;
				}
			}
			else {
				if (getKey(askWords[i], dictionaryMans, 0.7)!=null)
					condition.add("Person " + getKey(askWords[i], dictionaryMans, 0.7));
				if (getKey(askWords[i], dictionaryLabs, 0.75)!=null)
					condition.add("ItisLabs "+getKey(askWords[i], dictionaryLabs, 0.75));
			}
		}
		System.out.println(condition.toString());
		//Levenshtein l = new Levenshtein();
		double k = l.similarity("Саня", "Саню");
		System.out.println(k);
		System.out.println("лабы".contains("лаб"));
        
	}

	
	//System.out.println(l.distanceAbsolute("My string", "My $tring"));
}
