package com.studio.controller.api;

import com.studio.entity.DonHang;
import com.studio.model.Analys;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RestController
@RequestMapping("api/orders")
public class DonHangAPI {

  @Autowired
  SessionFactory factory;

  @RequestMapping
  public ResponseEntity<List<DonHang>> getOrders(){
    Session session = factory.openSession();
    String hql = "FROM DonHang";

    Query<DonHang> query = session.createQuery(hql);

    return new ResponseEntity<List<DonHang>>(query.list(), HttpStatus.OK);
  }
  @RequestMapping("customer={id}")
  public ResponseEntity<List<DonHang>> getOrdersOfCustomer(@PathVariable("id") int id){
    Session session = factory.openSession();
    String hql = "FROM DonHang WHERE khachHang.maKH= :id";

    Query<DonHang> query = session.createQuery(hql);
    query.setParameter("id", id);

    return new ResponseEntity<List<DonHang>>(query.list(), HttpStatus.OK);
  }
  @RequestMapping("month={month}")
  public ResponseEntity<List<DonHang>> getOrdersOfMonth(@PathVariable("month") int month){
    Session session = factory.openSession();
    String hql = "FROM DonHang WHERE MONTH(tGBatDau)= :month";

    Query<DonHang> query = session.createQuery(hql);
    query.setParameter("month", month);

    return new ResponseEntity<List<DonHang>>(query.list(), HttpStatus.OK);
  }
  @RequestMapping("from-to")
  public ResponseEntity<List<DonHang>> getOrdersOfFromTo(@RequestParam("from") long from, @RequestParam("to") long to){
    Session session = factory.openSession();
    String hql = "FROM DonHang WHERE tGBatDau BETWEEN :from AND :to";

    Query<DonHang> query = session.createQuery(hql);
    query.setParameter("from", new Date(from));
    query.setParameter("to", new Date(to));

    return new ResponseEntity<List<DonHang>>(query.list(), HttpStatus.OK);
  }
  @RequestMapping("month/now")
  public ResponseEntity<List<DonHang>> getOrdersOfMontoNow(){
    Session session = factory.openSession();
    String hql = "FROM DonHang WHERE tGBatDau BETWEEN CONCAT(YEAR(sysdate()),'-',MONTH(sysdate()),'-1 00:00:00') AND CURRENT_TIMESTAMP";

    Query<DonHang> query = session.createQuery(hql);

    return new ResponseEntity<List<DonHang>>(query.list(), HttpStatus.OK);
  }
  @RequestMapping("now")
  public ResponseEntity<List<DonHang>> getOrdersNow(){
    Session session = factory.openSession();
    String hql = "FROM DonHang WHERE DATE(tGBatDau) = CURDATE()";

    Query<DonHang> query = session.createQuery(hql);

    return new ResponseEntity<List<DonHang>>(query.list(), HttpStatus.OK);
  }

  @RequestMapping("{id}")
  public ResponseEntity<Object> getOrder(@PathVariable("id") int id){
    Session session = factory.openSession();
    try {
      DonHang dh = new DonHang(id);
      session.refresh(dh);
      return new ResponseEntity<Object>(dh, HttpStatus.OK);

    }catch (Exception e){
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.OK);
    }
  }

  @RequestMapping( value = "{id}", method = RequestMethod.DELETE)
  public ResponseEntity<Object> deleteOrder(@PathVariable("id") int id){
    Session session = factory.openSession();
    Transaction tran = session.beginTransaction();
    try {
      DonHang dh = new DonHang(id);
      session.refresh(dh);
      session.delete(dh);
      tran.commit();
      return new ResponseEntity<Object>("Deleted Order", HttpStatus.CREATED);

    }catch (Exception e){
      tran.rollback();
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "order", method = RequestMethod.POST)
  public ResponseEntity<Object> postOrder(@RequestBody DonHang dh){
    Session session = factory.openSession();
    Transaction tran = session.beginTransaction();
    try{
      session.save(dh);
      tran.commit();
      return new ResponseEntity<Object>("Inserted oder", HttpStatus.CREATED);
    } catch (Exception e){
      tran.rollback();
      session.close();
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "order", method = RequestMethod.PUT)
  public ResponseEntity<Object> putOrder(@RequestBody DonHang dh){
    Session session = factory.openSession();
    Transaction tran = session.beginTransaction();
    try{
      DonHang dh1 = new DonHang(dh.getMaDH());
      session.refresh(dh1);
      dh.setKhachHang(dh.getKhachHang());

      session.update(dh);
      tran.commit();
      return new ResponseEntity<Object>("Updated oder", HttpStatus.CREATED);
    } catch (Exception e){
      tran.rollback();
      session.close();
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping("sumary")
  public ResponseEntity<Object> getSumary(){
	  Session session = factory.openSession();
	  String hql = "SELECT MONTH(tGBatDau),SUM(tongTien) FROM DonHang WHERE tGBatDau "
	  		+ " BETWEEN CONCAT(YEAR(sysdate()),'-01-01 00:00:00') AND CURRENT_TIMESTAMP "
	  		+ "GROUP BY MONTH(tGBatDau)";
	  Query<Analys> query = session.createQuery(hql);

	  return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
  }
  @RequestMapping("sum/month")
  public ResponseEntity<Object> getOrderNow(){
	  Session session = factory.openSession();
	  String hql = "SELECT DAY(tGBatDau), SUM(tongTien) FROM DonHang "
	  		+ "WHERE tGBatDau BETWEEN CONCAT(YEAR(sysdate()),'-',MONTH(sysdate()),'-1 00:00:00') AND CURRENT_TIMESTAMP "
	  		+ "GROUP BY DAY(tGBatDau)";
	  Query<Analys> query = session.createQuery(hql);
	  
	  return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
  }
  
  @RequestMapping("sum/month={month}")
  public ResponseEntity<Object> getOrderMonth(@PathVariable("month") int month){
	  Session session = factory.openSession();
	  String hql = "SELECT DAY(tGBatDau), SUM(tongTien) FROM DonHang "
	  		+ "WHERE tGBatDau BETWEEN CONCAT(YEAR(sysdate()),'-',MONTH(sysdate()),'-1 00:00:00') AND CURRENT_TIMESTAMP "
	  		+ "GROUP BY DAY(tGBatDau)";
	  Query<Analys> query = session.createQuery(hql);
	  query.setParameter("month", month);
	  
	  return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
  }
  
  @RequestMapping("sum/from-to")
  public ResponseEntity<Object> getOrderFromTo(@RequestParam("from") long from, 
		  @RequestParam("to") long to){
	  Session session = factory.openSession();
	  
	  String hql = "SELECT MONTH(tGBatDau), DAY(tGBatDau), SUM(tongTien) FROM DonHang "
	  		+ "WHERE tGBatDau BETWEEN :from AND :to "
	  		+ "GROUP BY MONTH(tGBatDau), DAY(tGBatDau)";
	  Query<Analys> query = session.createQuery(hql);
	  query.setParameter("from", new Date(from));
	  query.setParameter("to", new Date(to));
	  
	  return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
  }

}
