package com.service;

import com.form.CreateForm;
import com.result.Result;

public interface CreateService {
    Result base(CreateForm.BaseForm form);

    Result web(CreateForm.WebForm form);
}
