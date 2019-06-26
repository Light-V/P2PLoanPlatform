package com.scut.p2ploanplatform.service;

import com.github.pagehelper.PageInfo;
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
     * @param pageNum 页码
     * @param pageSize 每页记录数
     * @return PageInfo<Notice>
     */
    PageInfo<Notice> getNotices(String userId, Integer pageNum, Integer pageSize);

    /**
     * 获取某个用户的未读通知
     * @param userId 用户或担保人id
     * @param pageNum 页码
     * @param pageSize 每页记录数
     * @return PageInfo<Notice>
     */
    PageInfo<Notice> getUnreadNotices(String userId, Integer pageNum, Integer pageSize);

    /**
     * 获取已读通知
     * @param userId 用户或担保人id
     * @param pageNum 页码
     * @param pageSize  每页记录数
     * @return
     */
    PageInfo<Notice> getReadNotices(String userId, Integer pageNum, Integer pageSize);

    /**
     * 读通知，将通知状态改成已读
     * @param userId 用户id
     * @param noticeId 通知的id
     */
    void readNotice(String userId, Integer noticeId);

    /**
     * 删除通知
     * @param userId 用户id
     * @param noticeId 通知的id
     */
    void deleteNotice(String userId, Integer noticeId);
}
