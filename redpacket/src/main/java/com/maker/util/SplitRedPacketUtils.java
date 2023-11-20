package com.maker.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 红包拆分工具类
 * */
public class SplitRedPacketUtils {
    private int money;//保存总金额(单位为分)
    private int amount;//红包个数
    private List<Integer> allPackets=new ArrayList<>();//拆分结果
    private int currentMoney; //当前已分配金额
    private int currentAmount; //当前已分配红包的个数
    private Random random=new Random();//随机数工具类

    public SplitRedPacketUtils(int money,int amount){
            this.money=money;
            this.amount=amount;
            this.currentMoney=0;
            this.currentAmount=0;
            handle();
    }
    /**
     * 拆分的方法
     * */
    private void handle(){
       if(this.currentAmount==this.amount-1){
           //最后一个红包
           allPackets.add(this.money-this.currentMoney);
           this.currentAmount+=1;
           this.currentMoney=this.money;
           return;
       }
       //不是最后一个红包
        int bound=(int) ((this.money-this.currentMoney)*0.8);//每次拆分金额最大不超过剩余金额的80%
        int rand=randMoney(bound);
        this.currentMoney+=rand;
        this.currentAmount+=1;
        this.allPackets.add(rand);
        handle();//迭代调用拆分红包

    }
    /**
     * 生成随机金额
     * */
    private int randMoney(int bound){
        int result=0;
        while(result==0){//红包金额不可为0
            result=random.nextInt(bound);
        }
        return result;//一定会返回不为0的数
    }

    public List<Integer> getAllPackets(){
        return this.allPackets;
    }
}
