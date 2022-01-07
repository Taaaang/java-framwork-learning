package per.leetcode.replaceWord;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2021/12/22 - 3:54 下午
 **/
class Solution {
    public int[][] multiSearch(String big, String[] smalls) {
        //构建前缀树
        WordManagerSupport support=new WordManagerSupport();
        ArrayList<ArrayList<Integer>> positionsIndex=new ArrayList<>(smalls.length);
        for (int i = 0; i < smalls.length; i++) {
            support.insert(smalls[i],i);
            positionsIndex.add(new ArrayList<>());
        }
        //查询
        support.search(big,positionsIndex);
        //转换结果
        int[][] res=new int[positionsIndex.size()][];
        for (int i = 0; i < positionsIndex.size(); i++) {
            res[i]=positionsIndex.get(i).stream().mapToInt(Integer::intValue).toArray();
        }
        return res;
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

    public static class BasicWordTree<T extends BasicWordTree> {
        private char sign;
        private boolean isWord;
        private List<T> nextList;

        public BasicWordTree(char sign){
            this.sign=sign;
            this.nextList=new ArrayList<>();
        }

        public void changeWord(boolean isWord){
            this.isWord=isWord;
        }

        public T getNext(char sign){
            if(nextList==null||nextList.isEmpty()){
                return null;
            }
            for (T wordTree : nextList) {
                if(wordTree.isEquals(sign)){
                    return wordTree;
                }
            }
            return null;
        }

        public void addNext(T wordTree){
            nextList.add(wordTree);
        }

        public boolean isEquals(char compareSign){
            return sign==compareSign;
        }

        public boolean isWord(){
            return isWord;
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

        public void search(String sentence,ArrayList<ArrayList<Integer>> positionsIndex){
            for (int i = 0; i < sentence.length(); i++) {
                String subStr=sentence.substring(i);
                searchPosition(subStr,i,positionsIndex);
            }
        }

        private void searchPosition(String sentence, int index,ArrayList<ArrayList<Integer>> positionsIndex){
            PositionWordTree current=rootTree;
            for (int i = 0; i < sentence.toCharArray().length; i++) {
                PositionWordTree next = current.getNext(sentence.charAt(i));
                if(next==null){
                    //无
                    break;
                }else {
                    if(next.isWord()){
                        positionsIndex.get(next.position).add(index);
                    }
                    current=next;
                }
            }
        }

    }


}