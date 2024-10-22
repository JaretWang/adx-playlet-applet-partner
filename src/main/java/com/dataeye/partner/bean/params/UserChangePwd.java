package com.dataeye.partner.bean.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jaret
 * @date 2024/10/22 15:12
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChangePwd {

    private String username;
    private String oldPwd;
    private String newPwd;

}
