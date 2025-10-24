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

    public void deletarCerveja(Long id){
        cervejaRepository.deleteById(id);
    }

    public Cerveja buscarPorId(Long id){
        return cervejaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public void atualizarCerveja (Cerveja cerveja){
        cervejaRepository.save(cerveja);
    }

}
