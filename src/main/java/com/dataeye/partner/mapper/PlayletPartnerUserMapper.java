package com.dataeye.partner.mapper;

import com.dataeye.partner.bean.PlayletPartnerUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author jaret
 * @date 2024/10/21 17:33
 * @description
 */
@Mapper
public interface PlayletPartnerUserMapper {

    @Select("select * from playlet_partner_user where username = #{username}")
    PlayletPartnerUser findByUsername(String username);

    int insertBatch(@Param("entities") List<PlayletPartnerUser> entities);

}
