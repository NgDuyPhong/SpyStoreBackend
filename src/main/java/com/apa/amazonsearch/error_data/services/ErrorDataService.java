package com.apa.amazonsearch.error_data.services;

import com.apa.amazonsearch.error_data.models.ErrorData;
import com.apa.users.models.User;

import java.util.List;

public interface ErrorDataService {
    List<ErrorData> getErrors(User user);
}
