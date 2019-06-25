package com.scut.p2ploanplatform.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by zonghang
 * Date 2019/6/24 23:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVo {

    private Integer pages;

    private Long total;

    private Integer size;

    private Integer currentPage;

    private List list;
}
