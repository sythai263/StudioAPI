package com.studio.controller.api;

import java.util.List;

import javax.validation.Valid;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.studio.entity.BangGia;

@Controller
@RestController
@RequestMapping("api/prices")
public class BangGiaAPI {

	@Autowired
	SessionFactory factory;

	@RequestMapping()
	public ResponseEntity<List<BangGia>> getPrices() {

		Session session = factory.openSession();
		String hql = "FROM BangGia";
		@SuppressWarnings("unchecked")
		Query<BangGia> query = session.createQuery(hql);

		return new ResponseEntity<List<BangGia>>(query.list(), HttpStatus.OK);
	}
	@RequestMapping("{id}")
	public ResponseEntity<Object> getPrice(@PathVariable("id") int id) {
		Session session = factory.openSession();
		BangGia bg = new BangGia(id);
		try {
			session.refresh(bg);
		} catch (Exception e) {
			return new ResponseEntity<Object>("Not found", HttpStatus.OK);
		}

		return new ResponseEntity<Object>(bg, HttpStatus.OK);

	}
	

	@RequestMapping("service={id}")
	public ResponseEntity<List<BangGia>> getPriceService(@PathVariable("id") int id) {
		Session session = factory.openSession();
		String hql = "FROM BangGia WHERE dichVu.maDV=:madv";
		@SuppressWarnings("unchecked")
		Query<BangGia> query = session.createQuery(hql);
		query.setParameter("madv", id);

		return new ResponseEntity<List<BangGia>>(query.list(), HttpStatus.OK);

	}
	
	@RequestMapping(value =  "price", method = RequestMethod.POST)
	public ResponseEntity<Object> postPrice(@Valid @RequestBody BangGia bg, 
			BindingResult errors){
		
		Session session = factory.openSession();
		if(bg.getsLNV() <1) {
			errors.rejectValue("amount", "price"," Amount employee must be >= 1");
		}
		if(bg.getGia() < 0 && bg.getGia() % 1000 != 0) {
			errors.rejectValue("price", "price"," Price must be >= 0  and Price mod 1000 = 0");
		}
		Transaction tran = session.beginTransaction();
		if (errors.hasErrors()){
			return new ResponseEntity<Object>(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
		}else {
			try {
				session.save(bg);
				tran.commit();
				return new ResponseEntity<Object>("Inserted with id"+ bg.getMaGia(), HttpStatus.CREATED);

			} catch (Exception e) {
				tran.rollback();
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		}
	}

	

}
