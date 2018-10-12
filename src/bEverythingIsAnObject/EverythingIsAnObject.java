package bEverythingIsAnObject;

import java.util.Date;
import static java.lang.System.out;

public class EverythingIsAnObject {
    //2.1 用引用操纵对象
    //2.2 必须由你创建所有对象
        //存储到什么地方：寄存器、堆栈、堆、常量存储、非RAM存储
        //特例：基本类型
        //Java中的数组
    //2.3 永远不需要销毁对象
        //作用域
        //对象的作用域
    //2.4 创建新的数据类型：类
    //2.5 方法、参数和返回值
    //2.6 构建一个Java程序
    //2.7 你的第一个Java程序
    public static class HelloDate {
        public static void main(String[] args) {
            out.println("Hello, it's: ");
            out.println(new Date());
        }
    }
    public static void main(String[] args) {
        HelloDate.main(args);
    }
    //2.8 注释和嵌入式文档
        //注释文档
        //语法
        //嵌入式HTML
        //一些标签示例
            //@see  @link   @docRoot    @inheritDoc @version    @author @since  @param  @return @throws @deprecated
        //文档示例
    //2.9 编码风格
    //2.10 总结
}
