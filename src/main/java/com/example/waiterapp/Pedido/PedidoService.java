package com.example.waiterapp.Pedido;

import com.example.waiterapp.Cliente.ClienteService;
import com.example.waiterapp.Item.ItemService;
import com.example.waiterapp.ItemPedido.ItemPedido;
import com.example.waiterapp.ItemPedido.ItemPedidoRepository;
import com.example.waiterapp.enums.Estado;
import com.example.waiterapp.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    
    private PedidoRepository pedidoRepository;
    private ItemPedidoRepository itemPedidoRepository;
    private ItemService itemService;
    private ClienteService clienteService;

    @Autowired
    public PedidoService(
            PedidoRepository pedidoRepository,
            ItemPedidoRepository itemPedidoRepository,
            ItemService itemService,
            ClienteService clienteService
    ) {
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.itemService = itemService;
        this.clienteService = clienteService;
    }

    public Pedido transformarDTO(PedidoDTO pedidoDTO){
        Pedido pedido = new Pedido(
                pedidoDTO.getId(),
                pedidoDTO.getDataCriacao(),
                pedidoDTO.getEstado(),
                pedidoDTO.getPrecoTotal(),
                pedidoDTO.getNotaAtendimento(),
                pedidoDTO.getNotaPedido(),
                pedidoDTO.getOpcoesExtras()
        );
        pedido.setItems(pedidoDTO.getItems());
        pedido.setCliente(pedidoDTO.getCliente());
        pedido.setGarcom(pedidoDTO.getGarcom());
        pedido.setPagamento(pedidoDTO.getPagamento());
        return pedido;
    }

    @Transactional
    public List<Pedido> listaPedidosByIdCliente(Long idCliente){
        return pedidoRepository.findallByIdCliente(idCliente);
    }

    public List<Pedido> listaPedidos(){
        return pedidoRepository.findAll();
    }

    public Pedido retornaPedidoById(Long idPedido){
        Optional<Pedido> pedido = pedidoRepository.findById(idPedido);
        return pedido.orElse(null);
    }

    @Transactional
    public Pedido inserePedido(Pedido pedido){
        pedido.setId(null);
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setEstado(Estado.EM_PREPARACAO);
        pedido.setCliente(clienteService.retornaClienteById(pedido.getCliente().getId()));
        pedido = pedidoRepository.save(pedido);

        for (ItemPedido ip : pedido.getItems()) {
            ip.setItem(itemService.retornaItemById(ip.getItem().getId()));
            ip.setPreco(ip.getItem().getPreco());
            ip.setPedido(pedido);
        }
        pedido.setPrecoTotal();
        itemPedidoRepository.saveAll(pedido.getItems());
        pedido = pedidoRepository.save(pedido);

        return pedido;
    }

    public Pedido atualizaPedido(Pedido pedido){
        retornaPedidoById(pedido.getId());
        return pedidoRepository.save(pedido);
    }

    public void apagaPedido(Long idPedido) throws DataIntegrityViolationException{
        retornaPedidoById(idPedido);
        try{
            pedidoRepository.deleteById(idPedido);
        }catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(("Não é possível excluir esse pedido"));
        }
    }
}
