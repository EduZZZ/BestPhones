package br.com.bestphones.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.bestphones.dao.ImagemProdutoDAO;
import br.com.bestphones.dao.PedidoResumidoDAO;
import br.com.bestphones.dao.ProdutoDAO;
import br.com.bestphones.model.Cliente;
import br.com.bestphones.model.ImagemProduto;
import br.com.bestphones.model.PedidoResumido;
import br.com.bestphones.model.Produto;
import br.com.bestphones.model.ProdutoCarrinho;

@Controller
public class PedidoController {


  private final PedidoResumidoDAO pedidoResumidoDao;

  public PedidoController(PedidoResumidoDAO pedidoResumidoDao) {
    this.pedidoResumidoDao = pedidoResumidoDao;
  }


  @GetMapping("/Meus-pedidos")
  public ModelAndView mostrarTela(HttpServletRequest request) {

    HttpSession sessao = request.getSession();

    Cliente c = (Cliente) sessao.getAttribute("cliente");


    ModelAndView mv = new ModelAndView("meus-pedidos");
    PedidoResumidoDAO pedidoResumidoDao = new PedidoResumidoDAO();

    List<PedidoResumido> pedidos = pedidoResumidoDao.getPedidos(c.getId());
    mv.addObject("pedidos", pedidos);
    return mv;
  }

  @GetMapping("/Backoffice/Pedidos")
  public ModelAndView listarPedidos() {
    List<PedidoResumido> pedidos = pedidoResumidoDao.todosPedidos();
    ModelAndView mv = new ModelAndView("backoffice-pedidos");
    mv.addObject("pedidos", pedidos);
    return mv;
  }


  @GetMapping("/Backoffice/Pedidos/Detalhes/{id}")
  public ModelAndView detalhesPedido(@PathVariable int id) {
    PedidoResumido pedido = pedidoResumidoDao.getPedidoPorId(id);
    ModelAndView mv = new ModelAndView("detalhes-pedido");
    mv.addObject("pedido", pedido);
    return mv;
  }

  @PostMapping("/Backoffice/Pedidos/AtualizarStatus")
  public String atualizarStatusPedido(@RequestParam("pedidoId") int pedidoId) {
    int statusAtual = pedidoResumidoDao.getStatusAtual(pedidoId);
    int proximoStatusId = pedidoResumidoDao.proximoStatus(statusAtual);
    pedidoResumidoDao.atualizarStatusPedido(pedidoId, proximoStatusId);
    return "redirect:/Backoffice/Pedidos";
  }


  @GetMapping("/Meus-pedidos/{id}")
  public ModelAndView exibirDetalhesPedido(@PathVariable("id") int id, HttpServletRequest request) {

    PedidoResumido pedido = pedidoResumidoDao.getPedidoPorId(id);
    ModelAndView mv = new ModelAndView("detalhes-pedido");
    mv.addObject("pedido", pedido);
    return mv;
  }
}