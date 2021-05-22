package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseDao;


//==> 회원관리 DAO CRUD 구현
@Repository("purchaseDaoImpl")
public class PurchaseDaoImpl implements PurchaseDao{
	
	///Field
	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	///Constructor
	public PurchaseDaoImpl() {
		System.out.println(this.getClass());
	}

	public void addPurchase(Purchase purchase) throws Exception{
		sqlSession.insert("PurchaseMapper.addPurchase", purchase);
	}
	
	public Purchase getPurchase(int tranNo) throws Exception{
		return sqlSession.selectOne("PurchaseMapper.getPurchase", tranNo);
	}
	
	public Purchase getPurchase2(int prodNo) throws Exception{
		return sqlSession.selectOne("PurchaseMapper.getPurchase2", prodNo);
	}
	
	public List<Purchase> getPurchaseList(Search search, String buyerId) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("search", search);
		map.put("buyerId", buyerId);
		
		return sqlSession.selectList("PurchaseMapper.getPurchaseList", map);
	}
	
	public void updatePurchase(Purchase purchase) throws Exception {
		sqlSession.update("PurchaseMapper.updatePurchase", purchase);
	}
	
	public void updateTranCode(Purchase purchase) throws Exception {
		sqlSession.selectOne("PurchaseMapper.updateTranCodePurchase", purchase);
	}
	// 게시판 Page 처리를 위한 전체 Row(totalCount)  return
	public int getTotalCount(String buyerId) throws Exception {
		return sqlSession.selectOne("PurchaseMapper.getTotalCount", buyerId);
	}

}