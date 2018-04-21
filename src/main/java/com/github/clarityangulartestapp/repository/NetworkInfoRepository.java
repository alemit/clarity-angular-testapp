package com.github.clarityangulartestapp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.github.clarityangulartestapp.model.NetworkInfo;

public interface NetworkInfoRepository extends CrudRepository<NetworkInfo, Long> {
    List<NetworkInfo> findAll();

    List<NetworkInfo> findByIp(String ip);

    List<NetworkInfo> findByHostname(String hostname);
}
