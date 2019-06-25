package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.Notice;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zonghang
 * Date 2019/6/16 10:47
 */
@Mapper
@Repository
public interface NoticeDao {

    @Insert("insert into notice(user_id, title, content, time, status) values (#{userId}, #{title}, #{content}, #{time}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "noticeId", keyColumn = "notice_id")
    int insert(Notice notice);

    @Select("select * from notice where notice_id = #{noticeId}")
    Notice findByNoticeId(Integer noticeId);

    @Select("select * from notice where user_id = #{userId} and status <> 2")
    List<Notice> findByUserId(String userId);

    @Select("select * from notice where user_id = #{userId} and status = #{status}")
    List<Notice> findByUserIdAndStatus(String userId, Integer status);

    @Update("update notice set status = #{status} where notice_id = #{noticeId}")
    int updateStatus(Notice notice);
}
