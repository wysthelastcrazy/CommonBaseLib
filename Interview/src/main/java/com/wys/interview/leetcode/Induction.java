package com.wys.interview.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yas on 2019/2/14
 * Describe:
 */
public class Induction {

    /**
     * 冒泡排序
     * @param arr
     * @return
     */
   public int[] bubbleSort(int[] arr){
       //外层循环控制排序趟数
       for (int i=0;i<arr.length-1;i++){
           //内层循环控制每趟排序多少次
           for (int j=0;j<arr.length-1-i;j++){
               if (arr[j]>arr[j+1]){
                   int temp=arr[j];
                   arr[j]=arr[j+1];
                   arr[j+1]=temp;
               }
           }
       }
       return arr;
   }

    /**
     * 获取二叉树最大节点
     * @param root
     * @return
     */
   public TreeNode maxNode(TreeNode root){
//       TreeNode left,right;
//       if (root==null) {
//           return null;
//       }
//       left=maxNode(root.left);
//       right=maxNode(root.right);
//       if (left!=null){
//           if (root.val<left.val){
//               root=left;
//           }
//       }
//       if (right!=null){
//           if (root.val<right.val){
//               root=right;
//           }
//       }
//       return root;

       if (root==null){
           return null;
       }
       TreeNode maxNode=root;
       TreeNode currNode=root;

       LinkedList<TreeNode> nodeLink=new LinkedList<>();
       while (currNode!=null||!nodeLink.isEmpty()){
           if (currNode!=null){
               nodeLink.push(currNode);
               currNode=currNode.left;
           }else {
               currNode=nodeLink.pop();
               if (maxNode.val<currNode.val){
                   maxNode=currNode;
               }
               currNode=currNode.right;
           }
       }
       return maxNode;
   }

    /**
     * 阶乘末尾0的个数
     * @param n
     * @return
     */
   public long trailingZeros(long n){

       if (n<5){
           return 0;
       }else{
           return (n/5+trailingZeros(n/5));
       }
   }

    /**
     * 合并排序数组
     * @param A
     * @param B
     * @return
     */
   public int[] mergeSortedArray(int[]A,int[]B){
       if (A==null){
           return B;
       }
       if (B==null){
           return A;
       }

       int[] a=new int[A.length+B.length];
       int i=0,j=0,k=0;
       while (k<=a.length-1){
           if (i<=A.length-1&&j<=B.length-1){
               if (A[i]<=B[j]){
                   a[k]=A[i];
                   i++;
               }else{
                   a[k]=B[j];
                   j++;
               }
           }else if (i>A.length-1&&j<=B.length-1){
               a[k]=B[j];
               j++;
           }else if (j>B.length-1&&i<=A.length-1){
               a[k]=A[i];
               i++;
           }
           k++;
       }
       return a;
   }

    /**
     * 反转元音字母
     * @param str
     * @return
     */
    public String reverseVowels(String str){
        Set<Character> set=new HashSet(Arrays.asList('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'));
        int i=0;
        int j=str.length()-1;
        char[] arr=new char[j+1];
        while (i<=j){
            char chi=str.charAt(i);
            char chj=str.charAt(j);
            if (!set.contains(chi)){
                arr[i]=chi;
                i++;
            }else if (!set.contains(chj)){
                arr[j]=chj;
                j--;
            }else{
                arr[i]=chj;
                arr[j]=chi;
                i++;
                j--;
            }
        }
        return new String(arr);
    }
   class TreeNode{
       public int val;
       public TreeNode left,right;
       public TreeNode(int val){
           this.val=val;
           this.left=this.right=null;
       }
   }

    /**
     * 选择字符串：给定一个字符串（以字符数组的形式给出）和一个偏移量，根据偏移量原地旋转字符串(从左向右旋转)
     *          输入:  str="abcdefg", offset = 3 输出: "efgabcd"
     * @param str
     * @param offset
     */
   public void rotateString(char[] str,int offset){
       char temp;
       if(offset==0) return;
       if(str.length==0)return;
       int len=str.length;
       for(int i=1;i<=offset%len;i++){
           temp=str[len-1];
           int j=len-2;
           while(j>=0){
               str[j+1]=str[j];
               j--;
           }
           str[0]=temp;
       }
   }

    public List<String> fizzBuzz(int n) {
       List<String> strs=new ArrayList<>();
       for (int i=1;i<=n;i++){
           if (i%3==0&&i%5==0){
               strs.add("fizz buzz");
           }else if (i%3==0){
               strs.add("fizz");
           }else if (i%5==0){
               strs.add("buzz");
           }else{
               strs.add(n+"");
           }
       }
       return strs;
    }

    public int strStr(String source, String target) {
        // Write your code here
        if (source==null||target==null)
        return -1;
        if (target.equals("")){
            return 0;
        }
        for (int i=0;i<=source.length()-target.length();i++){
            for (int j=0;j<target.length();j++){
                if (source.charAt(i+j)!=target.charAt(j)){
                    break;
                }
                if (j==target.length()-1){
                    return i;
                }
            }
        }
        return -1;
    }

    public int[] twoSum(int[] nums,int target){
//       int[] a = new int[2];
//       for (int i = 0;i<nums.length;i++){
//           for (int j = i+1;j<nums.length;j++){
//               if (nums[i]+nums[j] == target){
//                   a[0] = i;
//                   a[1] = j;
//                   return a;
//               }
//           }
//       }
//       return null;
        Map<Integer,Integer> map = new HashMap<>();
        for (int i = 0;i<nums.length;i++){
            int complement = target -nums[i];
            if (map.containsKey(complement)&&map.get(complement)!=i){
                return new int[]{i,map.get(complement)};
            }
            map.put(nums[i],i);
        }
        return null;
    }
    public ListNode addTwoNumbers(ListNode l1,ListNode l2){
        if (l1 == null){
            return l2;
        }else if (l2 == null){
            return l1;
        }
        ListNode ln = null;
        while (l1 != null || l2!=null){

        }

        return null;
    }
    class ListNode{
       int val;
       ListNode next;
       ListNode(int x){
           val = x;
       }
    }
}
