package com.studio.controller.api;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.studio.entity.DanhGiaNV;

import javax.validation.Valid;

@Controller
@RestController
@RequestMapping("api/rate-employee")
public class DanhGiaNVAPI {
	
	@Autowired
	SessionFactory factory;
	
	@RequestMapping
	public ResponseEntity<List<DanhGiaNV>> getRate(){
		
		Session session = factory.openSession();

		String hql = "FROM DanhGiaNV";
		@SuppressWarnings("unchecked")
		Query<DanhGiaNV> query = session.createQuery(hql);
		
		return new ResponseEntity<List<DanhGiaNV>>(query.list(), HttpStatus.OK);
	}
	
	@RequestMapping("employee={id}")
	public ResponseEntity<List<DanhGiaNV>> getEmployeeRate(@PathVariable("id") int id){
		
		Session session = factory.openSession();
		String hql = "FROM DanhGiaNV WHERE phucVu.maNV = :id";
		
		@SuppressWarnings("unchecked")
		Query<DanhGiaNV> query = session.createQuery(hql);
		query.setParameter("id", id);
		
		return new ResponseEntity<List<DanhGiaNV>>(query.list(), HttpStatus.OK);
	}
	
	@RequestMapping("customer={id}")
	public ResponseEntity<List<DanhGiaNV>> getCustomerRate(@PathVariable("id") int id){
		
		Session session = factory.openSession();
		String hql = "FROM DanhGiaNV WHERE phucVu.chiTiet.donHang.maKH = :id";
		
		@SuppressWarnings("unchecked")
		Query<DanhGiaNV> query = session.createQuery(hql);
		query.setParameter("id", id);
		
		return new ResponseEntity<List<DanhGiaNV>>(query.list(), HttpStatus.OK);
	}

	@RequestMapping(value = "rate", method = RequestMethod.POST)
	public ResponseEntity<Object> postRate(@Valid @RequestBody DanhGiaNV dg){
		Session session = factory.openSession();

		Transaction tran = session.beginTransaction();
		try {
			session.save(dg);
			tran.commit();
			return new ResponseEntity<Object>("Inserted Rate Employee ", HttpStatus.CREATED);
		} catch (Exception e) {
			tran.rollback();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	
	

}
