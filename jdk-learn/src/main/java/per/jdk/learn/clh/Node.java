package per.jdk.learn.clh;

/**
 * @Author：TangWenBiao
 * @Email：tangwenbiao@souche.com
 * @CreateTime：2022/3/31 - 1:36 下午
 **/
public class Node {

    /**
     * 节点是否在运行
     * true 运行中，false 运行完毕
     */
    private volatile boolean status;

    public Node() {
        this.status=true;
    }

    public Node(boolean status) {
        this.status = status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }
}
