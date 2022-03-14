package per.leetcode.replaceWord;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 无重复字符串，最长子串
 * https://leetcode-cn.com/problems/longest-substring-without-repeating-characters/
 *
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/3/14 - 9:45 上午
 **/
public class LengthOfLongestStrNoReplace {

    public static void main(String[] args) {
        String[] array=new String[]{"abcabcbb","bbbbb","pwwkew"};
        for (String s : array) {
            System.out.println(search(s));
        }

        System.out.println(search("tmmzuxt"));
    }

    public static int search(String s) {
        if (s == null) {
            return 0;
        }
        if (s.length() == 0 || s.length() == 1) {
            return s.length();
        }
        //后指针
        int backPoint = -1;
        //最长长度
        int maxLength = 0;
        //用hash存储子串用于比较是否重复o(1)
        int[] indexHash = new int[128];
        //设置为负一，解决有无重复值导致的计算总长度的差别
        /**
         * 1.无重复值
         * 总长度=index+1;
         *
         * 2.有重复值
         * 总长度=index-backIndex;
         *
         */
        Arrays.fill(indexHash, -1);
        for (int i = 0; i < s.length(); i++) {
            char cur=s.charAt(i);
            int index = indexHash[cur];
            //1.前指针随i自增
            //2.后指针,情况为要么存在，要么
            backPoint = Math.max(index, backPoint);
            //3.总长度,存在两种情况，第一有重复值，第二无重复值
            maxLength = Math.max(maxLength, i - backPoint);
            //填入hash表
            indexHash[cur] = i;

        }
        return maxLength;
    }


}
