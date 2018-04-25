package com.github.clarityangulartestapp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.github.clarityangulartestapp.model.NetworkInfo;

public interface NetworkInfoRepository extends CrudRepository<NetworkInfo, Long> {
    List<NetworkInfo> findAll();

    List<NetworkInfo> findNetworkInfoByIp(String ip);

    List<NetworkInfo> findNetworkInfoByHostname(String hostname);
}
