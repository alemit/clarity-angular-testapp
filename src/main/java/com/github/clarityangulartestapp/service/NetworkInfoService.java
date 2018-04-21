package com.github.clarityangulartestapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.clarityangulartestapp.model.NetworkInfo;
import com.github.clarityangulartestapp.repository.NetworkInfoRepository;

@Service
public class NetworkInfoService {
    @Autowired
    NetworkInfoRepository networkInfoRepository;

    public List<NetworkInfo> findAllNetworkInfo() {
        return networkInfoRepository.findAll();
    }

    public NetworkInfo findNetworkInfoById(Long id) {
        return networkInfoRepository.findOne(id);
    }

    public List<NetworkInfo> findByIp(String ip) {
        return networkInfoRepository.findByIp(ip);
    }

    public List<NetworkInfo> findByHostname(String hostname) {
        return networkInfoRepository.findByHostname(hostname);
    }

    public NetworkInfo createNetworkInfo(NetworkInfo networkInfo) {
        return networkInfoRepository.save(networkInfo);
    }

    public NetworkInfo updateNetworkInfoById(Long id, NetworkInfo networkInfo) {
        NetworkInfo ni = networkInfoRepository.findOne(id);

        if (ni == null) {
            return null;
        }
        ni.setId(id);
        ni.setIp(networkInfo.getIp());
        ni.setHostname(networkInfo.getHostname());
        return networkInfoRepository.save(ni);
    }

    public void deleteNetworkInfoById(Long id) {
        networkInfoRepository.delete(id);
    }
}
