package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.entity.Notice;
import com.scut.p2ploanplatform.service.NoticeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zonghang
 * Date 2019/6/16 21:15
 */
@Service
public class NoticeServiceImpl implements NoticeService {
    @Override
    public Notice sendNotice(String receiverId, String content) {
        return null;
    }

    @Override
    public List<Notice> getNotices(String userId) {
        return null;
    }

    @Override
    public List<Notice> getUnreadNotices(String userId) {
        return null;
    }

    @Override
    public void readNotice(Integer noticeId) {

    }
}
