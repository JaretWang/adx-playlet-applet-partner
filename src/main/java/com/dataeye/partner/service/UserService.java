package com.dataeye.partner.service;

import com.dataeye.partner.bean.PlayletPartnerUser;
import com.dataeye.partner.bean.params.UserChangePwd;

import java.util.List;

/**
 * @author jaret
 * @date 2024/10/21 17:32
 * @description
 */
public interface UserService {

    int changePwd(UserChangePwd changePwd);

    int insertUser(List<PlayletPartnerUser> users);

}
