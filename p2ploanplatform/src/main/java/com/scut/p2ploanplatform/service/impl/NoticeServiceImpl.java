package com.scut.p2ploanplatform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.scut.p2ploanplatform.dao.NoticeDao;
import com.scut.p2ploanplatform.entity.Notice;
import com.scut.p2ploanplatform.enums.NoticeStatusEnum;
import com.scut.p2ploanplatform.enums.ResultEnum;
import com.scut.p2ploanplatform.exception.CustomException;
import com.scut.p2ploanplatform.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by zonghang
 * Date 2019/6/16 21:15
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeDao noticeDao;

    private final String ORDER_BY = "time desc";

    @Override
    public Notice sendNotice(String receiverId, String title, String content) {
        Notice notice = new Notice();
        notice.setUserId(receiverId);
        notice.setTitle(title);
        notice.setContent(content);
        notice.setStatus(NoticeStatusEnum.UNREAD.getCode());
        notice.setTime(new Date());
        noticeDao.insert(notice);
        return notice;
    }

    @Override
    public PageInfo<Notice> getNotices(String userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Notice> noticeList = noticeDao.findByUserId(userId);
        return new PageInfo<>(noticeList);
    }

    @Override
    public PageInfo<Notice> getUnreadNotices(String userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Notice> noticeList = noticeDao.findByUserIdAndStatus(userId, NoticeStatusEnum.UNREAD.getCode());
        return new PageInfo<>(noticeList);
    }

    @Override
    public PageInfo<Notice> getReadNotices(String userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Notice> noticeList = noticeDao.findByUserIdAndStatus(userId, NoticeStatusEnum.READ.getCode());
        return new PageInfo<>(noticeList);
    }

    @Override
    public void deleteNotice(String userId, Integer noticeId) {
        Notice notice = noticeDao.findByNoticeId(noticeId);
        if (notice == null || !notice.getUserId().equals(userId)) {
            throw new CustomException(ResultEnum.NOTICE_NOT_EXIST);
        }
        if (notice.getStatus().equals(NoticeStatusEnum.DELETE.getCode())) {
            return;
        }
        notice.setStatus(NoticeStatusEnum.DELETE.getCode());
        noticeDao.updateStatus(notice);
    }

    @Override
    public void readNotice(String userId, Integer noticeId) {
        Notice notice = noticeDao.findByNoticeId(noticeId);
        if (notice == null || !notice.getUserId().equals(userId)) {
            throw new CustomException(ResultEnum.NOTICE_NOT_EXIST);
        }
        if (notice.getStatus().equals(NoticeStatusEnum.READ.getCode())) {
            return;
        }
        notice.setStatus(NoticeStatusEnum.READ.getCode());
        noticeDao.updateStatus(notice);
    }
}
