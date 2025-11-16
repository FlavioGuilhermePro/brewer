package com.example.brewer.service;

import com.example.brewer.model.Cerveja;
import com.example.brewer.repository.CervejaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // ===== MÉTODO MODIFICADO =====
    // Agora pode deletar cervejas sem problemas!
    @Transactional
    public void deletarCerveja(Long id){
        Cerveja cerveja = buscarPorId(id);

        // Opcional: Adicionar validação de segurança
        if (cerveja.getQuantidadeEstoque() > 0) {
            // Aviso: você está deletando um produto que ainda tem estoque
            System.out.println("⚠️ AVISO: Deletando cerveja com " +
                    cerveja.getQuantidadeEstoque() + " unidades em estoque");
        }

        // Agora pode deletar sem problemas!
        // Os itens de pedidos antigos continuarão com os dados históricos
        cervejaRepository.deleteById(id);
    }

    public Cerveja buscarPorId(Long id){
        return cervejaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public void atualizarCerveja(Cerveja cerveja){
        cervejaRepository.save(cerveja);
    }

    public Page<Cerveja> buscarComFiltro(String nomeBusca, Pageable pageable) {
        if (nomeBusca != null && !nomeBusca.isEmpty()) {
            return cervejaRepository.findByNomeContainingIgnoreCase(nomeBusca, pageable);
        } else {
            return cervejaRepository.findAll(pageable);
        }
    }
}