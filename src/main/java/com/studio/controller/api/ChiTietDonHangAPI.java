package com.studio.controller.api;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.studio.entity.BangGia;
import com.studio.entity.ChiTietDonHang;
import com.studio.entity.DonHang;

@Controller
@RestController
@RequestMapping("api/detail-order")
public class ChiTietDonHangAPI {
	@Autowired
	SessionFactory factory;

	@RequestMapping
	public ResponseEntity<List<ChiTietDonHang>> getListDetail() {
		Session session = factory.openSession();

		String hql = "FROM ChiTietDonHang";
		@SuppressWarnings("unchecked")
		Query<ChiTietDonHang> query = session.createQuery(hql);
		return new ResponseEntity<List<ChiTietDonHang>>(query.list(), HttpStatus.OK);
	}

	@RequestMapping("/date={date}")
	public ResponseEntity<List<ChiTietDonHang>> getListDetailMonth(@PathVariable("date") int date) {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date from = new Date(cal.getTimeInMillis());

		cal.set(Calendar.DAY_OF_MONTH, date);
		Date to = new Date(cal.getTimeInMillis());

		Session session = factory.openSession();

		String hql = "FROM ChiTietDonHang WHERE donHang.tGBatDau BETWEEN :from AND :to";
		@SuppressWarnings("unchecked")
		Query<ChiTietDonHang> query = session.createQuery(hql);

		query.setParameter("from", from);
		query.setParameter("to", to);
		return new ResponseEntity<List<ChiTietDonHang>>(query.list(), HttpStatus.OK);
	}

	@RequestMapping(value = "from-to", method = RequestMethod.GET)
	public ResponseEntity<List<ChiTietDonHang>> getListDetailCustom(@RequestBody List<String> form)
			throws ParseException {

		Session session = factory.openSession();

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date from = formatter.parse(form.get(0));
		Date to = formatter.parse(form.get(1));

		String hql = "FROM ChiTietDonHang WHERE donHang.tGBatDau BETWEEN :from AND :to";
		@SuppressWarnings("unchecked")
		Query<ChiTietDonHang> query = session.createQuery(hql);

		query.setParameter("from", from);
		query.setParameter("to", to);
		return new ResponseEntity<List<ChiTietDonHang>>(query.list(), HttpStatus.OK);
	}

	@RequestMapping("order={id}")
	public ResponseEntity<List<ChiTietDonHang>> getOrder(@PathVariable("{id}") int id) {
		Session session = factory.openSession();

		String hql = "FROM ChiTietDonHang WHERE dichVu.maDV=:id";
		@SuppressWarnings("unchecked")
		Query<ChiTietDonHang> query = session.createQuery(hql);
		query.setParameter("id", id);
		return new ResponseEntity<List<ChiTietDonHang>>(query.list(), HttpStatus.OK);

	}

	@RequestMapping(value = "detail", method = RequestMethod.POST)
	public ResponseEntity<Object> postDetail(@Valid @RequestBody List<ChiTietDonHang> ct) {
		Session session = factory.openSession();

		Transaction tran = session.beginTransaction();
		try {

			for (ChiTietDonHang c : ct) {
				DonHang dh = c.getDonHang();
				session.refresh(dh);
				c.setDonHang(dh);

				BangGia bg = c.getBangGia();
				session.refresh(bg);
				c.setBangGia(bg);
				
				session.save(c);
			}
			tran.commit();
			return new ResponseEntity<Object>("Inserted detail orders ", HttpStatus.CREATED);
		} catch (Exception e) {
			tran.rollback();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "detail", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteDetail(@Valid @RequestBody List<Integer> ct) {
		Session session = factory.openSession();
		Transaction tran = session.beginTransaction();
		try {

			for (int id : ct) {
				ChiTietDonHang c = new ChiTietDonHang(id);
				session.refresh(c);
				session.delete(c);
			}
			tran.commit();
			return new ResponseEntity<Object>("Deleted detail orders ", HttpStatus.CREATED);
		} catch (Exception e) {
			tran.rollback();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	

}
