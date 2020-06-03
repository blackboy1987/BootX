
package com.bootx.controller.admin;

import com.bootx.common.Message;
import com.bootx.common.Page;
import com.bootx.common.Pageable;
import com.bootx.entity.Log;
import com.bootx.service.LogService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/log")
public class LogController extends BaseController {

    @Resource
    private LogService logService;

    @PostMapping("/list")
    public Message list(Pageable pageable) {
        return Message.success(logService.findPage(pageable));
    }

    @PostMapping("/view")
    public Log view(Long id) {
        return logService.find(id);
    }

    @PostMapping("/delete")
    public @ResponseBody
    Message delete(Long[] ids) {
        logService.delete(ids);
        return SUCCESS_MESSAGE;
    }

    @PostMapping("/clear")
    public @ResponseBody
    Message clear() {
        logService.clear();
        return SUCCESS_MESSAGE;
    }

}
