package com.studio.controller.api;

import com.studio.entity.SanPham;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("api/products")
public class SanPhamAPI {

  @Autowired
  SessionFactory factory;

  @RequestMapping
  public ResponseEntity<Object> getProducts(){

    Session session = factory.openSession();
    String hql = "FROM SanPham";
    Query<SanPham> query = session.createQuery(hql);

    return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
  }
  @RequestMapping("{id}")
  public ResponseEntity<Object> getProduct(@PathVariable("id") int id){

    Session session = factory.openSession();
    try {
      SanPham pv = new SanPham(id);
      session.refresh(pv);
      return new ResponseEntity<Object>(pv, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping("service={id}")
  public ResponseEntity<Object> getProductService(@PathVariable("id") int id){

    Session session = factory.openSession();
    String hql = "FROM SanPham WHERE dichVu.maDV = :id";
    try {
      Query<SanPham> query = session.createQuery(hql);
      query.setParameter("id", id);
      return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(name = "product", method = RequestMethod.POST)
  public ResponseEntity<Object> postProduct(@RequestBody List<SanPham> sp){
    Session session = factory.openSession();

    Transaction tran = session.beginTransaction();
    try {
      for (SanPham c : sp) {
        session.save(c);
      }
      tran.commit();
      return new ResponseEntity<Object>("Inserted products ", HttpStatus.CREATED);
    } catch (Exception e) {
      tran.rollback();
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(name = "product", method = RequestMethod.PUT)
  public ResponseEntity<Object> putProduct(@RequestBody SanPham sp){
    Session session = factory.openSession();

    Transaction tran = session.beginTransaction();
    try {
      SanPham s = new SanPham(sp.getMaSP());
      session.refresh(s);
      sp.setDichVu(s.getDichVu());
      session.save(sp);
      tran.commit();
      return new ResponseEntity<Object>("Updated products ", HttpStatus.CREATED);
    } catch (Exception e) {
      tran.rollback();
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }




}
