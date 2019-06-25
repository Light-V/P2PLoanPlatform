package com.scut.p2ploanplatform.controller;

import com.github.pagehelper.PageInfo;
import com.scut.p2ploanplatform.entity.Notice;
import com.scut.p2ploanplatform.service.impl.NoticeServiceImpl;
import com.scut.p2ploanplatform.utils.ResultVoUtil;
import com.scut.p2ploanplatform.vo.PageVo;
import com.scut.p2ploanplatform.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zonghang
 * Date 2019/6/24 23:06
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeServiceImpl noticeService;

    @PostMapping("/add")
    public ResultVo add(@RequestParam("title") String title, @RequestParam("content") String content,
                        @SessionAttribute("user") String userId) {
        Notice notice = noticeService.sendNotice(userId, title, content);
        return ResultVoUtil.success(notice);
    }

    @GetMapping("/all")
    public ResultVo all(@RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
                        @RequestParam(value = "page_size", defaultValue = "30") Integer pageSize,
                        @SessionAttribute(value = "user") String userId) {
        PageInfo<Notice> noticePageInfo = noticeService.getNotices(userId, pageNum, pageSize);
        return ResultVoUtil.success(new PageVo(
                noticePageInfo.getPages(),
                noticePageInfo.getTotal(),
                noticePageInfo.getPageSize(),
                noticePageInfo.getPageNum(),
                noticePageInfo.getList()
        ));
    }

    @GetMapping("/unread")
    public ResultVo unread(@RequestParam(value = "page_name", defaultValue = "1") Integer pageNum,
                           @RequestParam(value = "page_size", defaultValue = "30") Integer pageSize,
                           @SessionAttribute(value = "user") String userId) {
        PageInfo<Notice> noticePageInfo = noticeService.getUnreadNotices(userId, pageNum, pageSize);
        return ResultVoUtil.success(new PageVo(
                noticePageInfo.getPages(),
                noticePageInfo.getTotal(),
                noticePageInfo.getPageSize(),
                noticePageInfo.getPageNum(),
                noticePageInfo.getList()
        ));
    }

    @GetMapping("/read")
    public ResultVo read(@RequestParam(value = "notice_id", defaultValue = "") Integer noticeId,
                         @SessionAttribute(value = "user") String userId) {
        noticeService.readNotice(userId, noticeId);
        return ResultVoUtil.success();
    }

    @GetMapping("/delete")
    public ResultVo delete(@RequestParam(value = "notice_id", defaultValue = "") Integer noticeId,
                           @SessionAttribute(value = "user") String userId) {
        noticeService.deleteNotice(userId, noticeId);
        return ResultVoUtil.success();
    }
}
