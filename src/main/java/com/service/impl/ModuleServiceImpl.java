package com.service.impl;

import com.form.ModuleForm;
import com.mapper.ModuleMapper;
import com.model.Module;
import com.result.Result;
import com.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleServiceImpl implements ModuleService {
    @Autowired
    ModuleMapper moduleMapper;

    @Override
    public Result list(ModuleForm.ListForm form) {
        List<Module> modules = moduleMapper.list(form);

        return Result.success(modules);
    }

    @Override
    public Result listAll() {
        List<Module> modules = moduleMapper.listAll();

        return Result.success(modules);
    }
}
