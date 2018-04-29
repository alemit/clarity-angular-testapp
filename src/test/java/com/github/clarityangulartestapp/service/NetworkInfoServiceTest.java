package com.github.clarityangulartestapp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.clarityangulartestapp.error.InvalidFieldValidationException;
import com.github.clarityangulartestapp.model.NetworkInfo;
import com.github.clarityangulartestapp.repository.NetworkInfoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NetworkInfoServiceTest {

    private NetworkInfo netInfoGoogle;
    private NetworkInfo netInfoYahoo;

    private List<NetworkInfo> networkInfoList;

    @MockBean
    NetworkInfoRepository networkInfoRepository;

    @Autowired
    NetworkInfoService networkInfoService;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        netInfoGoogle = newNetworkInfo(1L, "192.168.1.1", "https://google.com");
        netInfoYahoo = newNetworkInfo(2L, "192.168.1.2", "https://yahoo.com");

        networkInfoList = new ArrayList<NetworkInfo>();
        networkInfoList.add(netInfoGoogle);
        networkInfoList.add(netInfoYahoo);

        when(networkInfoRepository.findOne(null)).thenThrow(IllegalArgumentException.class);
        when(networkInfoRepository.findAll()).thenReturn(networkInfoList);
        when(networkInfoRepository.findOne(1L)).thenReturn(netInfoGoogle);
        when(networkInfoRepository.findNetworkInfoByIp("192.168.1.1")).thenReturn(Arrays.asList(netInfoGoogle));
        when(networkInfoRepository.findNetworkInfoByHostname("https://google.com")).thenReturn(Arrays.asList(netInfoGoogle));
        when(networkInfoRepository.save(netInfoGoogle)).thenReturn(netInfoGoogle);
    }

    @Test
    public void whenFindAllNetworkInfo_thenNetworkInfoListFound() {
        List<NetworkInfo> returnedNetworkInfoList = this.networkInfoService.findAllNetworkInfo();
        assertEquals(networkInfoList, returnedNetworkInfoList);
        assertTrue(returnedNetworkInfoList.contains(netInfoGoogle));
        assertTrue(returnedNetworkInfoList.contains(netInfoYahoo));
    }

	@Test(expected = IllegalArgumentException.class)
    public void givenIdNull_whenFindNetworkInfoById_thenThrowException() {
        this.networkInfoService.findNetworkInfoById(null);
    }

    @Test
    public void givenIncorrectId_whenFindNetworkInfoById_thenReturnNull() {
        NetworkInfo findNetworkInfoByIncorrectId = this.networkInfoService.findNetworkInfoById(10L);
        assertNull(findNetworkInfoByIncorrectId);
    }

    @Test
    public void givenExistingIp_whenFindNetworkInfoByIp_thenNetworkInfoListReturned() {
        List<NetworkInfo> networkInfoList = this.networkInfoService.findNetworkInfoByIp("192.168.1.1");
        assertTrue(networkInfoList.contains(netInfoGoogle));
        assertEquals(1, networkInfoList.size());
    }

    @Test
    public void givenValidHostname_whenFindNetworkInfoByHostname_thenNetworkInfoListFound() {
        List<NetworkInfo> networkInfoList = this.networkInfoService.findNetworkInfoByHostname("https://google.com");
        assertTrue(networkInfoList.contains(netInfoGoogle));
        assertEquals(1, networkInfoList.size());
    }

    @Test
    public void givenNetworkInfo_whenCreateNetworkInfo_thenNetworkInfoWithId() {
        NetworkInfo createNetworkInfo = this.networkInfoService.createNetworkInfo(netInfoGoogle);
        assertEquals(netInfoGoogle, createNetworkInfo);
    }

    @Test
    public void givenValidUpdateNetworkInfoRequest_whenUpdateNetworkInfoById_thenUpdate() {
        NetworkInfo updateNetworkInfoById = this.networkInfoService.updateNetworkInfoById(netInfoGoogle.getId(), netInfoGoogle);
        assertEquals(netInfoGoogle, updateNetworkInfoById);
    }

    @Test
    public void givenInvalidNetworkInfoId_whenUpdateNetworkInfoById_thenNullReturned() {
        NetworkInfo updateNetworkInfoById = this.networkInfoService.updateNetworkInfoById(10L, netInfoGoogle);
        assertNull(updateNetworkInfoById);
    }

    @Test(expected = InvalidFieldValidationException.class)
    public void givenInvalidNetworkInfoId_whenDeleteNetworkInfoById_thenThrowValidationException() throws InvalidFieldValidationException {
        this.networkInfoService.deleteNetworkInfoById(10L);
    }

    @Test
    public void givenValidNetworkInfoId_whenDeleteNetworkInfoById_thenSuccess() throws InvalidFieldValidationException {
        this.networkInfoService.deleteNetworkInfoById(1L);
        verify(networkInfoRepository, times(1)).delete(1L);
    }

    private NetworkInfo newNetworkInfo(Long id, String ip, String hostname) {
        NetworkInfo ni = new NetworkInfo();
        if (id != null) {
            ni.setId(id);
        }
        if (ip != null) {
            ni.setIp(ip);
        }
        if (hostname != null) {
            ni.setHostname(hostname);
        }
        return ni;
    }
}
