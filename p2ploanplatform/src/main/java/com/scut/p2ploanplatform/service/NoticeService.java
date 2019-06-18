package com.scut.p2ploanplatform.service;

import com.scut.p2ploanplatform.entity.Notice;

import java.util.List;

/**
 * Created by zonghang
 * Date 2019/6/16 11:32
 */
public interface NoticeService {

    /**
     * 发送通知
     * @param  receiverId 被通知的人的id（用户或担保人）
     * @param title 通知的标题
     * @param content 通知的内容
     * @return 通知成功后返回Notice
     */
    Notice sendNotice(String receiverId, String title, String content);

    /**
     * 获取某个用户的所有通知
     * @param userId 用户或担保人id
     * @return List<Notice>
     */
    List<Notice> getNotices(String userId);

    /**
     * 获取某个用户的未读通知
     * @param userId 用户或担保人id
     * @return List<Notice>
     */
    List<Notice> getUnreadNotices(String userId);

    /**
     * 读通知，将通知状态改成已读
     * @param noticeId 通知的id
     */
    void readNotice(Integer noticeId);
}
