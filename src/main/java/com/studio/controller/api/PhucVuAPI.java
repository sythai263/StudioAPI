package com.studio.controller.api;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.gson.Gson;
import com.studio.entity.KhachHang;
import com.studio.entity.PhucVu;
import com.studio.entity.TaiKhoan;
import com.studio.model.Analys;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RestController
@RequestMapping("api/assignments")
public class PhucVuAPI {
	@Autowired
	SessionFactory factory;
	@Autowired
	private GoogleCredential googleCredential;

	@Autowired
	private Drive googleDrive;

	@RequestMapping
	public ResponseEntity<Object> getAssignments() {

		Session session = factory.openSession();
		String hql = "FROM PhucVu";

		Query<PhucVu> query = session.createQuery(hql);
		return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
	}

	@RequestMapping("{id}")
	public ResponseEntity<Object> getAssignment(@PathVariable("id") int id) {

		Session session = factory.openSession();
		try {
			PhucVu pv = new PhucVu(id);
			session.refresh(pv);
			return new ResponseEntity<Object>(pv, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("employee={id}")
	public ResponseEntity<Object> getAssignmentEmployee(@PathVariable("id") int id) {

		Session session = factory.openSession();
		String hql = "FROM PhucVu WHERE nhanVien.maNV= :id";

		try {
			Query<PhucVu> query = session.createQuery(hql);
			query.setParameter("id", id);
			return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("employee={id}&now")
	public ResponseEntity<Object> getAssignmentEmployeeNow(@PathVariable("id") int id) {
		Session session = factory.openSession();
		String hql = "FROM PhucVu WHERE  nhanVien.maNV= :id AND DATE(chiTiet.donHang.tGBatDau) = curdate()";

		try {
			Query<PhucVu> query = session.createQuery(hql);
			query.setParameter("id", id);
			return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("now")
	public ResponseEntity<Object> getAssignmentNow() {
		Session session = factory.openSession();
		String hql = "FROM PhucVu WHERE DATE(chiTiet.donHang.tGBatDau) = curdate()";

		try {
			Query<PhucVu> query = session.createQuery(hql);
			return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("order={id}")
	public ResponseEntity<Object> getAssignmentOrder(@PathVariable("id") int id) {

		Session session = factory.openSession();
		String hql = "FROM PhucVu WHERE chiTiet.donHang.maDH= :id";

		try {
			Query<PhucVu> query = session.createQuery(hql);
			query.setParameter("id", id);
			return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("order={id}&now")
	public ResponseEntity<Object> getAssignmentOrderNow(@PathVariable("id") int id) {
		Session session = factory.openSession();
		String hql = "FROM PhucVu WHERE chiTiet.donHang.maDH = :id AND DATE(chiTiet.donHang.tGBatDau) = curdate()";

		try {
			Query<PhucVu> query = session.createQuery(hql);
			query.setParameter("id", id);
			return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "assignment", method = RequestMethod.POST)
	public ResponseEntity<Object> postAssignment(@Valid @RequestBody List<PhucVu> pv) {
		Session session = factory.openSession();

		Transaction tran = session.beginTransaction();
		try {

			for (PhucVu c : pv) {
				session.save(c);
			}
			tran.commit();
			return new ResponseEntity<Object>("Inserted assignment ", HttpStatus.CREATED);
		} catch (Exception e) {
			tran.rollback();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "assignment", method = RequestMethod.PUT)
	public ResponseEntity<Object> postAssignment(@Valid @RequestBody PhucVu pv) {
		Session session = factory.openSession();

		Transaction tran = session.beginTransaction();
		try {
			PhucVu p = new PhucVu(pv.getMaPV());
			session.refresh(p);
			pv.setNhanVien(p.getNhanVien());
			pv.setChiTiet(p.getChiTiet());
			session.update(pv);
			tran.commit();
			return new ResponseEntity<Object>("Inserted detail orders ", HttpStatus.CREATED);
		} catch (Exception e) {
			tran.rollback();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("hoa-hong/employee={id}")
	public ResponseEntity<Object> getHoaHongThang(@PathVariable("id") int id) {

		Session session = factory.openSession();
		String hql = "SELECT DAY(chiTiet.donHang.tGBatDau), SUM(hoaHong) as tong " + "FROM PhucVu "
				+ "WHERE nhanVien.maNV = :id AND MONTH(chiTiet.donHang.tGBatDau) = MONTH(SYSDATE()) "
				+ "AND YEAR(chiTiet.donHang.tGBatDau) = YEAR(sysdate()) " + "GROUP BY DAY(chiTiet.donHang.tGBatDau) ";
		Query<Analys> query = session.createQuery(hql);

		query.setParameter("id", id);
		return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
	}

	@RequestMapping("hoa-hong/employee={id}&year={year}")
	public ResponseEntity<Object> getHoaHongNam(@PathVariable("id") int id, @PathVariable("year") int year) {

		Session session = factory.openSession();
		String hql = "SELECT SUM(hoaHong) as tong " + "FROM PhucVu "
				+ "WHERE nhanVien.maNV = :id AND YEAR(chiTiet.donHang.tGBatDau) = :year ";
		Query<Analys> query = session.createQuery(hql);

		query.setParameter("id", id);
		query.setParameter("year", year);
		return new ResponseEntity<Object>(query.list(), HttpStatus.OK);
	}

	@RequestMapping(value = "check-in", method = RequestMethod.POST)
	public ResponseEntity<Object> postCheckIn(@Validated @RequestPart("phucvu") PhucVu pv,
			@RequestPart("file") MultipartFile files, BindingResult errors) throws IOException {
		
		File file = uploadFileToCheckIn(files);
		Session session = factory.openSession();
		Transaction tran = session.beginTransaction();

		if (!errors.hasErrors()) {
			try {
				session.refresh(pv);
				pv.setAnhCheckIn("https://drive.google.com/uc?export=view&id=" + file.getId());
				pv.settGCheckIn(new Date());
				session.update(pv);
				tran.commit();
				session.refresh(pv);
				return new ResponseEntity<Object>(pv, HttpStatus.CREATED);
			} catch (Exception e) {
				tran.rollback();
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
			} finally {
				session.close();
			}
		} else
			return new ResponseEntity<Object>(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
	}
	@RequestMapping(value = "check-out", method = RequestMethod.POST)
	public ResponseEntity<Object> postCheckOut(@Validated @RequestPart("phucvu") PhucVu pv,
			@RequestPart("file") MultipartFile files, BindingResult errors) throws IOException {
		
		File file = uploadFileToCheckOut(files);
		Session session = factory.openSession();
		Transaction tran = session.beginTransaction();

		if (!errors.hasErrors()) {
			try {
				session.refresh(pv);
				pv.setAnhCheckOut("https://drive.google.com/uc?export=view&id=" + file.getId());
				pv.settGCheckOut(new Date());
				session.update(pv);
				tran.commit();
				session.refresh(pv);
				return new ResponseEntity<Object>(pv, HttpStatus.CREATED);
			} catch (Exception e) {
				tran.rollback();
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
			} finally {
				session.close();
			}
		} else
			return new ResponseEntity<Object>(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
	}

	public File uploadFileToCheckIn(MultipartFile files) throws IOException {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		java.io.File cvf = convert(files);

		File fileMetadata = new File();
		fileMetadata.setParents(Collections.singletonList("1ObvVoBjBulSvs3wTDoTjCPhAyPUNRn5Y"))
				.setName("check-in_" + format.format(cal.getTime()) + ".jpg");
		FileContent mediaContent = new FileContent("image/jpeg", cvf);
		File file = googleDrive.files().create(fileMetadata, mediaContent).setFields("id").execute();
		cvf.delete();
		return file;
	}
	
	public File uploadFileToCheckOut(MultipartFile files) throws IOException {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		java.io.File cvf = convert(files);

		File fileMetadata = new File();
		fileMetadata.setParents(Collections.singletonList("11Gqvm6NMZ7SuxrSgb4iHvoKrPpFtKZNz"))
				.setName("check-out_" + format.format(cal.getTime()) + ".jpg");
		FileContent mediaContent = new FileContent("image/jpeg", cvf);
		File file = googleDrive.files().create(fileMetadata, mediaContent).setFields("id").execute();
		cvf.delete();
		return file;
	}

	public java.io.File convert(MultipartFile file) throws IOException {
		java.io.File convFile = new java.io.File("temp.jpg");
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());

		return convFile;
	}
}
