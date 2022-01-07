package per.leetcode.replaceWord;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2021/12/22 - 2:35 下午
 **/
public class MultipleSearch {

    public static void main(String[] args) {
        String big="mississippi";
        String[] smalls=new String[]{"is","ppi","hi","sis","i","ssippi"};
        int[][] search = search(big, smalls);
        Gson gson=new Gson();
        System.out.println(gson.toJson(search));
    }

    private static int[][] search(String big, String[] smalls){
        //构建前缀树
        WordManagerSupport support=new WordManagerSupport();
        int[][] positionsIndex=new int[smalls.length][];
        int[] sequence=new int[smalls.length];
        for (int i = 0; i < smalls.length; i++) {
            support.insert(smalls[i],i);
        }

        //查询
        support.search(big,positionsIndex,sequence);
        for (int i = 0; i < positionsIndex.length; i++) {
            positionsIndex[i]=Arrays.copyOf(positionsIndex[i],sequence[i]);
        }
        //转换结果
        return positionsIndex;
    }

    private static class PositionWordTree extends BasicWordTree<PositionWordTree>{

        private int position;

        public PositionWordTree(char sign,int position) {
            super(sign);
            this.position=position;
        }

        public void setPosition(int position){
            this.position=position;
        }
    }

    private static class WordManagerSupport{
        private PositionWordTree rootTree;

        public void insert(String word,int position){
            if(rootTree==null){
                rootTree=new PositionWordTree(' ',-1);
            }
            PositionWordTree current=rootTree;
            for (int i = 0; i < word.toCharArray().length; i++) {
                char c=word.charAt(i);
                PositionWordTree next = current.getNext(c);
                if(next==null){
                    PositionWordTree newWordTree = new PositionWordTree(c, i);
                    current.addNext(newWordTree);
                    current=newWordTree;
                }else {
                    current=next;
                }
            }
            current.changeWord(true);
            current.setPosition(position);
        }

        public void search(String sentence,int[][] positionsIndex,int[] sequence){
            for (int i = 0; i < sentence.length(); i++) {
                searchPosition(sentence.toCharArray(),i,positionsIndex,sequence);
            }
        }

        private void searchPosition(char[] sentenceChars, int index,int[][] positionsIndex,int[] sequence){
            PositionWordTree current=rootTree;
            for (int i = index; i < sentenceChars.length; i++) {
                PositionWordTree next = current.getNext(sentenceChars[i]);
                if(next==null){
                    //无
                    break;
                }else {
                    if(next.isWord()){
                        positionsIndex[next.position][sequence[next.position]++]=index;
                    }
                    current=next;
                }
            }
        }

    }

}
