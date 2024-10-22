package com.dataeye.partner.service;

import com.dataeye.partner.bean.PlayletPartnerUser;
import com.dataeye.partner.bean.params.UserChangePwd;
import com.dataeye.partner.mapper.PlayletPartnerUserMapper;
import com.dataeye.partner.security.MyRealm;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jaret
 * @date 2024/10/21 17:32
 * @description
 */
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private PlayletPartnerUserMapper playletPartnerUserMapper;

    @Override
    public int changePwd(UserChangePwd changePwd) {
        return 0;
    }

    @Override
    public int insertUser(List<PlayletPartnerUser> users) {
        users.forEach(e -> {
            //随机盐
            String salt = new SecureRandomNumberGenerator().nextBytes(10).toHex();
            Sha256Hash sha256Hash = new Sha256Hash(e.getPassword(), ByteSource.Util.bytes(salt), MyRealm.HASH_ITERATIONS);
            e.setPassword(sha256Hash.toString());
            e.setSalt(salt);
        });
        return playletPartnerUserMapper.insertBatch(users);
    }

}
