package com.maker.spring.service.impl;

import com.maker.spring.dao.IMessageDao;
import com.maker.spring.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MessageServiceImpl implements IMessageService {
    @Autowired
    private IMessageDao messageDao;
    @Override
    public List<String> findAll() {

        return messageDao.selectAll();
    }
}
