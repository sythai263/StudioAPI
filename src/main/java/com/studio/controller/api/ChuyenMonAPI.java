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

import com.studio.entity.ChuyenMon;

@Controller
@RestController
@RequestMapping("api/specializes")
public class ChuyenMonAPI {
	
	@Autowired
	SessionFactory factory;
	
	@RequestMapping
	public ResponseEntity<List<ChuyenMon>> getSpecializes(){
		
		Session session = factory.openSession();
		String hql = "FROM ChuyenMon";
		
		@SuppressWarnings("unchecked")
		Query<ChuyenMon> query = session.createQuery(hql);
		
		return new ResponseEntity<List<ChuyenMon>>(query.list(), HttpStatus.OK);
	}
	
	@RequestMapping("{id}")
	public ResponseEntity<Object> getSpecialize(@PathVariable("id") int id){
		
		Session session = factory.openSession();
		
		ChuyenMon cm = new ChuyenMon(id);
		session.refresh(cm);
		return new ResponseEntity<Object>(cm, HttpStatus.OK);
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteSpecialize(@PathVariable("id") int id){
		Session session = factory.openSession();
		Transaction tran = session.beginTransaction();
		try {
			ChuyenMon cm = new ChuyenMon(id);
			session.refresh(cm);
			session.delete(cm);
			tran.commit();
		} catch (Exception e) {
			tran.rollback();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Object>(true,HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value = "specialize", method = RequestMethod.POST)
	public ResponseEntity<Object> postSpecialize(@Valid @RequestBody ChuyenMon cm, BindingResult errors){
		if (errors.hasErrors()) {
			return new ResponseEntity<Object>(errors.getAllErrors(), HttpStatus.OK);
		}
		Session session = factory.openSession();
		Transaction tran = session.beginTransaction();
		try {
			session.save(cm);
			tran.commit();
		} catch (Exception e) {
			tran.rollback();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.OK);
		}
		return new ResponseEntity<Object>(true,HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "specialize", method = RequestMethod.PUT)
	public ResponseEntity<Object> putSpecialize(@Valid @RequestBody ChuyenMon cm, BindingResult errors){
		if (errors.hasErrors()) {
			return new ResponseEntity<Object>(errors.getAllErrors(), HttpStatus.OK);
		}
		Session session = factory.openSession();
		Transaction tran = session.beginTransaction();
		try {
			session.update(cm);
			tran.commit();
		} catch (Exception e) {
			tran.rollback();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.OK);
		}
		return new ResponseEntity<Object>(true,HttpStatus.CREATED);
	}

}
