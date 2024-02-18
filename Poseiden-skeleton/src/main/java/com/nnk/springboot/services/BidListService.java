package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BidListService {

    @Autowired
    BidListRepository bidListRepository;

    public List<BidList> findAll(){
        return bidListRepository.findAll();
    }

    public BidList save(BidList bid){
        return bidListRepository.save(bid);
    }

    public Optional<BidList> findById(int id){
        return bidListRepository.findById(id);
    }

    public void deleteById(int id){
        bidListRepository.deleteById(id);
    }

}
