package com.scut.p2ploanplatform.entity;

import lombok.Data;
import org.apache.ibatis.annotations.Options;

import java.util.Date;

/**
 * Created by zonghang
 * Date 2019/6/16 10:24
 */
@Data
public class Notice {

    private Integer noticeId;

    private String userId;

    private String title;

    private String content;

    private Date time;

    private Integer status;
}
