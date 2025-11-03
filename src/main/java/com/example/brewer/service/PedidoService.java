package com.example.brewer.service;

import com.example.brewer.repository.ClienteRepository;
import com.example.brewer.repository.ItemPedidoRepository;
import com.example.brewer.repository.PedidoRepository;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {
    private PedidoRepository pedidoRepository;
    private ItemPedidoRepository itemPedidoRepository;
    private ClienteRepository clienteRepository;
    private CervejaService cervejaService;
}
