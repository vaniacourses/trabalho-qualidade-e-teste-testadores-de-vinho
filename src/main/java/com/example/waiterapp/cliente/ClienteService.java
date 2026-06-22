package com.example.waiterapp.cliente;

import com.example.waiterapp.pedido.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class ClienteService {
    
    private ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente transformarDTO(ClienteDTO clienteDTO){
        Cliente cliente = new Cliente(
                clienteDTO.getId(),
                clienteDTO.getNome(),
                clienteDTO.getEmail(),
                clienteDTO.getCpf(),
                clienteDTO.getDataCriacao()
        );
        cliente.setPedidos(clienteDTO.getPedidos());
        return cliente;
    }

    public List<Cliente> listaClientes() {
        return clienteRepository.findAll();
    }

    public Cliente retornaClienteById(Long idCliente) {
        return clienteRepository.findById(idCliente).orElse(null);
    }

    public List<Pedido> retornaPedidosCliente(Long idCliente){
        return retornaClienteById(idCliente).getPedidos();
    }

    public void inserePedidosCliente(Long idCliente, List<Pedido> pedidos){
        Cliente cliente = retornaClienteById(idCliente);
        
        cliente.setPedidos(pedidos);
    }

    public Cliente insereCliente(Cliente cliente) {
        cliente.setId(null);
        cliente.setDataCriacao(LocalDateTime.now(ZoneId.of("UTC")));
        return clienteRepository.save(cliente);
    }

    public Cliente atualizaCliente(Cliente cliente){
        retornaClienteById(cliente.getId());
        return clienteRepository.save(cliente);
    }
    
    public void apagaCliente(long idCliente) throws DataIntegrityViolationException{
        try{
            clienteRepository.deleteById(idCliente);
        }catch(DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Não é possível excluir esse cliente");
        }
    }

    public Cliente retornaClienteByCpf(String cpf) {
        return clienteRepository.findByCpf(cpf).orElse(null);
    }
}
