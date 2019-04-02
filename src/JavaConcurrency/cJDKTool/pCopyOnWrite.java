package JavaConcurrency.cJDKTool;

import JavaConcurrency.zUtils.CopyOnWriteMap;

import java.util.Map;

//16 CopyOnWrite
public class pCopyOnWrite {
    //16.1 什么是CopyOnWrite容器
        //CopyOnWrite是计算机设计领域中的一种优化策略，也是一种在并发场景下常用的设计思想——写入时复制思想。
        //当有多个调用者同时去请求一个资源数据的时候，有一个调用者出于某些原因需要对当前的数据源进行修改，这个时候系统将会复制一个当前数据源的副本给调用者修改。
        //CopyOnWrite容器即写时复制的容器，当我们往一个容器中添加元素的时候，不直接往容器中添加，而是将当前容器进行copy，复制出来一个新的容器，然后向新容器中添加我们需要的元素，最后将原容器的引用指向新容器。
    //16.2 CopyOnWriteArrayList
        //CopyOnWriteArrayList经常被用于“读多写少”的并发场景，是因为CopyOnWriteArrayList无需任何同步措施，大大增强了读的性能。
    //16.3 CopyOnWrite的业务中实现
        static class BlackListServiceImpl {
            private static CopyOnWriteMap<String, Boolean> blackListMap = new CopyOnWriteMap<>(1000);

            public static boolean isBlackList(String id) {
                return blackListMap.get(id) != null;
            }

            public static void addBlackList(String id) {
                blackListMap.put(id, Boolean.TRUE);
            }

            public static void addBlackList(Map<String, Boolean> ids) {
                blackListMap.putAll(ids);
            }
        }
        //CopyOnWrite容器有数据一致性的问题，它只能保证最终数据一致性。
        //如果我们希望写入的数据马上能准确地读取，请不要使用CopyOnWrite容器。
}
