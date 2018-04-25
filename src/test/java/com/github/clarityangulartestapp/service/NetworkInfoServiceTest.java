package com.github.clarityangulartestapp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.clarityangulartestapp.error.NetworkInfoValidationException;
import com.github.clarityangulartestapp.model.NetworkInfo;
import com.github.clarityangulartestapp.repository.NetworkInfoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NetworkInfoServiceTest {

    private NetworkInfo niSuccess1;
    private NetworkInfo niSuccess2;
    private NetworkInfo niError;
    private NetworkInfo niCreate;
    private NetworkInfo niUpdate;
    private List<NetworkInfo> networkInfoList;

    @MockBean
    NetworkInfoRepository networkInfoRepository;

    @Autowired
    NetworkInfoService networkInfoService;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        niSuccess1 = newNetworkInfo(1L, "192.168.1.1", "https://google.com");
        niSuccess2 = newNetworkInfo(2L, "192.168.1.2", "https://yahoo.com");
        niError = newNetworkInfo(0L, null, null);
        niCreate = newNetworkInfo(null, "192.168.1.1", "https://google.com");
        niUpdate = newNetworkInfo(1L, "192.168.1.2", "https://yahoo.com");

        networkInfoList = new ArrayList<NetworkInfo>();
        networkInfoList.add(niSuccess1);
        networkInfoList.add(niSuccess2);

        Mockito.when(networkInfoRepository.findAll()).thenReturn(networkInfoList);
        Mockito.when(networkInfoRepository.findOne(1L)).thenReturn(niSuccess1);
        Mockito.when(networkInfoRepository.findOne(2L)).thenReturn(niSuccess2);
        Mockito.when(networkInfoRepository.findOne(3L)).thenReturn(niUpdate);
        Mockito.when(networkInfoRepository.findOne(null)).thenThrow(IllegalArgumentException.class);
        Mockito.when(networkInfoRepository.findNetworkInfoByIp("192.168.1.1")).thenReturn(Arrays.asList(niSuccess1));
        Mockito.when(networkInfoRepository.findNetworkInfoByIp("192.168.1.2")).thenReturn(Arrays.asList(niSuccess2));
        Mockito.when(networkInfoRepository.findNetworkInfoByHostname("https://google.com")).thenReturn(Arrays.asList(niSuccess1));
        Mockito.when(networkInfoRepository.findNetworkInfoByHostname("https://yahoo.com")).thenReturn(Arrays.asList(niSuccess2));
        Mockito.when(networkInfoRepository.save(niCreate)).thenReturn(niSuccess1);
        Mockito.when(networkInfoRepository.save(niUpdate)).thenReturn(niUpdate);
    }

    @Test
    public void whenFindAllNetworkInfo_thenNetworkInfoArrayFound() {
        List<NetworkInfo> returnedNetworkInfoList = this.networkInfoService.findAllNetworkInfo();
        assertEquals(networkInfoList, returnedNetworkInfoList);
        assertTrue(returnedNetworkInfoList.contains(niSuccess1));
        assertTrue(returnedNetworkInfoList.contains(niSuccess2));
        assertFalse(returnedNetworkInfoList.contains(niError));
    }

    @Test
    public void givenId_whenFindNetworkInfoById_thenNetworkInfoFound() {
        NetworkInfo findNetworkInfoById1 = this.networkInfoService.findNetworkInfoById(1L);
        NetworkInfo findNetworkInfoById2 = this.networkInfoService.findNetworkInfoById(2L);
        assertEquals(niSuccess1, findNetworkInfoById1);
        assertEquals(niSuccess2, findNetworkInfoById2);
    }

    @Test
    public void givenIdNull_whenFindNetworkInfoById_thenThrowException() {
        exception.expect(IllegalArgumentException.class);
        this.networkInfoService.findNetworkInfoById(null);
    }

    @Test
    public void givenIncorrectId_whenFindNetworkInfoById_thenReturnNull() {
        NetworkInfo findNetworkInfoByIncorrectId = this.networkInfoService.findNetworkInfoById(10L);
        assertNull(findNetworkInfoByIncorrectId);
    }

    @Test
    public void givenIp_whenFindNetworkInfoByIp_thenNetworkInfoArrayFound() {
        List<NetworkInfo> findNetworkInfoByIp1List = this.networkInfoService.findNetworkInfoByIp("192.168.1.1");
        List<NetworkInfo> findNetworkInfoByIp2List = this.networkInfoService.findNetworkInfoByIp("192.168.1.2");
        assertTrue(findNetworkInfoByIp1List.contains(niSuccess1));
        assertFalse(findNetworkInfoByIp1List.contains(niSuccess2));
        assertTrue(findNetworkInfoByIp2List.contains(niSuccess2));
        assertFalse(findNetworkInfoByIp2List.contains(niSuccess1));
    }

    @Test
    public void givenHostname_whenFindNetworkInfoByHostname_thenNetworkInfoArrayFound() {
        List<NetworkInfo> findNetworkInfoByHostname1List = this.networkInfoService.findNetworkInfoByHostname("https://google.com");
        List<NetworkInfo> findNetworkInfoByHostname2List = this.networkInfoService.findNetworkInfoByHostname("https://yahoo.com");
        assertTrue(findNetworkInfoByHostname1List.contains(niSuccess1));
        assertFalse(findNetworkInfoByHostname1List.contains(niSuccess2));
        assertTrue(findNetworkInfoByHostname2List.contains(niSuccess2));
        assertFalse(findNetworkInfoByHostname2List.contains(niSuccess1));
    }

    @Test
    public void givenNetworkInfo_whenCreateNetworkInfo_thenNetworkInfoWithId() {
        NetworkInfo createNetworkInfo = this.networkInfoService.createNetworkInfo(niCreate);
        assertEquals(niSuccess1, createNetworkInfo);
    }

    @Test
    public void givenSuccess1IdAndSuccess2NetworkInfo_whenUpdateNetworkInfoById_thenNiUpdateReturned() {
        NetworkInfo updateNetworkInfoById = this.networkInfoService.updateNetworkInfoById(niUpdate.getId(), niSuccess2);
        assertEquals(niUpdate.getId(), updateNetworkInfoById.getId());
        assertEquals(niUpdate.getIp(), updateNetworkInfoById.getIp());
        assertEquals(niUpdate.getHostname(), updateNetworkInfoById.getHostname());
    }

    @Test
    public void givenIncorrectIdAndSuccess2NetworkInfo_whenUpdateNetworkInfoById_thenNullReturned() {
        NetworkInfo updateNetworkInfoByIncorrectId = this.networkInfoService.updateNetworkInfoById(10L, niSuccess2);
        assertNull(updateNetworkInfoByIncorrectId);
    }

    @Test
    public void givenIncorrectId_whenDeleteNetworkInfoById_thenNetworkInfoValidationException() throws NetworkInfoValidationException {
        exception.expect(NetworkInfoValidationException.class);
        this.networkInfoService.deleteNetworkInfoById(10L);
    }

    @Test
    public void givenSucces1Id_whenDeleteNetworkInfoById_thenSuccess() throws NetworkInfoValidationException {
        this.networkInfoService.deleteNetworkInfoById(1L);
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
