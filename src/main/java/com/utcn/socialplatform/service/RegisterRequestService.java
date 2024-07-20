package com.utcn.socialplatform.service;


import com.utcn.socialplatform.dtos.RegisterRequestDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface RegisterRequestService {
    void approveUser(String email) throws ApiExceptionResponse;
    void denyUser(String email, String reason) throws ApiExceptionResponse;
    List<RegisterRequestDTO> getUnvalidatedUser();
    void resetPassword(String email) throws ApiExceptionResponse;
}