package org.example.bookstoreproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreproject.persistance.entity.Setting;
import org.example.bookstoreproject.security.CustomUserDetails;
import org.example.bookstoreproject.service.dto.CreateSettingDTO;
import org.example.bookstoreproject.service.dto.SettingDTO;
import org.example.bookstoreproject.service.services.PermissionService;
import org.example.bookstoreproject.service.services.SettingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/settings")
public class SettingController {
    private final SettingService settingService;
    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<SettingDTO> createSetting(@RequestBody CreateSettingDTO settingDTO,
                                                    @AuthenticationPrincipal CustomUserDetails user) {
        permissionService.checkPermission(user, "MANAGE_BOOK_METADATA");
        try {
            Setting setting = settingService.createSetting(settingDTO);
            return new ResponseEntity<>(SettingDTO.fromEntity(setting), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
