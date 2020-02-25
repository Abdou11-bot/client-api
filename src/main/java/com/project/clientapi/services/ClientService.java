package com.project.clientapi.services;

import com.project.clientapi.repositories.client.Client;
import com.project.clientapi.repositories.client.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ImageService imageService;
    public ClientService(ClientRepository clientRepository, ImageService imageService) {
        this.clientRepository = clientRepository;
//        this.initDB();
        this.imageService = imageService;
    }

    private void initDB() {
        Client client = new Client();
        client.setMail("Abdou@gmail.com");
        client.setCIN("CIN1");
        client.setFirstName("Laouali");
        client.setLastName("Mahaboubou");
        this.addClient(client);
    }

    public Client getClient(Long id){
        return this.clientRepository.findById(id).orElseThrow();
    }

    public List<Client> getAllClients(){
        return this.clientRepository.findAll();
    }

    public Client addClient(Client client) {
        return this.clientRepository.save(client);
    }

    public void deleteClient(Long id){
        this.imageService.deleteImages(id);
        this.clientRepository.deleteById(id);
    }
}
