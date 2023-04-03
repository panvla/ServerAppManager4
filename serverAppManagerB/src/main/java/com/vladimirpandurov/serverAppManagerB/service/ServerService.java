package com.vladimirpandurov.serverAppManagerB.service;

import com.vladimirpandurov.serverAppManagerB.domain.Server;
import com.vladimirpandurov.serverAppManagerB.enumeration.Status;
import com.vladimirpandurov.serverAppManagerB.repository.ServerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServerService {

    private final ServerRepository serverRepository;

    public Server create(Server server){
        server.setImageUrl(setServerImageUrl());
        return this.serverRepository.save(server);
    }

    public Server ping(String ipAddress) throws IOException {
        Server server = this.serverRepository.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(1000)? Status.SERVER_UP : Status.SERVER_DOWN);
        this.serverRepository.save(server);
        return server;
    }

    public Collection<Server> list(int limit){
        return this.serverRepository.findAll(PageRequest.of(0, limit)).toList();
    }

    public Server get(Long id){
        return this.serverRepository.findById(id).get();
    }

    public Server update(Server server){
        return this.serverRepository.save(server);
    }

    public Boolean delete(Long id){
        this.serverRepository.deleteById(id);
        return Boolean.TRUE;
    }

    private String setServerImageUrl(){
        String[] imageNames = {"server1.png", "server2.png", "server3.png", "server4.png"};
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/Documents/panvlaGit/resources/png/" + imageNames[new Random().nextInt(4)]).toUriString();
    }
}
