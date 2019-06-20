package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.Notice;
import com.scut.p2ploanplatform.enums.NoticeStatusEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
/**
 * Created by zonghang
 * Date 2019/6/16 10:55
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class NoticeDaoTest {

    @Autowired
    private NoticeDao noticeDao;

    @Test
    @Transactional
    public void insertTest() {
        Notice notice = new Notice();
        notice.setUserId("123456789098");
        notice.setTitle("test");
        notice.setContent("This is a test.");
        notice.setTime(new Date());
        notice.setStatus(NoticeStatusEnum.UNREAD.getCode());
        int result = noticeDao.insert(notice);
        assertEquals(1, result);
        assertNotNull(notice.getNoticeId());
    }

    @Test
    @Transactional
    public void findByNoticeId() {
        Notice notice = new Notice();
        notice.setUserId("123456789098");
        notice.setTitle("test");
        notice.setContent("This is a test.");
        notice.setTime(new Date());
        notice.setStatus(NoticeStatusEnum.UNREAD.getCode());
        noticeDao.insert(notice);
        Notice result = noticeDao.findByNoticeId(notice.getNoticeId());
        assertNotNull(result);
    }

    @Test
    @Transactional
    public void findByUserIdTest() {
        Notice notice = new Notice();
        notice.setUserId("123456789098");
        notice.setTitle("test");
        notice.setContent("This is a test.");
        notice.setTime(new Date());
        notice.setStatus(NoticeStatusEnum.UNREAD.getCode());
        noticeDao.insert(notice);
        List<Notice> noticeList = noticeDao.findByUserId("123456789098");
        for (Notice n : noticeList) {
            assertNotNull(n.getNoticeId());
            assertNotNull(n.getUserId());
            assertNotNull(n.getContent());
            assertNotNull(n.getStatus());
            assertNotNull(n.getTime());
        }
    }

    @Test
    @Transactional
    public void findByUserIdAndStatusTest() {
        Notice notice = new Notice();
        notice.setUserId("123456789098");
        notice.setTitle("test");
        notice.setContent("This is a test.");
        notice.setTime(new Date());
        notice.setStatus(NoticeStatusEnum.UNREAD.getCode());
        noticeDao.insert(notice);
        notice = new Notice();
        notice.setUserId("123456789098");
        notice.setTitle("test");
        notice.setContent("This is a test.");
        notice.setTime(new Date());
        notice.setStatus(NoticeStatusEnum.READ.getCode());
        noticeDao.insert(notice);
        List<Notice> noticeList = noticeDao.findByUserIdAndStatus("123456789098", NoticeStatusEnum.UNREAD.getCode());
        for (Notice n : noticeList) {
            assertNotNull(n.getNoticeId());
            assertNotNull(n.getUserId());
            assertNotNull(n.getContent());
            assertNotNull(n.getStatus());
            assertNotNull(n.getTime());
        }
    }

    @Test
    @Transactional
    public void updateStatus() {
        Notice notice = new Notice();
        notice.setUserId("123456789098");
        notice.setTitle("test");
        notice.setContent("This is a test.");
        notice.setTime(new Date());
        notice.setStatus(NoticeStatusEnum.UNREAD.getCode());
        noticeDao.insert(notice);
        notice.setStatus(NoticeStatusEnum.READ.getCode());
        int result = noticeDao.updateStatus(notice);
        assertEquals(1, result);
    }
}