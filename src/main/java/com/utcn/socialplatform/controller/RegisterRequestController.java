package com.utcn.socialplatform.controller;

import com.utcn.socialplatform.dtos.RegisterRequestDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import com.utcn.socialplatform.service.RegisterRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.utcn.socialplatform.model.Constants.*;

@RestController
@RequestMapping(API_V1)
public class RegisterRequestController {

    private final RegisterRequestService registerRequestService;

    public RegisterRequestController(RegisterRequestService registerRequestService) {
        this.registerRequestService = registerRequestService;
    }

    @PatchMapping(REGISTER+APPROVE_REQUEST+"/{email_address}")
    public ResponseEntity<String> approveAuthenticationRequest(@PathVariable String email_address) {
        try {
            registerRequestService.approveUser(email_address);
            return ResponseEntity.ok("The account for email " + email_address + " was validated.");
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.status(e.getStatus())
                    .body(e.getMessage());
        }
    }

    @PostMapping(REGISTER+DENY_REQUEST+"/{email_address}")
    public ResponseEntity<String> denyRequest(@PathVariable("email_address") String email, @RequestBody String reason) {
        try {
            if(reason == null || reason.isEmpty()) registerRequestService.denyUser(email, "Nu a fost oferit niciun motiv.");
            else registerRequestService.denyUser(email,reason);
            return ResponseEntity.ok("User with email " + email + " has been denied.");
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping(AUTHENTICATION+REGISTER_REQUESTS)
    public ResponseEntity<List<RegisterRequestDTO>> getRegisterRequests() {
        List<RegisterRequestDTO> registerRequests = registerRequestService.getUnvalidatedUser();
        return ResponseEntity.ok(registerRequests);
    }

    @PatchMapping(REGISTER+RESET_PASSWORD+"/{email}")
    public ResponseEntity<?> resetPassword(@PathVariable String email) {
        try {
            registerRequestService.resetPassword(email);
            return ResponseEntity.ok("Password reset request has been successfully processed.");
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}