package com.project.clientapi.rest;

import com.project.clientapi.repositories.client.Client;
import com.project.clientapi.services.ClientService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> getAllClients(){
        return this.clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public Client getClient(@PathVariable Long id) {
        return this.clientService.getClient(id);
    }

    @PostMapping("/add")
    public Client addClient(@Valid @RequestBody Client client)
    {
        return this.clientService.addClient(client);
    }

    @PutMapping("/edit/{id}")
    public Client Client(@PathVariable("id") Long id, @Valid @RequestBody Client updateClient)
    {
        Client ancientClient =this.clientService.getClient(id);
        if(!ancientClient.equals(null)){
            updateClient.setId(id);
            this.clientService.addClient(updateClient);
            return updateClient;
        }
        return updateClient;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteClient(@PathVariable Long id)
    {

        Client client =this.clientService.getClient(id);
        if(!client.equals(null)){
            client.setId(id);
            this.clientService.deleteClient(id);
            return "Client deleted";
        }
        return "delete Failed";
    }

}

