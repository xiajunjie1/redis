package com.maker.service;

import com.maker.util.SplitRedPacketUtils;
import com.maker.util.ValidateRedPacketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface IRedPacketService {
   public List<Integer> split(int money,int amount);
   public String add(String userid,List<Integer> packets);

   public Integer grab(String key,String userid);

   public Map<String,Integer> getResult(String key);
}
