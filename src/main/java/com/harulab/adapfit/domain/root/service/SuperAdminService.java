package com.harulab.adapfit.domain.root.service;

import com.harulab.adapfit.domain.admin.domain.Admin;
import com.harulab.adapfit.domain.root.domain.repository.SuperAdminRepository;
import com.harulab.adapfit.domain.root.facade.SuperAdminFacade;
import com.harulab.adapfit.domain.root.presentation.dto.req.SuperAdminCreateRequestDto;
import com.harulab.adapfit.domain.root.presentation.dto.req.UpdateAccountInfoRequestDto;
import com.harulab.adapfit.domain.root.presentation.dto.res.SuperAdminResponseDto;
import com.harulab.adapfit.domain.admin.facade.AdminFacade;
import com.harulab.adapfit.domain.admin.presentation.dto.req.AdminRequestDto;
import com.harulab.adapfit.domain.admin.presentation.dto.res.AdminCreateResponseDto;
import com.harulab.adapfit.domain.admin.presentation.dto.res.AdminResponseDto;
import com.harulab.adapfit.global.annotation.ServiceWithTransactionalReadOnly;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ServiceWithTransactionalReadOnly
public class SuperAdminService {

    private final SuperAdminRepository superAdminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminFacade adminFacade;
    private final SuperAdminFacade superAdminFacade;

    public SuperAdminResponseDto getMine() {
        return new SuperAdminResponseDto(superAdminFacade.getCurrentAdmin());
    }

    @Transactional
    public void createRoot(SuperAdminCreateRequestDto req) {
        superAdminRepository.save(req.toEntity()).encodePassword(passwordEncoder);
    }

    @Transactional
    public AdminCreateResponseDto createAdmin() {
        AdminRequestDto adminRequestDto = new AdminRequestDto();
        Admin admin = adminFacade.save(adminRequestDto.toEntity());
        admin.encodePassword(passwordEncoder);
        return new AdminCreateResponseDto(adminRequestDto.getAuthId());
    }

    public List<AdminResponseDto> getUserList() {
        return adminFacade.findAll()
                .stream()
                .map(AdminResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateAdminInfo(Long adminId, UpdateAccountInfoRequestDto req) {
        adminFacade.findById(adminId).updateInfo(req);
    }

}
