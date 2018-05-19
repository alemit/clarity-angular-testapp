package com.github.clarityangulartestapp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.github.clarityangulartestapp.error.InvalidFieldValidationException;
import com.github.clarityangulartestapp.error.ValidationErrorField;
import com.github.clarityangulartestapp.model.NetworkInfo;
import com.github.clarityangulartestapp.model.User;
import com.github.clarityangulartestapp.repository.NetworkInfoRepository;
import com.github.clarityangulartestapp.security.JwtFilter;

@Service
public class NetworkInfoService {

    private static final Logger logger = LoggerFactory.getLogger(NetworkInfoService.class);

    private ApplicationContext applicationContext;
    private NetworkInfoRepository networkInfoRepository;

    @Autowired
    public NetworkInfoService(ApplicationContext applicationContext, NetworkInfoRepository networkInfoRepository) {
        super();
        this.applicationContext = applicationContext;
        this.networkInfoRepository = networkInfoRepository;
    }

    public List<NetworkInfo> findAllNetworkInfo() {
        logger.debug("[findAllNetworkInfo] called");
        return networkInfoRepository.findAll();
    }

    public NetworkInfo findNetworkInfoById(Long id) {
        logger.debug("[findNetworkInfoById] called with id=" + id);
        return networkInfoRepository.findOne(id);
    }

    public List<NetworkInfo> findNetworkInfoByIp(String ip) {
        logger.debug("[findNetworkInfoByIp] called with ip=" + ip);
        return networkInfoRepository.findNetworkInfoByIp(ip);
    }

    public List<NetworkInfo> findNetworkInfoByHostname(String hostname) {
        logger.debug("[findNetworkInfoByHostname] called with hostname=" + hostname);
        return networkInfoRepository.findNetworkInfoByHostname(hostname);
    }

    public NetworkInfo createNetworkInfo(NetworkInfo networkInfo) {
        logger.debug("[createNetworkInfo] called for " + networkInfo);
        networkInfo.setCreateUser(getSessionUser().getUsername());
        return networkInfoRepository.save(networkInfo);
    }

    public NetworkInfo updateNetworkInfoById(Long id, NetworkInfo networkInfo) {
        logger.debug("[updateNetworkInfoById] called with id=" + id + " for " + networkInfo);
        NetworkInfo ni = networkInfoRepository.findOne(id);

        if (ni == null) {
            return null;
        }
        ni.setId(id);
        ni.setIp(networkInfo.getIp());
        ni.setHostname(networkInfo.getHostname());
        return networkInfoRepository.save(ni);
    }

    public void deleteNetworkInfoById(Long id) throws InvalidFieldValidationException {
        logger.debug("[deleteNetworkInfoById] called for id=" + id);
        NetworkInfo ni = networkInfoRepository.findOne(id);

        if (ni == null) {
            throw new InvalidFieldValidationException("Invalid networkInfo id",
                    new ValidationErrorField("networkInfo", "id", "Invalid networkInfo id"));
        }

        networkInfoRepository.delete(id);
    }

    private User getSessionUser() {
        return (User) applicationContext.getBean(JwtFilter.REQUEST_BEAN);
    }
}
