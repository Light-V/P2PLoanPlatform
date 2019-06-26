package com.scut.p2ploanplatform.service.impl;

import com.github.pagehelper.PageInfo;
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
        PageInfo<Notice> noticePageInfo = noticeService.getNotices("123456789098", 1, 9);
        assertNotNull(noticePageInfo);
        assertEquals(1, noticePageInfo.getPageNum());
        assertEquals(9, noticePageInfo.getPageSize());
    }

    @Test
    @Transactional
    public void getUnreadNotices() {
        for (int i = 0; i < 5; ++i) {
            noticeService.sendNotice("123456789098", "Test", "This is a test");
        }
        PageInfo<Notice> noticePageInfo = noticeService.getUnreadNotices("123456789098", 1, 9);
        assertNotNull(noticePageInfo);
        assertEquals(1, noticePageInfo.getPageNum());
        assertEquals(9, noticePageInfo.getPageSize());
        for (Notice notice : noticePageInfo.getList()) {
            assertEquals(NoticeStatusEnum.UNREAD.getCode(), notice.getStatus());
        }
    }

    @Test
    @Transactional
    public void getReadNotices() {
        for (int i = 0; i < 5; ++i) {
            Notice notice = noticeService.sendNotice("123456789098", "Test", "This is a test");
            noticeService.readNotice("123456789098", notice.getNoticeId());
        }
        PageInfo<Notice> noticePageInfo = noticeService.getReadNotices("123456789098", 1, 9);
        assertNotNull(noticePageInfo);
        assertEquals(1, noticePageInfo.getPageNum());
        assertEquals(9, noticePageInfo.getPageSize());
        for (Notice notice : noticePageInfo.getList()) {
            assertEquals(NoticeStatusEnum.READ.getCode(), notice.getStatus());
        }
    }

    @Test
    @Transactional
    public void readNotice() {
        Notice notice = noticeService.sendNotice("123456789098", "Test", "This is a test");
        noticeService.readNotice(notice.getUserId(), notice.getNoticeId());
        Notice result = noticeDao.findByNoticeId(notice.getNoticeId());
        assertEquals(NoticeStatusEnum.READ.getCode(), result.getStatus());
    }

    @Test
    @Transactional
    public void deleteNotice() {
        Notice notice = noticeService.sendNotice("123456789098", "Test", "This is a test");
        noticeService.deleteNotice(notice.getUserId(), notice.getNoticeId());
        Notice result = noticeDao.findByNoticeId(notice.getNoticeId());
        assertEquals(NoticeStatusEnum.DELETE.getCode(), result.getStatus());
    }
}