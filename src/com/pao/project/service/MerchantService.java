package com.pao.project.service;

import com.pao.project.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MerchantService {
    private static MerchantService instance;

    private List<Merchant> merchants;
    private Map<String, Merchant> merchantsById;

    private MerchantService() {
        merchants = new ArrayList<>();
        merchantsById = new HashMap<>();
    }

    public static MerchantService getInstance() {
        if (instance == null) {
            instance = new MerchantService();
        }
        return instance;
    }

//    public Merchant addMerchant(String name, MerchantCategory category,
//                                BusinessClient businessClient,
//                                Account settlementAccount) {
//        Merchant merchant = new Merchant(name, category, businessClient, settlementAccount);
//
//        merchants.add(merchant);
//        merchantsById.put(merchant.getId(), merchant);
//
//        return merchant;
//    }

    public Merchant addMerchant(String name, MerchantCategory category,
                                BusinessClient businessClient,
                                Account settlementAccount) {

        if (!settlementAccount.getOwner().equals(businessClient)) {
            throw new IllegalArgumentException("Contul de decontare nu apartine clientului business.");
        }

        Merchant merchant = new Merchant(name, category, businessClient, settlementAccount);

        merchants.add(merchant);
        merchantsById.put(merchant.getId(), merchant);

        return merchant;
    }

    public Merchant findById(String id) {
        Merchant merchant = merchantsById.get(id);

        if (merchant == null) {
            throw new RuntimeException("Merchantul cu id-ul " + id + " nu exista");
        }

        return merchant;
    }

    public List<Merchant> getAllMerchants() {
        return new ArrayList<>(merchants);
    }

    public void deleteMerchant(String id) {
        Merchant merchant = findById(id);

        merchants.remove(merchant);
        merchantsById.remove(id);
    }

    public void deactivateMerchant(String id) {
        Merchant merchant = findById(id);
        merchant.deactivate();
    }

    public void activateMerchant(String id) {
        Merchant merchant = findById(id);
        merchant.activate();
    }
}