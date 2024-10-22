package com.dataeye.partner.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author jaret
 * @date 2024/10/21 17:33
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayletPartnerUser implements Serializable {

    private Long id;
    private String username;
    private String password;
    private String salt;

}
