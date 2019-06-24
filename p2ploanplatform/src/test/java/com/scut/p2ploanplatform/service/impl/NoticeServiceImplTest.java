package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.NoticeDao;
import com.scut.p2ploanplatform.entity.Notice;
import com.scut.p2ploanplatform.enums.NoticeStatusEnum;
import com.scut.p2ploanplatform.enums.ResultEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by zonghang
 * Date 2019/6/20 10:19
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class NoticeServiceImplTest {

    @Autowired
    private NoticeServiceImpl noticeService;

    @Autowired
    private NoticeDao noticeDao;

    @Test
    @Transactional
    public void sendNotice() {
        Notice notice = noticeService.sendNotice("123456789098", "Test", "This is a test");
        assertNotNull(notice);
        assertNotNull(notice.getNoticeId());
    }

    @Test
    @Transactional
    public void getNotices() {
        for (int i = 0; i < 5; ++i) {
            noticeService.sendNotice("123456789098", "Test", "This is a test");
        }
        List<Notice> noticeList = noticeService.getNotices("123456789098");
        assertNotNull(noticeList);
    }

    @Test
    @Transactional
    public void getUnreadNotices() {
        for (int i = 0; i < 5; ++i) {
            noticeService.sendNotice("123456789098", "Test", "This is a test");
        }
        List<Notice> noticeList = noticeService.getUnreadNotices("123456789098");
        assertNotNull(noticeList);
    }

    @Test
    @Transactional
    public void readNotice() {
        Notice notice = noticeService.sendNotice("123456789098", "Test", "This is a test");
        noticeService.readNotice(notice.getNoticeId());
        Notice result = noticeDao.findByNoticeId(notice.getNoticeId());
        assertEquals(NoticeStatusEnum.READ.getCode(), result.getStatus());
    }
}