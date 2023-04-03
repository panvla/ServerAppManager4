package com.vladimirpandurov.serverAppManagerB.controller;

import com.vladimirpandurov.serverAppManagerB.domain.Response;
import com.vladimirpandurov.serverAppManagerB.domain.Server;
import com.vladimirpandurov.serverAppManagerB.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/servers")
@RequiredArgsConstructor
@CrossOrigin
public class ServerController {

    private final ServerService serverService;

    @GetMapping
    public ResponseEntity<Response> getServers() {
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(LocalDateTime.now())
                .message("Servers retrieved")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .data(Map.of("servers", this.serverService.list(30)))
                .build()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<Response> getServer(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Server retrieved")
                .data(Map.of("server", this.serverService.get(id)))
                .build()
        );
    }
    @GetMapping("/ping/{idAddress}")
    public ResponseEntity<Response> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException {
        Server server = this.serverService.ping(ipAddress);
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(LocalDateTime.now())
                .message("Server created")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .data(Map.of("server", server))
                .build()
        );
    }
    @PostMapping
    public ResponseEntity<Response> saveServer(@RequestBody Server server){
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(LocalDateTime.now())
                .data(Map.of("server", this.serverService.create(server)))
                .message("Server created")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .build()
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteServer(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(LocalDateTime.now())
                .message("Server deleted")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .data(Map.of("deleted", this.serverService.delete(id)))
                .build()
        );
    }
    @GetMapping(path="/image/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getServerImage(@PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/Documents/panvlaGit/resources/png/" + fileName));
    }

}
