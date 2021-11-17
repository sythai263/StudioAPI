package com.studio.controller.api;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.studio.entity.ChuyenMon;
import com.studio.entity.KhachHang;
import com.studio.entity.NhanVien;
import com.studio.entity.TaiKhoan;

@Controller
@RestController
@RequestMapping("api/employees")
public class NhanVienAPI {

	@Autowired
	SessionFactory factory;

	@Autowired
	private GoogleCredential googleCredential;

	@Autowired
	private Drive googleDrive;

	@RequestMapping()
	public ResponseEntity<List<NhanVien>> getEmployees() {
		Session session = factory.openSession();
		String hql = "FROM NhanVien";

		@SuppressWarnings("unchecked")
		Query<NhanVien> query = session.createQuery(hql);

		return new ResponseEntity<List<NhanVien>>(query.list(), HttpStatus.OK);
	}

	@RequestMapping("{maNV}")
	public ResponseEntity<Object> getEmployee(@PathVariable("maNV") int manv) {
		Session session = factory.openSession();

		NhanVien nv = new NhanVien();
		nv.setMaNV(manv);
		try {
			session.refresh(nv);
			return new ResponseEntity<Object>(nv, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>("Not found", HttpStatus.BAD_REQUEST);
		} finally {
			session.close();
		}

	}

	@SuppressWarnings("unchecked")
	@RequestMapping("phone={phone}")
	public ResponseEntity<Object> getEmployee(@PathVariable("phone") String phone) {
		Session session = factory.openSession();
		try {
			String hql = "FROM NhanVien WHERE taiKhoan.sdt = :phone";
			Query<NhanVien> query = session.createQuery(hql);
			query.setParameter("phone", phone);

			NhanVien nv = query.list().get(0);
			return new ResponseEntity<Object>(nv, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>("Not found", HttpStatus.BAD_REQUEST);
		} finally {
			session.close();
		}

	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteEmployee(@PathVariable("id") int manv) {
		Session session = factory.openSession();
		NhanVien nv = new NhanVien();
		Transaction tran = session.beginTransaction();
		nv.setMaNV(manv);
		try {
			session.refresh(nv);
			TaiKhoan tk = nv.getTaiKhoan();
			tk.setBiKhoa(false);
			session.update(tk);
			tran.commit();
			return new ResponseEntity<Object>("Locked account", HttpStatus.CREATED);
		} catch (Exception e) {
			tran.rollback();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} finally {
			session.close();
		}
	}

	@RequestMapping(value = "employee", method = RequestMethod.POST)
	public ResponseEntity<Object> postEmployee(@Validated @RequestBody NhanVien nv, BindingResult errors)
			throws IOException {

		Session session = factory.openSession();

		Transaction tran = session.beginTransaction();

		TaiKhoan tk = nv.getTaiKhoan();
		tk.setBiKhoa(true);
		String hash = BCrypt.hashpw(tk.getMatKhau(), BCrypt.gensalt(12));
		tk.setMatKhau(hash);

		if (tk.getRole().equalsIgnoreCase("admin") || tk.getRole().equals("") || tk.getRole() == null) {
			tk.setRole("ADMIN");
		} else
			tk.setRole("EMPLOYEE");
		nv.setTaiKhoan(tk);
		if (!errors.hasErrors()) {
			try {
				ChuyenMon cm = nv.getChuyenMon();
				session.refresh(cm);
				nv.setChuyenMon(cm);
				nv.setAvatar("https://drive.google.com/uc?export=view&id=1LyfBPy_6L1NtcVwC5tR0b-SrFuxZwhsV");
				session.save(tk);
				session.save(nv);
				tran.commit();
				return new ResponseEntity<Object>(nv, HttpStatus.CREATED);

			} catch (Exception e) {
				tran.rollback();
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
			} finally {
				session.close();
			}
		} else
			return new ResponseEntity<Object>(errors.getAllErrors(), HttpStatus.BAD_REQUEST);

	}

	@RequestMapping(value = "employee-avatar", method = RequestMethod.POST)
	public ResponseEntity<Object> postEmployee(@Validated @RequestBody NhanVien nv,
			@RequestParam("file") MultipartFile files, BindingResult errors) throws IOException {

		File file = uploadFile(files);

		Session session = factory.openSession();

		Transaction tran = session.beginTransaction();

		TaiKhoan tk = nv.getTaiKhoan();
		tk.setBiKhoa(true);
		String hash = BCrypt.hashpw(tk.getMatKhau(), BCrypt.gensalt(12));
		tk.setMatKhau(hash);

		if (tk.getRole().equalsIgnoreCase("admin") || tk.getRole().equals("") || tk.getRole() == null) {
			tk.setRole("ADMIN");
		} else
			tk.setRole("EMPLOYEE");
		nv.setTaiKhoan(tk);
		if (!errors.hasErrors()) {
			try {
				ChuyenMon cm = nv.getChuyenMon();
				session.refresh(cm);
				nv.setChuyenMon(cm);
				nv.setAvatar("https://drive.google.com/uc?export=view&id=" + file.getId());
				session.save(tk);
				session.save(nv);
				tran.commit();
				return new ResponseEntity<Object>(nv, HttpStatus.CREATED);

			} catch (Exception e) {
				tran.rollback();
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
			} finally {
				session.close();
			}
		} else
			return new ResponseEntity<Object>(errors.getAllErrors(), HttpStatus.BAD_REQUEST);

	}

	@RequestMapping(value = "employee", method = RequestMethod.PUT)
	public ResponseEntity<Object> putEmployee(@Validated @RequestBody NhanVien nv, BindingResult errors) {
		Session session = factory.openSession();

		Transaction tran = session.beginTransaction();
		if (!errors.hasErrors()) {
			try {
				ChuyenMon cm = nv.getChuyenMon();
				session.refresh(cm);
				nv.setChuyenMon(cm);
				session.update(nv);
				tran.commit();
				return new ResponseEntity<Object>(nv, HttpStatus.CREATED);

			} catch (Exception e) {
				tran.rollback();
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
			} finally {
				session.close();
			}
		} else
			return new ResponseEntity<Object>(errors.getAllErrors(), HttpStatus.BAD_REQUEST);

	}

	@RequestMapping(value = "change-avatar/{id}", method = RequestMethod.POST)
	public ResponseEntity<Object> postAvatar(@RequestParam("file") MultipartFile files, @PathVariable("{id}") int id)
			throws IOException {

		File file = uploadFile(files);
		Session session = factory.openSession();
		Transaction tran = session.beginTransaction();
		try {
			NhanVien nv = new NhanVien(id);

			session.refresh(nv);
			nv.setAvatar("https://drive.google.com/uc?export=view&id=" + file.getId());
			session.update(nv);

			tran.commit();
			return new ResponseEntity<Object>("OK ", HttpStatus.CREATED);
		} catch (Exception e) {
			tran.rollback();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "{id}/location", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateLocation(@PathVariable("id") int id, 
			@RequestParam("kinhdo") double kinhdo, 
			@RequestParam("vido") double vido) {

		Session session = factory.openSession();
		Transaction tran = session.beginTransaction();
		try {
			NhanVien nv = new NhanVien(id);
			session.refresh(nv);
			nv.setKinhDo(kinhdo);
			nv.setViDo(vido);
			nv.setCapNhat(new Date());
			session.update(nv);
			tran.commit();
			return new ResponseEntity<Object>(true, HttpStatus.CREATED);

		} catch (Exception e) {
			tran.rollback();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	public File uploadFile(MultipartFile files) throws IOException {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		java.io.File cvf = convert(files);

		File fileMetadata = new File();
		fileMetadata.setParents(Collections.singletonList("14M1Mec75EE5yE5FCtNW52eQ4ybrrVg65"))
				.setName("avatar_" + format.format(cal.getTime()) + ".jpg");
		FileContent mediaContent = new FileContent("image/jpeg", cvf);
		File file = googleDrive.files().create(fileMetadata, mediaContent).setFields("id").execute();
		cvf.delete();
		return file;
	}

	public java.io.File convert(MultipartFile file) throws IOException {
		java.io.File convFile = new java.io.File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}
}
