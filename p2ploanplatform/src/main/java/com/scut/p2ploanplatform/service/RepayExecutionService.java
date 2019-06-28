package com.scut.p2ploanplatform.service;

public interface RepayExecutionService {

    /**
     * 执行还款流程，参考详细设计文档的顺序图（定时触发，不推荐模块外部调用）
     */
    void doRepay();
}
