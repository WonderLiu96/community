package com.wonder.dao;

import com.wonder.model.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author: wonder
 * @Date: 2020/1/2
 */
@Mapper
public interface LoginTicketDAO {
    String TABLE_NAME = "login_ticket";
    String INSERT_FIELDS = " user_id, expired, status, ticket ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    /**
     * 添加ticket
     * @param ticket
     * @return
     */
    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,
            ") values (#{userId},#{expired},#{status},#{ticket})"})
    int addTicket(LoginTicket ticket);

    /**
     * 根据ticket获取登录ticket
     * @param ticket
     * @return
     */
    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME,
            " where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    /**
     * 更新ticket状态
     * @param ticket
     * @param status
     */
    @Update({"update ",TABLE_NAME," set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket")String ticket,@Param("status")int status);
}
