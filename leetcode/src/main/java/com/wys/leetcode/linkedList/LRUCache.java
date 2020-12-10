package com.wys.leetcode.linkedList;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyasheng
 * @date 2020/12/10
 * @Describe: LRU缓存
 */
class LRUCache {
    class DLinkedNode{
        int key;
        int value;
        DLinkedNode prev;
        DLinkedNode next;
        public DLinkedNode(){};
        public DLinkedNode(int _key,int _value){
            key = _key;
            value = _value;
        }
    }

    private Map<Integer,DLinkedNode> cache = new HashMap<>();
   //当前size
    private int size;
    //容量
    private int capacity;
    private DLinkedNode head;
    private DLinkedNode tail;

    public LRUCache(int capacity){
        this.size = 0;
        this.capacity = capacity;
        //使用伪头部节点
        head = new DLinkedNode();
        tail = new DLinkedNode();
    }

    /**
     * 添加或更新（当key已经存在时）元素
     * @param key
     * @param value
     */
    public void put(int key,int value){
        DLinkedNode node = cache.get(key);
        if (node == null){
            //如果key不存在，创建一个新的节点
            DLinkedNode newNode = new DLinkedNode(key,value);
            //添加进哈希表
            cache.put(key,newNode);
            // 添加到双链表的头部
            addToHead(newNode);
            ++size;
            if (size>capacity){
                //如果超出容量，删除双向链表的尾部节点
                DLinkedNode tail = removeTail();
                // 删除哈希表中对应的项
                cache.remove(tail.key);
                --size;
            }
        }else{
            //如果key存在，先通过哈希表定位，在修改其值并移动到头部
            node.value = value;
            moveToHead(node);
        }
    }

    /**
     * 获取元素
     * @param key
     * @return
     */
    public int get(int key){
        DLinkedNode node = cache.get(key);
        if (node == null) {
            return -1;
        }
        //如果key存在，先通过哈希表定位，再移动到头部
        moveToHead(node);
        return node.value;
    }

    /**
     * 在头部添加节点
     * @param node
     */
    private void addToHead(DLinkedNode node){
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    /**
     * 删除节点
     * @param node
     */
    private void removeNode(DLinkedNode node){
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    /**
     * 将指定节点移动到头部
     * @param node
     */
    private void moveToHead(DLinkedNode node){
        removeNode(node);
        addToHead(node);
    }

    /**
     * 删除尾部节点
     * @return 删除的尾部节点，返回给哈希表删除对应的元素
     */
    private DLinkedNode removeTail(){
        DLinkedNode res = tail.prev;
        removeNode(res);
        return res;
    }
}
