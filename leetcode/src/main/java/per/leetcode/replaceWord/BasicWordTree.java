package per.leetcode.replaceWord;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2021/12/22 - 2:36 下午
 **/
public class BasicWordTree<T extends BasicWordTree> {
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
