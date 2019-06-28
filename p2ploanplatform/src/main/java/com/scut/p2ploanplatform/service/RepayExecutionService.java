package com.scut.p2ploanplatform.service;

import com.scut.p2ploanplatform.vo.RepayExecutionResultVo;

import java.util.List;

public interface RepayExecutionService {

    /**
     * 执行还款流程，参考详细设计文档的顺序图（定时触发，不推荐模块外部调用）
     */
    List<RepayExecutionResultVo> doRepay();
}
