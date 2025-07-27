package com.project.dozeo_appleGame.web.service.account;

public interface Oauth2UnlinkService {
    void unlink(String provider, String accessToken) throws Exception;
    void removeOauth2User(Long id);

}
