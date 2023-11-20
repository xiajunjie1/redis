package com.maker.spring.dao.impl;

import com.maker.spring.dao.IMessageDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class MessageDaoImpl implements IMessageDao {
    private static final Logger LOGGER= LoggerFactory.getLogger(MessageDaoImpl.class);
    @Override
    public List<String> selectAll() {
        LOGGER.info("【MessageDAO数据层】调用了selectAll方法(实际中用MP或者JPA完成)");
        return List.of("Hello","Nice","Good");
    }
}
