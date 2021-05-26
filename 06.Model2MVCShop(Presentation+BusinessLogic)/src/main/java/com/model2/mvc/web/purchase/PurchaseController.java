package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;

@Controller
public class PurchaseController {
	
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	public PurchaseController() {
		// TODO Auto-generated constructor stub
		System.out.println(this.getClass());
	}
	
	@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	@RequestMapping("/addPurchaseView.do")
	public String addPurchase( @RequestParam("prodNo") int prodNo, Model model) throws Exception{
		
		System.out.println("/addPurchaseView.do");
		
		Product product = productService.getProduct(prodNo);
		
		model.addAttribute("product", product);
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
	
	@RequestMapping("/addPurchase.do")
	public String addPurchase( @ModelAttribute("purchase") Purchase purchase,
								@ModelAttribute("product") Product product,
								@ModelAttribute("user") User user,
									Model model) throws Exception{
		
		System.out.println("/addPurchase.do");
		
		purchase.setTranCode("1");
		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);
		
		purchaseService.addPurchase(purchase);
		
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/addPurchase.jsp";
	}
	
	@RequestMapping("/getPurchase.do")
	public String getPurchase( @RequestParam("tranNo") int tranNo,
								Model model) throws Exception{
		
		System.out.println("/getPurchase.do");
		
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/getPurchase.jsp";
	}
	
	@RequestMapping("/listPurchase.do")
	public String listPurchase( @ModelAttribute("search") Search search, Model model,
								 HttpSession session ) throws Exception {
		
		System.out.println("/listPurchase.do");
		
		if(search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		User user = (User)session.getAttribute("user");
		
		Map<String, Object> map = purchaseService.getPurchaseList(search, user.getUserId());
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/purchase/listPurchase.jsp";
	}
	
	@RequestMapping("/updatePurchaseView.do")
	public String updatePurchaseView( @RequestParam("tranNo") int tranNo, Model model) throws Exception{
		
		System.out.println("/updatePurchaseView.do");
		
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/updatePurchase.jsp";
	}
	
	@RequestMapping("/updatePurchase.do")
	public String updatePurchase( @ModelAttribute ("purchase") Purchase purchase) throws Exception{
		
		System.out.println("/updatePurchase.do");
		
		purchaseService.updatePurchase(purchase);
		
		
		return "redirect:/getPurchase.do?tranNo="+purchase.getTranNo();
	}
	
	@RequestMapping("/updateTranCode.do")
	public String updateTranCode( @RequestParam("tranNo") int tranNo,
									@RequestParam("tranCode") String tranCode,
									@RequestParam("page") int page) throws Exception {
		
		System.out.println("/updateTranCode.do");
		
		Purchase purchase = purchaseService.getPurchase(tranNo);

		purchase.setTranCode(tranCode);
		
		purchaseService.updateTranCode(purchase);
		
		return "redirect:/listPurchase.do?menu=manage&page="+page;
	}
	
	@RequestMapping("updateTranCodeByProd.do")
	public String updateTranCodeByProd( @RequestParam("prodNo") int prodNo,
											@RequestParam("tranCode") String tranCode,
											@RequestParam("page") int page) throws Exception {
		
		System.out.println("/updateTranCodeByProd.do");
		
		Purchase purchase = purchaseService.getPurchase2(prodNo);
		
		purchase.setTranCode(tranCode);
		
		purchaseService.updateTranCode(purchase);
		
		return "redirect:/listProduct.do?menu=manage&page="+page;
		
	}

}
