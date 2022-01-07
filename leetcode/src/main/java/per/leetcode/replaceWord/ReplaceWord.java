package per.leetcode.replaceWord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link https://leetcode-cn.com/problems/UhWRSj/}
 *
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2021/12/22 - 10:21 上午
 **/
public class ReplaceWord {

    public static void main(String[] args) {
        List<String> dictionary=new ArrayList<>();
        dictionary.add("cat");
        dictionary.add("bat");
        dictionary.add("rat");
        String sentence="the cattle was rattled by the battery";
        System.out.println(replaceWord2(dictionary,sentence));
    }

    public static String replaceWord2(List<String> dictionary,String sentence){
        WordManagerSupport support=new WordManagerSupport();
        dictionary.forEach(support::insert);
        String[] words = sentence.split(" ");
        StringBuilder stringBuilder=new StringBuilder();
        for (String word : words) {
            String search = support.search(word);
            if(search==null){
                stringBuilder.append(word);
                stringBuilder.append(" ");
            }else {
                stringBuilder.append(search);
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.substring(0,stringBuilder.length()-1);
    }

    public static String replaceWord(List<String> dictionary,String sentence){
        String[] words = sentence.split(" ");
        StringBuilder replaceSentence=new StringBuilder();
        for (String word : words) {
            String replaceWord = checkRootWord3(dictionary, word);
            replaceSentence.append(replaceWord==null?word:replaceWord);
            replaceSentence.append(" ");
        }
        return replaceSentence.substring(0,replaceSentence.length()-1);
    }


    private static String checkRootWord(List<String> dictionary,String word){
        int minLength=Integer.MAX_VALUE;
        int minRootWordIndex=-1;
        for (int i = 0; i < dictionary.size(); i++) {
            String rootWord = dictionary.get(i);
            //排除不合格的词根
            if(rootWord.length()>word.length()){
                continue;
            }
            for (int j = 0; j < word.length(); j++) {
                //匹配
                if(j>(rootWord.length()-1)){
                    if(minLength>rootWord.length()){
                        minRootWordIndex=i;
                        minLength=rootWord.length();
                    }
                    break;
                }
                //不同,跳过该词
                if(rootWord.charAt(j)!=word.charAt(j)){
                    break;
                }

            }
        }
        return minRootWordIndex==-1?null:dictionary.get(minRootWordIndex);
    }


    /**
     * 相比于 checkRootWord 更加耗时
     * 为何使用String的indexOf()比charAt更耗时？
     * 因为indexOf需要鉴别其他位置是否命中，所以多了一层循环
     * @param dictionary
     * @param word
     * @return
     */
    private static String checkRootWord2(List<String> dictionary,String word){
        int minLength=Integer.MAX_VALUE;
        int minRootWordIndex=-1;
        for (int i = 0; i < dictionary.size(); i++) {
            String rootWord = dictionary.get(i);
            //排除不合格的词根
            if(rootWord.length()>word.length()){
                continue;
            }
            if(word.indexOf(rootWord)==0){
                //匹配
                if(minLength>rootWord.length()){
                    minRootWordIndex=i;
                    minLength=rootWord.length();
                }
            }else {
                //不匹配
                continue;
            }

        }
        return minRootWordIndex==-1?null:dictionary.get(minRootWordIndex);
    }

    private static String checkRootWord3(List<String> dictionary,String word){
        int minRootWordLength=0;
        int minRootWordIndex=-1;
        for (int i = 0; i < dictionary.size(); i++) {
            String rootWord = dictionary.get(i);
            //排除不合格的词根
            if(rootWord.length()>word.length()){
                continue;
            }
            //当以进行了一次比较，产生的最小长度大于了当前词根的长度，则跳过
            if(minRootWordIndex!=-1&&minRootWordLength<rootWord.length()){
                continue;
            }
            if(rootWord.equals(word.substring(0,rootWord.length()))){
                minRootWordIndex=i;
                minRootWordLength=rootWord.length();
            }
        }
        return minRootWordIndex==-1?null:dictionary.get(minRootWordIndex);
    }

    private static class WordTree extends BasicWordTree<WordTree>{
        public WordTree(char sign) {
            super(sign);
        }
    }

    private static class WordManagerSupport{
        private WordTree rootTrees;

        public void insert(String word){
            if(rootTrees==null){
                rootTrees=new WordTree(' ');
            }
            WordTree currentWord=rootTrees;
            for (char c : word.toCharArray()) {
                if(currentWord.getNext(c)==null){
                    WordTree wordTree=new WordTree(c);
                    currentWord.addNext(wordTree);
                    currentWord=wordTree;
                }else {
                    currentWord=currentWord.getNext(c);
                }
            }
            currentWord.changeWord(true);
        }

        public String search(String word){

            StringBuilder stringBuilder=new StringBuilder();
            WordTree currentWord=rootTrees;
            for (char c : word.toCharArray()) {
                WordTree next = currentWord.getNext(c);
                if(next==null){
                    return null;
                }
                stringBuilder.append(c);
                if(next.isWord()){
                    return stringBuilder.toString();
                }
                currentWord=next;
            }
            return null;
        }
    }



}


