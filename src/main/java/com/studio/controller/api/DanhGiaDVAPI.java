package com.studio.controller.api;

import com.studio.entity.DanhGiaDV;
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
@RequestMapping("api/rate-service")
public class DanhGiaDVAPI {
  @Autowired
  SessionFactory factory;

  @RequestMapping
  public ResponseEntity<List<DanhGiaDV>> getRate(){

    Session session = factory.openSession();

    String hql = "FROM DanhGiaDV";
    @SuppressWarnings("unchecked")
    Query<DanhGiaDV> query = session.createQuery(hql);

    return new ResponseEntity<List<DanhGiaDV>>(query.list(), HttpStatus.OK);
  }

  @RequestMapping("service={id}")
  public ResponseEntity<List<DanhGiaDV>> getEmployeeRate(@PathVariable("id") int id){

    Session session = factory.openSession();
    String hql = "FROM DanhGiaDV WHERE phucVu.chiTiet.bangGia.dichVu.maDV = :id";

    @SuppressWarnings("unchecked")
    Query<DanhGiaDV> query = session.createQuery(hql);
    query.setParameter("id", id);

    return new ResponseEntity<List<DanhGiaDV>>(query.list(), HttpStatus.OK);
  }

  @RequestMapping("customer={id}")
  public ResponseEntity<List<DanhGiaDV>> getCustomerRate(@PathVariable("id") int id){

    Session session = factory.openSession();
    String hql = "FROM DanhGiaNV WHERE phucVu.chiTiet.donHang.maKH = :id";

    @SuppressWarnings("unchecked")
    Query<DanhGiaDV> query = session.createQuery(hql);
    query.setParameter("id", id);

    return new ResponseEntity<List<DanhGiaDV>>(query.list(), HttpStatus.OK);
  }

  @RequestMapping(value = "rate", method = RequestMethod.POST)
  public ResponseEntity<Object> postRate(@Valid @RequestBody DanhGiaDV dg){
    Session session = factory.openSession();

    Transaction tran = session.beginTransaction();
    try {
      session.save(dg);
      tran.commit();
      return new ResponseEntity<Object>("Inserted Rate Service ", HttpStatus.CREATED);
    } catch (Exception e) {
      tran.rollback();
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
