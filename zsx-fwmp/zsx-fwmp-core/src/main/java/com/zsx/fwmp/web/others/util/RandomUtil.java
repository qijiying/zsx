package com.zsx.fwmp.web.others.util;

import java.util.Random;
import java.util.Vector;

/**
 * @ClassName RandomUtil
 * @description 随机数工具类
 * @author lz
 * @date 2018年7月4日10:29:26
 */
public class RandomUtil {


	 /**
	 * @Title RandomNum
	 * @param n
	 * @param m
	 * @description 获得n条m之内的随机数
	 * @return
	 */
	public static Vector<Integer> RandomNum(int n, int m){    
	     Vector<Integer> random = new Vector<Integer>();; //定义一个向量，用于动态数组的存储
	     //随机数的范围，可以生成10 个0-100的数，那么n=10，m=100.
	     Random ran = new Random();
	     int ra = ran.nextInt(m);
	         random.add(ra);   //先生成第一位随机数，后面就有的比较了。
	         for (int i = 0; i < n; i++) {
	             int r = ran.nextInt(m);
	             for (int j = 0; j < i; j++) {
	                 if (random.get(j) == r) { //一旦发现有重复的数，立即结束循环，并重新执行第j步
	                     i--;
	                     break;
	                 } else { //只要没有重复，那么就在第i个位置写入新数据。多次反复j遍
	                     if (random.size() > i)
	                    	 random.set(i, r);
	                     else
	                    	 random.add(r);
	                  }
	              }
	          }
           return random;
	  }
	
}
