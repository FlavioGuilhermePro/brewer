package com.example.brewer.service;

import com.example.brewer.model.Cliente;
import com.example.brewer.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public void salvarCliente(Cliente cliente){
        clienteRepository.save(cliente);
    }

    public List<Cliente> listarClientes(){
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id){
        return clienteRepository.findById(id).orElse(null);
    }

    public void deletarCliente(Long id){
        clienteRepository.deleteById(id);
    }



}
