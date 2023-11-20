package com.maker.util;

import java.util.Iterator;
import java.util.List;

public class ValidateRedPacketUtil {

    public static boolean check(List<Integer> packets,int money){
        if(packets==null || packets.size()==0){
            return false; //红包不存在
        }
        int sum=0;//计算红包总金额
       Iterator<Integer> iterator = packets.iterator();
       while(iterator.hasNext()){
        int data=iterator.next();
        if(data==0)return false;//红包金额不可为0
           sum+=data;

       }
       return sum==money; //验证输入金额和红包总金额是否相等
    }
    }
