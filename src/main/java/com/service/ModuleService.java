package com.service;

import com.form.ModuleForm;
import com.result.Result;

public interface ModuleService {
    Result list(ModuleForm.ListForm form);

    Result listAll();
}
