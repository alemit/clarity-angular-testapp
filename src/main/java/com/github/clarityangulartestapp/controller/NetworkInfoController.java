package com.github.clarityangulartestapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.clarityangulartestapp.model.NetworkInfo;
import com.github.clarityangulartestapp.service.NetworkInfoService;

@RestController
public class NetworkInfoController {
    @Autowired
    NetworkInfoService networkInfoService;

    @RequestMapping(path = "/network-info", method = RequestMethod.GET)
    public List<NetworkInfo> findAllNetworkInfo() {
        return networkInfoService.findAllNetworkInfo();
    }

    @RequestMapping(path = "/network-info/{id}", method = RequestMethod.GET)
    public NetworkInfo findNetworkInfoById(@PathVariable("id") Long id) {
        return networkInfoService.findNetworkInfoById(id);
    }

    @RequestMapping(path = "/network-info", method = RequestMethod.POST)
    public ResponseEntity<?> createNetworkInfo(@RequestBody NetworkInfo networkInfo) {
        NetworkInfo ni = networkInfoService.createNetworkInfo(networkInfo);
        return new ResponseEntity<NetworkInfo>(ni, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/network-info/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateNetworkInfoById(@PathVariable("id") Long id, @RequestBody NetworkInfo networkInfo) {
        NetworkInfo ni = networkInfoService.updateNetworkInfoById(id, networkInfo);
        if (ni == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ni);
    }
    
    @RequestMapping(path = "/network-info/{id}", method = RequestMethod.DELETE)
    public void deleteNetworkInfoById(@PathVariable("id") Long id) {
        networkInfoService.deleteNetworkInfoById(id);
    }
}
