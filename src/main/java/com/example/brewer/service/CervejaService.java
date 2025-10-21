package com.example.brewer.service;

import com.example.brewer.model.Cerveja;
import com.example.brewer.repository.CervejaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CervejaService {
    private final CervejaRepository cervejaRepository;

    public void adicionarCerveja(Cerveja cerveja){
         cervejaRepository.save(cerveja);
    }

    public List<Cerveja> listarCervejas(){
        return cervejaRepository.findAll();
    }

}
