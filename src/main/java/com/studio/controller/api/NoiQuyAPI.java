package com.studio.controller.api;

import com.studio.entity.NoiQuy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("api/rules")
public class NoiQuyAPI {

  @Autowired
  SessionFactory factory;

  @RequestMapping
  public ResponseEntity<Object> getRules(){

    Session session = factory.openSession();
    String hql = "FROM NoiQuy";

    Query<NoiQuy> query = session.createQuery(hql);
    return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
  }

  @RequestMapping("{id}")
  public ResponseEntity<Object> getRule(@PathVariable("id") int id){

    Session session = factory.openSession();
    try{
      NoiQuy nq = new NoiQuy(id);
      session.refresh(nq);
      return new ResponseEntity<Object>(nq, HttpStatus.OK);
    }catch (Exception e){
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
  @RequestMapping(value = "rule", method = RequestMethod.POST)
  public ResponseEntity<Object> postRule(@RequestBody NoiQuy nq){
    Session session = factory.openSession();
    Transaction tran = session.beginTransaction();
    try{
      session.save(nq);
      tran.commit();
      return new ResponseEntity<Object>("Inserted rule ", HttpStatus.CREATED);
    }catch (Exception e){
      tran.rollback();
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "rule", method = RequestMethod.PUT)
  public ResponseEntity<Object> putRule(@RequestBody NoiQuy nq){
    Session session = factory.openSession();
    Transaction tran = session.beginTransaction();
    try{
      session.update(nq);
      tran.commit();
      return new ResponseEntity<Object>("Updated rule ", HttpStatus.CREATED);
    }catch (Exception e){
      tran.rollback();
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
