package com.scut.p2ploanplatform.service.impl;

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
    public List<Notice> getNotices(String userId) {
        return noticeDao.findByUserId(userId);
    }

    @Override
    public List<Notice> getUnreadNotices(String userId) {
        return noticeDao.findByUserIdAndStatus(userId, NoticeStatusEnum.UNREAD.getCode());
    }

    @Override
    public void readNotice(Integer noticeId) {
        Notice notice = noticeDao.findByNoticeId(noticeId);
        if (notice == null) {
            throw new CustomException(ResultEnum.NOTICE_NOT_EXIST);
        }
        if (notice.getStatus().equals(NoticeStatusEnum.READ.getCode())) {
            return;
        }
        notice.setStatus(NoticeStatusEnum.READ.getCode());
        noticeDao.updateStatus(notice);
    }
}
