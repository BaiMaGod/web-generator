package com.controller;

import com.form.CreateForm;
import com.result.Result;
import com.service.CreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("json/create")
public class CreateController {
    @Autowired
    CreateService createService;

    /**
     * 生成 基本 结构
     * @param form
     * @return
     */
    @PostMapping("/base")
    public Result base(CreateForm.BaseForm form){

        return createService.base(form);
    }

    /**
     * 生成 web 项目
     * @param form
     * @return
     */
    @PostMapping("/web")
    public Result web(CreateForm.WebForm form){

        return createService.web(form);
    }
}
