package com.studio.controller.api;

import com.studio.entity.Luong;
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
@RequestMapping("api/salaries")
public class LuongAPI {
  @Autowired
  SessionFactory factory;

  @RequestMapping()
  public ResponseEntity<Object> getSalaries(){
    Session session = factory.openSession();
    String hql = "FROM Luong";

    Query<Luong> query = session.createQuery(hql);
    return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
  }
  @RequestMapping("{id}")
  public ResponseEntity<Object> getSalary(@PathVariable("id") int id){
    Session session = factory.openSession();
    String hql = "FROM Luong WHERE maLuong = :id";

    Query<Luong> query = session.createQuery(hql);
    query.setParameter("id", id);
    return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
  }

  @RequestMapping("employee={id}")
  public ResponseEntity<Object> getSalaryEmployee(@PathVariable("id") int id){
    Session session = factory.openSession();
    String hql = "FROM Luong WHERE nhanVien.maNV = :id";

    Query<Luong> query = session.createQuery(hql);
    query.setParameter("id", id);
    return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
  }
  @RequestMapping("month={month}&year={year}")
  public ResponseEntity<Object> getSalaryMonth(@PathVariable("month") int month,
                                               @PathVariable("year") int year){
    Session session = factory.openSession();
    String hql = "FROM Luong WHERE MONTH(ngayTK) = :month AND YEAR(ngayTK) = :year ";

    Query<Luong> query = session.createQuery(hql);
    query.setParameter("month", month);
    query.setParameter("year", year);
    return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
  }
  @RequestMapping("month")
  public ResponseEntity<Object> getSalaryMonthPresent(){
    Session session = factory.openSession();
    String hql = "FROM Luong WHERE MONTH(ngayTK) = MONTH(sysDate())";

    Query<Luong> query = session.createQuery(hql);
    return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
  }
  @RequestMapping("year")
  public ResponseEntity<Object> getSalaryYearPresent(){
    Session session = factory.openSession();
    String hql = "FROM Luong WHERE MONTH(ngayTK) = MONTH(sysDate())";

    Query<Luong> query = session.createQuery(hql);
    return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
  }

  @RequestMapping("from-to")
  public ResponseEntity<Object> getSalaryFrom(@RequestBody List<String> form) throws ParseException {
    Session session = factory.openSession();

    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date from = formatter.parse(form.get(0));
    Date to = formatter.parse(form.get(1));
    String hql = "FROM Luong WHERE ngayTK BETWEEN :from AND :to";

    Query<Luong> query = session.createQuery(hql);
    query.setParameter("from", from);
    query.setParameter("to", to);

    return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
  }
  @RequestMapping("salary")
  public ResponseEntity<Object> postSalary(@RequestBody Luong l){

    Session session = factory.openSession();
    Transaction tran = session.beginTransaction();
    try {
      session.save(l);
      tran.commit();
      return new ResponseEntity<Object>(l, HttpStatus.CREATED);
    }catch (Exception e){
      tran.rollback();
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "salary", method = RequestMethod.PUT)
  public ResponseEntity<Object> putSalary(@RequestBody Luong l){

    Session session = factory.openSession();
    Transaction tran = session.beginTransaction();
    Luong l1 = new Luong(l.getMaLuong());
    if(!l1.isThanhToan()){
      try {
        l.setNhanVien(l1.getNhanVien());
        tran.commit();
        return new ResponseEntity<Object>(l, HttpStatus.CREATED);
      }catch (Exception e){
        tran.rollback();
        return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
    }else return new ResponseEntity<Object>("Unable to edit paid invoice information", HttpStatus.BAD_REQUEST);
  }
}
