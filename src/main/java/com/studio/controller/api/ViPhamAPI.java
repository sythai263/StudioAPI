package com.studio.controller.api;

import com.studio.entity.SanPham;
import com.studio.entity.ViPham;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RestController
@RequestMapping("api/fouls")
public class ViPhamAPI {

  @Autowired
  SessionFactory factory;

  @RequestMapping
  public ResponseEntity<Object> getFouls(){

    Session session = factory.openSession();
    String hql = "FROM ViPham";
    Query<ViPham> query = session.createQuery(hql);

    return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
  }

  @RequestMapping("{id}")
  public ResponseEntity<Object> getFoul( @PathVariable("id") int id){
    Session session = factory.openSession();
    String hql = "FROM ViPham WHERE maVP= :id";
    try {

      Query<ViPham> query = session.createQuery(hql);


      return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
    }catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping("employee={id}")
  public ResponseEntity<Object> getFoulEmployee( @PathVariable("id") int id){
    Session session = factory.openSession();
    String hql = "FROM ViPham WHERE nhanVien.maNV= :id";
    try {

      Query<ViPham> query = session.createQuery(hql);


      return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
    }catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping("rule={id}")
  public ResponseEntity<Object> getFoulRule( @PathVariable("id") int id){
    Session session = factory.openSession();
    String hql = "FROM ViPham WHERE noiQuy.maNQ= :id";
    try {

      Query<ViPham> query = session.createQuery(hql);


      return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
    }catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping("month={month}&year={year}")
  public ResponseEntity<Object> getFoulRule( @PathVariable("month") int month,
                                             @PathVariable("year") int year){
    Session session = factory.openSession();
    String hql = "FROM ViPham WHERE MONTH(thoiGian) = :month AND YEAR(thoiGian) = :year ";
    try {

      Query<ViPham> query = session.createQuery(hql);
      query.setParameter("month", month);
      query.setParameter("year", year);

      return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
    }catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping("employee={id}&now")
  public ResponseEntity<Object> getFoulNow( @PathVariable("id") int id){
    Session session = factory.openSession();
    String hql = "FROM ViPham WHERE nhanVien.maNV = :id AND MONTH(thoiGian) = MONTHs(sysdate()) AND YEAR(thoiGian) = YEAR(sysdate()) ";
    try {

      Query<ViPham> query = session.createQuery(hql);
      query.setParameter("id", id);

      return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
    }catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
  @RequestMapping("foul-now")
  public ResponseEntity<Object> getFoulNows( @PathVariable("id") int id){
    Session session = factory.openSession();
    String hql = "FROM ViPham WHERE MONTH(thoiGian) = MONTHs(sysdate()) AND YEAR(thoiGian) = YEAR(sysdate()) ";
    try {
      Query<ViPham> query = session.createQuery(hql);
      query.setParameter("id", id);

      return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
    }catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping("from-to")
  public ResponseEntity<Object> getFoulFromTo(@RequestBody List<String> form) throws ParseException {
    Session session = factory.openSession();
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date from = formatter.parse(form.get(0));
    Date to = formatter.parse(form.get(1));
    String hql = "FROM ViPham WHERE thoiGian BETWEEN :from AND :to";
    try {
      Query<ViPham> query = session.createQuery(hql);
      query.setParameter("from", from);
      query.setParameter("to", to);

      return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
    }catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
  @RequestMapping("employee={id}&from-to")
  public ResponseEntity<Object> getFoulEmpFromTo(@RequestBody List<String> form,
                                                 @PathVariable("id") int id) throws ParseException {
    Session session = factory.openSession();
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date from = formatter.parse(form.get(0));
    Date to = formatter.parse(form.get(1));
    String hql = "FROM ViPham WHERE nhanVien.maNV = :id AND thoiGian BETWEEN :from AND :to";
    try {
      Query<ViPham> query = session.createQuery(hql);
      query.setParameter("id", id);
      query.setParameter("from", from);
      query.setParameter("to", to);

      return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
    }catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
  @RequestMapping(value = "foul", method = RequestMethod.POST)
  public ResponseEntity<Object> postFoul(@RequestBody ViPham vp) throws ParseException {
    Session session = factory.openSession();
    Transaction tran = session.beginTransaction();
    try {
      session.save(vp);
      tran.commit();
      return new ResponseEntity<Object>(vp, HttpStatus.CREATED);
    }catch (Exception e) {
      tran.rollback();
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
