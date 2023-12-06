package com.mikhail.telegram.service;

import com.mikhail.telegram.entity.AppUser;

public interface AppUserService {

    String registryUser(AppUser appUser);

    String setEmail(AppUser appUser, String email);
}
