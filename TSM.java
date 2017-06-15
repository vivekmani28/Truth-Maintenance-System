import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public class TSM {
	public static int index =0;
	public static Map<String,LinkedList<KBItem>> knowledgeBase = new LinkedHashMap<String,LinkedList<KBItem>>();
	public static Map<String, LinkedList<Implication>> implicationMap = new HashMap<String, LinkedList<Implication>>();
	public static void main(String[] args) {
		
		 if (args.length != 1){
			System.out.println("Usage:: TSM File_Name");
			return;
		}
		
		String fileName = args[0].trim();
		//String fileName = "src//TSMInput.txt";
		populateKnowledgeBase(fileName);
		printKnowledgeBase();

	}

	private static void printKnowledgeBase() {
		LinkedList<KBItem> tmp;
		int tmpCnt;
		Iterator<String> iterator = knowledgeBase.keySet().iterator();
		String key;
		while (iterator.hasNext()) {
		   key = iterator.next();
		   tmp = knowledgeBase.get(key);
		   if(key.indexOf(":") == -1)
			   System.out.println(tmp.get(0).getValue());
		   else if (tmp.size() > 0){
			   System.out.print(key);
			   tmpCnt =0;
			   while(tmpCnt < tmp.size()){
				   System.out.print(tmp.get(tmpCnt).getValue()+",");
				   tmpCnt++;
			   }
			   System.out.println("\b ");
		   }
		}
	}

	private static void populateKnowledgeBase(String fileName) {
		try{
			// Read and populate KB with input file
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			String tmpArray[];
			String tmpStr;
			LinkedList<KBItem> tmpList;
			
			while ((line = reader.readLine()) != null && line.length() > 0) {
                tmpArray = line.trim().split(":");
                if(tmpArray[0].equals("Tell")){
                	index++;
                	if(knowledgeBase.get(tmpArray[1]) == null){
	                	tmpList = getKBList(tmpArray[1]);
	                	tmpList.add(new KBItem(tmpArray[1], index));
	                	knowledgeBase.put(tmpArray[1],tmpList);
	                	processLiteralAddition(tmpArray[1],index);
                	}
                }
                else if(tmpArray[0].equals("Retract")){
                	tmpStr = getKey(tmpArray[1]);
                	if(tmpStr != null){
	                	processLiteralRemoval(tmpArray[1],knowledgeBase.get(tmpArray[1]).get(0).getParentIndex());
	                	removeFromKBMap(tmpArray[1]);
                	}
                	if(tmpArray[1].indexOf("->") != -1){
                		partialRemoveFromKBMap(tmpArray[1]);
                	}
                }
                else{
                	System.err.println("Invalid input in file");
                	reader.close();
                	System.exit(-1);
                }
            }
			reader.close();
		}
		catch(FileNotFoundException exp){
			System.err.println("Input file not present");
			exp.printStackTrace();
		} catch (IOException exp) {
			System.err.println("Unable to read data from Input File");
			exp.printStackTrace();
		}
	}
	
	private static void partialRemoveFromKBMap(String value){
	
		
		LinkedList<KBItem> tmp;
		int tmpCnt;
		Iterator<String> iterator = knowledgeBase.keySet().iterator();
		String key;
		while (iterator.hasNext()) {
		   key = iterator.next();
		   tmp = knowledgeBase.get(key);
		   if(key.indexOf(":") != -1){
			   tmpCnt =0;
			   while(tmpCnt < tmp.size()){
				   if(tmp.get(tmpCnt).getValue().contains(value))
					   tmp.remove(tmpCnt);  
				   else
					   tmpCnt++;
			   }
		   }
		}
	}
	
	private static void removeFromKBMap(String value){
		Iterator<Entry<String, LinkedList<KBItem>>> iter = knowledgeBase.entrySet().iterator();
		while (iter.hasNext()) {
		    Entry<String, LinkedList<KBItem>> entry = iter.next();
		    if(entry.getValue().size() ==0  ||value.equalsIgnoreCase(entry.getValue().get(0).getValue())){
		        iter.remove();
		    }
		}
	}
	private static String getKey(String value){
		 for (Entry<String, LinkedList<KBItem>> entry : knowledgeBase.entrySet()) {
	            if (entry.getValue().size() > 0 && entry.getValue().get(0).getValue().equals(value)) {
	                return entry.getKey();
	            }
	        }
		 return null;
	}
	
	private static LinkedList<KBItem> getKBList(String name){
		LinkedList<KBItem> tmpList;
		if((tmpList =  knowledgeBase.get(name)) == null)
			tmpList = new LinkedList<KBItem>();
		return tmpList;
	}
	
	private static LinkedList<Implication> getImplicationList(String name){
		LinkedList<Implication> tmpList;
		if((tmpList =  implicationMap.get(name)) == null)
			tmpList = new LinkedList<Implication>();
		return tmpList;
	}

	private static void processLiteralRemoval(String string, int tmpIndex) {

		LinkedList<KBItem> tmp;
		int tmpCnt;
		Iterator<String> iterator = knowledgeBase.keySet().iterator();
		String key;
		KBItem tmpItem;
		while (iterator.hasNext()) {
		   key = iterator.next();
		   tmp = knowledgeBase.get(key);
		   if(key.indexOf(":") != -1) {
			   tmpCnt =0;
			   while(tmpCnt < tmp.size()){
				   tmpItem = tmp.get(tmpCnt);
				   if(tmpItem.getParentIndex() == tmpIndex || tmpItem.getOtherParentIndex() == tmpIndex)
					   tmp.remove(tmpCnt);
				   else
					   tmpCnt++;
			   }
		   }
		}
	}

	private static void processLiteralAddition(String literal, int index) {
		Implication tmpImplication = null ;
		LinkedList<Implication> tmpImplList;
		String tmpStr;
		int tmpCnt;
		if(literal.length() <= 2){
			// If the entire literal contains no implication: Tell A
			//Call Retract for the negated input before proceeding
			if(literal.indexOf("-") == -1)
				tmpStr="-"+literal;
			else
				tmpStr= literal.replace("-", "");
        	if(getKey(tmpStr) != null){
            	processLiteralRemoval(tmpStr,knowledgeBase.get(tmpStr).get(0).getParentIndex());
            	removeFromKBMap(tmpStr);
        	}
			tmpImplList = implicationMap.get(literal);

			if(tmpImplList != null){
				tmpCnt = 0;
				while(tmpCnt < tmpImplList.size()){
					tmpImplication = tmpImplList.get(tmpCnt);
					if(tmpImplication.isAnd() == false){
						updateKBWithSimpleRelation(tmpImplication);
					}
					else {
						updateKBWithAndRelation(tmpImplication);
					}
					tmpCnt++;
				}
			}
		} else{
			// If the entire literal contains implication: Tell A->B
			String[] expr = literal.split("->");
			tmpStr = expr[0].trim();
			if(tmpStr.length() <= 2){
				//Only one variable on the left
				tmpImplication = new Implication(tmpStr, expr[1].trim(), false, index, literal);
				insertImplication(tmpImplication);
				updateKBWithSimpleRelation(tmpImplication);
				
			}
			else{
				if (expr[0].indexOf("*") != -1){
					expr[0] = expr[0].replaceAll("\\+", "\\|").replaceAll("\\*", "@");
					if(expr[0].indexOf("|") == -1){
						//Only AND
						String tmpArray2[] = expr[0].split("@");
						int tmpCnt2=0;
						while(tmpCnt2 < 2){
							tmpImplication = new Implication(tmpArray2[tmpCnt2], expr[1].trim(), true, index, literal,tmpArray2[(tmpCnt2+1)%2]);
							insertImplication(tmpImplication);
							tmpCnt2++;
						}
						updateKBWithAndRelation(tmpImplication);
					}
					else{
						//Both OR and AND
						String tmpArray[] = expr[0].split("\\|");
						tmpCnt=0;
						while(tmpCnt < tmpArray.length){
							if(tmpArray[tmpCnt].indexOf("@") == -1){
								tmpImplication = new Implication(tmpArray[tmpCnt], expr[1].trim(), false, index, literal);
								insertImplication(tmpImplication);
								updateKBWithSimpleRelation(tmpImplication);
							}
							else{
								//Handle the AND
								String tmpArray2[] = tmpArray[tmpCnt].split("@");
								int tmpCnt2=0;
								while(tmpCnt2 < 2){
									tmpImplication = new Implication(tmpArray2[tmpCnt2], expr[1].trim(), true, index, literal,tmpArray2[(tmpCnt2+1)%2]);
									insertImplication(tmpImplication);
									tmpCnt2++;
								}
								updateKBWithAndRelation(tmpImplication);
							}
							tmpCnt++;
						}
					}
				}
				else{
					// This means there is a implication with no AND 
					String[] parents = expr[0].split("\\+");
					tmpCnt = 0;
					while(tmpCnt < parents.length){
						tmpImplication = new Implication(parents[tmpCnt], expr[1].trim(), false, index, literal);
						insertImplication(tmpImplication);
						updateKBWithSimpleRelation(tmpImplication);
						tmpCnt++;
					}
				}
			}
		}
	}
	private static void insertImplication(Implication newImpl){
		LinkedList<Implication> tmpImplList;
		tmpImplList = getImplicationList(newImpl.getParent());
		tmpImplList.add(newImpl);
		implicationMap.put(newImpl.getParent(), tmpImplList);
	}
	
	private static void updateKBWithAndRelation(Implication impl){
		StringBuilder tmpBuilder;
		String tmpKey;
		LinkedList<KBItem> tmpItemList,tmpItemList1,tmpItemList2;

		if(((tmpItemList1 = knowledgeBase.get(impl.getotherParent())) != null || (tmpItemList1 = knowledgeBase.get(impl.getotherParent() + ":")) != null) &&  
				((tmpItemList2 = knowledgeBase.get(impl.getParent())) != null || (tmpItemList2 = knowledgeBase.get(impl.getParent() + ":")) != null)) {
			tmpBuilder = new StringBuilder();
			tmpKey = impl.getChild()+":";
			tmpItemList = getKBList(tmpKey);
			tmpBuilder.append("{").append(impl.getotherParent()).append(",").append(impl.getParent()).append(",").append(impl.getRawString()).append("}");
			tmpItemList.add(new KBItem(tmpBuilder.toString(), tmpItemList1.get(0).getParentIndex(),tmpItemList2.get(0).getParentIndex()));
			knowledgeBase.put(tmpKey, tmpItemList);
		}
		
		//
		Implication tmpImplication = null ;
		LinkedList<Implication> tmpImplList;
		int tmpCnt;
		tmpImplList = implicationMap.get(impl.getChild());

		if(tmpImplList != null){
			tmpCnt = 0;
			while(tmpCnt < tmpImplList.size()){
				tmpImplication = tmpImplList.get(tmpCnt);
				if(tmpImplication.isAnd() == false){
					updateKBWithSimpleRelation(tmpImplication);
				}
				else {
					updateKBWithAndRelation(tmpImplication);
				}
				tmpCnt++;
			}
		}
	}
	
	private static void updateKBWithSimpleRelation(Implication impl){
		StringBuilder tmpBuilder;
		String tmpKey;
		LinkedList<KBItem> tmpItemList,tmpItemList1;
		Implication tmpImplication = null ;
		LinkedList<Implication> tmpImplList;
		int tmpCnt;

		if((tmpItemList1= knowledgeBase.get(impl.getParent())) != null || (tmpItemList1 =knowledgeBase.get(impl.getParent()+":")) != null){
			tmpBuilder = new StringBuilder();
			tmpKey = impl.getChild()+":";
			tmpItemList = getKBList(tmpKey);
			tmpBuilder.append("{").append(impl.getParent()).append(",").append(impl.getRawString()).append("}");
			tmpItemList.add(new KBItem(tmpBuilder.toString(), tmpItemList1.get(0).getParentIndex()));
			knowledgeBase.put(tmpKey, tmpItemList);
		}
		//
		tmpImplList = implicationMap.get(impl.getChild());
		if(tmpImplList != null){

			tmpCnt = 0;
			while(tmpCnt < tmpImplList.size()){
				tmpImplication = tmpImplList.get(tmpCnt);
				if(tmpImplication.isAnd() == false){
					updateKBWithSimpleRelation(tmpImplication);
				}
				else {
					updateKBWithAndRelation(tmpImplication);
				}
				tmpCnt++;
			}
		}
		
	}
}
