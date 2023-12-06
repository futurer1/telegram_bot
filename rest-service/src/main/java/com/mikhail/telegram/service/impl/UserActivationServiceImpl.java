package com.mikhail.telegram.service.impl;

import com.mikhail.telegram.dao.AppUserDAO;
import com.mikhail.telegram.entity.AppUser;
import com.mikhail.telegram.service.UserActivationService;
import com.mikhail.telegram.utils.CryptoTool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserActivationServiceImpl implements UserActivationService {

    private final AppUserDAO appUserDAO;

    private final CryptoTool cryptoToolUserId;

    public UserActivationServiceImpl(AppUserDAO appUserDAO,
                                     @Qualifier("CryptoToolUserId") CryptoTool cryptoToolUserId
    ) {
        this.appUserDAO = appUserDAO;
        this.cryptoToolUserId = cryptoToolUserId;
    }


    /**
     * Активация пользователя
     *
     * @param cryptoUserId
     * @return
     */
    @Override
    public boolean activate(String cryptoUserId) {
        Long userId = cryptoToolUserId.idOf(cryptoUserId);
        Optional<AppUser> appUserOptional = appUserDAO.findUserById(userId);
        if (appUserOptional.isPresent()) {

            AppUser appUser = appUserOptional.get();
            appUser.setIsActive(true);
            appUserDAO.save(appUser);

            return true;
        }

        return false;
    }
}
