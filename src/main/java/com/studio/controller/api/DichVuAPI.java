package com.studio.controller.api;

import com.studio.entity.DichVu;
import com.studio.entity.TaiKhoan;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RestController
@RequestMapping("api/services")
public class DichVuAPI {

  @Autowired
  SessionFactory factory;

  @RequestMapping
  public ResponseEntity<List<DichVu>> getServices (){

    Session session = factory.openSession();
    String hql = "FROM DichVu";

    @SuppressWarnings("unchecked")
    Query<DichVu> query = session.createQuery(hql);

    return new ResponseEntity<List<DichVu>>(query.list(), HttpStatus.OK);
  }
  @RequestMapping("{id}")
  public ResponseEntity<Object> getService(@PathVariable("id") int id){
    Session session = factory.openSession();

    try {
      DichVu dv = new DichVu(id);
      session.refresh(dv);
      return new ResponseEntity<Object>(dv, HttpStatus.CREATED);
    }catch (Exception e){
      session.close();
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  public ResponseEntity<Object> deleteService(@PathVariable("id") int id){
    Session session = factory.openSession();
    Transaction tran = session.beginTransaction();

    try {
      DichVu dv = new DichVu(id);
      session.refresh(dv);
      session.delete(dv);
      tran.commit();
      return new ResponseEntity<Object>(dv, HttpStatus.CREATED);
    }catch (Exception e){
      tran.rollback();
      session.close();
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "service", method = RequestMethod.POST)
  public ResponseEntity<Object> postService(@Valid @RequestBody DichVu dv){
    Session session = factory.openSession();
    Transaction tran = session.beginTransaction();
    try {
      session.save(dv);

      tran.commit();
      session.close();
      return new ResponseEntity<Object>("Inserted service", HttpStatus.CREATED);

    }catch (Exception e){
      tran.rollback();
      session.close();
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
  @RequestMapping(value = "service", method = RequestMethod.PUT)
  public ResponseEntity<Object> putService(@Valid @RequestBody DichVu dv){
    Session session = factory.openSession();
    Transaction tran = session.beginTransaction();
    try {
      session.save(dv);
      tran.commit();
      session.close();
      return new ResponseEntity<Object>("Updated service", HttpStatus.CREATED);
    }catch (Exception e){
      tran.rollback();
      session.close();
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
