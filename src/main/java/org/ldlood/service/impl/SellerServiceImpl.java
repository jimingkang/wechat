package org.ldlood.service.impl;

import org.ldlood.dataobject.SellerInfo;
import org.ldlood.repository.SellerInfoRepository;
import org.ldlood.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by Ldlood on 2017/8/10.
 */
@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerInfoRepository repository;

    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        return repository.findByOpenid(openid);
    }
}

