package com.apa.amazonsearch.error_data.services;

import com.apa.amazonsearch.error_data.models.ErrorData;
import com.apa.amazonsearch.error_data.repositories.ErrorDataRepository;
import com.apa.users.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ErrorDataServiceImpl implements ErrorDataService {
    private final ErrorDataRepository errorDataRepository;

    @Override
    public List<ErrorData> getErrors(User user) {
        return errorDataRepository.findByUserId(user.getId());
    }
}
