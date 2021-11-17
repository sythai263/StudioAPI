package com.studio.controller.api;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

@Controller
@RestController
@RequestMapping("api/customers")

public class KhachHangAPI {
	@Autowired
	SessionFactory factory;

	@Autowired
	private GoogleCredential googleCredential;

	@Autowired
	private Drive googleDrive;


	@RequestMapping
	public ResponseEntity<List<KhachHang>> getCustomers() {

		Session session = factory.openSession();
		String hql = "FROM KhachHang";

		Query<KhachHang> query = session.createQuery(hql);
		return new ResponseEntity<List<KhachHang>>(query.list(), HttpStatus.OK);

	}

	@RequestMapping("{id}")
	public ResponseEntity<Object> getCustomer(@PathVariable("id") int id) {

		Session session = factory.openSession();
		KhachHang kh = new KhachHang(id);
		try {
			session.refresh(kh);
			return new ResponseEntity<Object>(kh, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "id", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteCustomer(@PathVariable("id") int id) {
		Session session = factory.openSession();
		Transaction tran = session.beginTransaction();
		try {
			KhachHang kh = new KhachHang(id);
			session.refresh(kh);
			TaiKhoan tk = kh.getTaiKhoan();
			tk.setBiKhoa(false);
			session.update(tk);
			tran.commit();
			return new ResponseEntity<Object>("Deleted account ", HttpStatus.CREATED);
		} catch (Exception e) {
			tran.rollback();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "customer-avatar", method = RequestMethod.POST)
	public ResponseEntity<Object> postCustomerAvatar(@Validated @RequestPart("customer") KhachHang kh,
			@RequestPart("file") MultipartFile files, BindingResult errors) throws IOException {
		File file = uploadFile(files);
		Session session = factory.openSession();
		Transaction tran = session.beginTransaction();
		TaiKhoan tk = kh.getTaiKhoan();

		tk.setBiKhoa(true);
		String hash = BCrypt.hashpw(tk.getMatKhau(), BCrypt.gensalt(12));
		tk.setMatKhau(hash);

		tk.setRole("CUSTOMER");
		kh.setTaiKhoan(tk);
		if (!errors.hasErrors()) {
			try {
				session.save(tk);
				kh.setAvatar("https://drive.google.com/uc?export=view&id=" + file.getId());
				session.save(kh);
				tran.commit();
				return new ResponseEntity<Object>(kh, HttpStatus.CREATED);
			} catch (Exception e) {
				tran.rollback();
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
			} finally {
				session.close();
			}
		} else
			return new ResponseEntity<Object>(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "customer", method = RequestMethod.POST)
	public ResponseEntity<Object> postCustomer(@Validated @RequestBody KhachHang kh, BindingResult errors)
			throws IOException {
		Session session = factory.openSession();
		Transaction tran = session.beginTransaction();
		TaiKhoan tk = kh.getTaiKhoan();

		tk.setBiKhoa(true);
		String hash = BCrypt.hashpw(tk.getMatKhau(), BCrypt.gensalt(12));
		tk.setMatKhau(hash);

		tk.setRole("CUSTOMER");
		kh.setTaiKhoan(tk);
		if (!errors.hasErrors()) {
			try {
				session.save(tk);
				kh.setAvatar("https://drive.google.com/uc?export=view&id=1LyfBPy_6L1NtcVwC5tR0b-SrFuxZwhsV");
				session.save(kh);
				tran.commit();
				return new ResponseEntity<Object>(kh, HttpStatus.CREATED);
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
	public ResponseEntity<Object> postAvatar(@RequestParam("file") MultipartFile files, 
			@PathVariable("{id}") int id)
			throws IOException {

		File file = uploadFile(files);
		Session session = factory.openSession();
		Transaction tran = session.beginTransaction();
		try {
			KhachHang kh = new KhachHang(id);
			session.refresh(kh);
			kh.setAvatar("https://drive.google.com/uc?export=view&id=" + file.getId());
			tran.commit();
			return new ResponseEntity<Object>("OK ", HttpStatus.CREATED);
		} catch (Exception e) {
			tran.rollback();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "customer", method = RequestMethod.PUT)
	public ResponseEntity<Object> putCustomer(@Validated @RequestBody KhachHang kh, BindingResult errors) {
		Session session = factory.openSession();
		Transaction tran = session.beginTransaction();

		if (!errors.hasErrors()) {
			try {
				TaiKhoan tk = new TaiKhoan(kh.getTaiKhoan().getSdt());
				session.refresh(tk);
				kh.setTaiKhoan(tk);
				session.update(kh);
				tran.commit();
				return new ResponseEntity<Object>("Updated info customer", HttpStatus.CREATED);

			} catch (Exception e) {
				tran.rollback();
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
			} finally {
				session.close();
			}
		} else
			return new ResponseEntity<Object>(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
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
		java.io.File convFile = new java.io.File("temp.jpg");
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		
        
		return convFile;
	}

}
