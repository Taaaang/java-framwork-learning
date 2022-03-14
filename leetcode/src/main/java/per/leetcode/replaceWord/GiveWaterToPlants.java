package per.leetcode.replaceWord;

/**
 * 给植物浇水
 * https://leetcode-cn.com/problems/watering-plants/
 *
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/3/10 - 9:27 上午
 **/
public class GiveWaterToPlants {
    public int wateringPlants(int[] plants, int capacity) {
        //1.一次完，数组长度
        //2.一次完不了，（循环次数-1）（3*每次最后位置）+数组长度
        if(plants.length==0){
            return 0;
        }
        int stepCount=0;
        int nowCapacity=capacity;
        for (int i = 0; i < plants.length; i++) {
            if(nowCapacity-plants[i]<0){
                //计算步数
                stepCount+=(i)*2;
                //重置
                nowCapacity=capacity;
                i=i-1;
            }else {
                //消耗水
                nowCapacity-=plants[i];
            }
            if(plants.length==(i+1)){
                stepCount+=plants.length;
            }
        }
        return stepCount;
    }
    public static void main(String[] args) {
        int[] plants=new int[]{2,2,3,3};
        int capacity=5;
        /*int[] plants=new int[]{1,1,1,4,2,3};
        int capacity=4;*/
        GiveWaterToPlants method=new GiveWaterToPlants();
        int i = method.wateringPlants(plants, capacity);
        System.out.println("需要步数:"+i);
    }
}
