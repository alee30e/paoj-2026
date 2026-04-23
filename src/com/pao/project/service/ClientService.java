package com.pao.project.service;

import com.pao.project.exception.UserNotFoundException;
import com.pao.project.model.BusinessClient;
import com.pao.project.model.Client;
import com.pao.project.model.IndividualClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientService {
    private List<Client> clients;
    private Map<String, Client> clientsById;
    private static ClientService instance;
    private ClientService(){
        clients = new ArrayList<>();
        clientsById = new HashMap<>();
    }
    public static ClientService getInstance(){
        if (instance == null)
            instance = new ClientService();
        return instance;
    }
    public IndividualClient addIndividualClient(String id, String address, String email, String phone, String firstName, String lastName,
                                                String cnp, String occupation, String dateOfBirth, Double monthlyIncome){
        IndividualClient client = new IndividualClient(id, address, email, phone,
                firstName, lastName, cnp, occupation, dateOfBirth, monthlyIncome);

        clients.add(client);
        clientsById.put(id, client);

        return client;
    }

    public BusinessClient addBusinessClient(String id, String address, String email, String phone, String companyName, String cui,
                                            String contactPerson, Double monthlyRevenue, Double monthlyExpenses){
        BusinessClient client = new BusinessClient(id, address, email, phone,
                companyName, cui, contactPerson, monthlyRevenue, monthlyExpenses);

        clients.add(client);
        clientsById.put(id, client);

        return client;
    }

    public Client findById(String id){
        return clientsById.get(id);
    }

    public List<Client> getAllClients(){
        return new ArrayList<>(clients);
    }

    public void deleteClient(String id){
        Client client = clientsById.get(id);
        if (client != null){
            clients.remove(client);
            clientsById.remove(id);
        }
    }
    public Client findByIdentificationNumber(String identificationNumber){
        for (Client client : clients){
            if (client.getIdentificationNumber().equalsIgnoreCase(identificationNumber)){
                return client;
            }
        }
        return null;
//        throw new UserNotFoundException("Clientul cu numarul de identificare nu exista ");
    }
}