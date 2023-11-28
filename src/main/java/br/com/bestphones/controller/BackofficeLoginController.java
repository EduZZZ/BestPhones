package br.com.bestphones.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.bestphones.dao.UsuarioDAO;
import br.com.bestphones.model.Usuario;

@Controller
public class BackofficeLoginController {

  @GetMapping("/Backoffice/Login")
  public ModelAndView mostrarTela() {
    Usuario u = new Usuario();
    ModelAndView mv = new ModelAndView("backoffice-login");
    mv.addObject("usuario", u);
    return mv;
  }

  @PostMapping("/Backoffice/Login")
  public ModelAndView login(
          @ModelAttribute(value = "usuario") Usuario u,
          HttpServletRequest request) {
    UsuarioDAO usuarioDao = new UsuarioDAO();
    ModelAndView mv;

    u = usuarioDao.getUsuario(u.getEmail(), u.getSenha());
    if (u != null ) {
      HttpSession sessao = request.getSession();
      sessao.setAttribute("user", u);
      mv = new ModelAndView("redirect:/Backoffice/Home");
      mv.addObject("u", u);
    } else {
      mv = new ModelAndView("backoffice-login");
      mv.addObject("usuario", new Usuario());
    }
    return mv;
  }

  @GetMapping("/Backoffice/Logout")
  public String logout(HttpServletRequest request) {
    HttpSession sessao = request.getSession();
    sessao.invalidate(); // Isso irá invalidar a sessão e limpar todos os dados de sessão
    return "redirect:/Backoffice/Login"; // Redireciona o usuário para a página de login
  }
}
