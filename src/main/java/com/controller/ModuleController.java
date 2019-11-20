package com.controller;

import com.form.ModuleForm;
import com.result.Result;
import com.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("json/module")
public class ModuleController {
    @Autowired
    ModuleService moduleService;

    @RequestMapping("/list")
    public Result list(ModuleForm.ListForm form){

        return moduleService.list(form);
    }

    @RequestMapping("/listAll")
    public Result listAll(){

        return moduleService.listAll();
    }
}
