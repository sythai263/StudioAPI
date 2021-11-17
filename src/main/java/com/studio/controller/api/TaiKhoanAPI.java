package com.studio.controller.api;

import java.util.List;
import java.util.regex.Pattern;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.studio.entity.TaiKhoan;

@Controller
@RestController
@RequestMapping("api/accounts")
public class TaiKhoanAPI {

	final String REGEX_PHONE_VN = "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$";
	final Pattern PHONE_PATTERN = Pattern.compile(REGEX_PHONE_VN);

	@Autowired
	SessionFactory factory;

	@RequestMapping()
	public ResponseEntity<List<TaiKhoan>> getAccounts() {
		Session session = factory.openSession();
		String hql = "FROM TaiKhoan";

		@SuppressWarnings("unchecked")
		Query<TaiKhoan> query = session.createQuery(hql);

		return new ResponseEntity<List<TaiKhoan>>(query.list(), HttpStatus.OK);

	}

	@RequestMapping("{phone}")
	public ResponseEntity<Object> getAccount(@PathVariable("phone") String phone) {
		Session session = factory.openSession();

		TaiKhoan tk = new TaiKhoan(phone);
		try {
			session.refresh(tk);
			return new ResponseEntity<Object>(tk, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>("Not found", HttpStatus.BAD_REQUEST);
		} finally {
			session.close();
		}
	}

	@RequestMapping(value = "{phone}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteAcount(@PathVariable("phone") String phone) {
		Session session = factory.openSession();
		Transaction tran = session.beginTransaction();
		TaiKhoan tk = new TaiKhoan(phone);
		try {
			session.refresh(tk);
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

	@RequestMapping(value = "account", method = RequestMethod.POST)
	public ResponseEntity<Object> postAcount(@Validated @RequestBody TaiKhoan tk, BindingResult errors) {
		Session session = factory.openSession();

		Transaction tran = session.beginTransaction();
		if (tk.getSdt().isBlank()) {
			errors.rejectValue("phone", "Account", "Phone number not blank!");
		} else if (!PHONE_PATTERN.matcher(tk.getSdt()).matches()) {
			errors.rejectValue("phone", "Account", "Phone number is not in the correct format !");
		}
		if (tk.getMatKhau().isBlank()) {
			errors.rejectValue("password", "Account", "Password not blank!");
		}
		if (errors.hasErrors()) {
			return new ResponseEntity<Object>(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
		} else {
			tk.setBiKhoa(true);
			String hash = BCrypt.hashpw(tk.getMatKhau(), BCrypt.gensalt(12));
			tk.setMatKhau(hash);

			if (tk.getRole().equalsIgnoreCase("admin")) {
				tk.setRole("ADMIN");
			} else if (tk.getRole().equalsIgnoreCase("employee")) {
				tk.setRole("EMPLOYEE");
			} else
				tk.setRole("CUSTOMER");

			try {
				session.save(tk);
				tran.commit();
				return new ResponseEntity<Object>(tk, HttpStatus.CREATED);

			} catch (Exception e) {
				tran.rollback();
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
			} finally {
				session.close();
			}
		}
	}

	@RequestMapping(value = "account", method = RequestMethod.PUT)
	public ResponseEntity<Object> putAccount(@Validated @RequestBody TaiKhoan tk, 
			BindingResult errors) {
		Session session = factory.openSession();

		Transaction tran = session.beginTransaction();

		if (tk.getSdt().isBlank()) {
			errors.rejectValue("phone", "Account", "Phone number not blank!");
		} else if (!PHONE_PATTERN.matcher(tk.getSdt()).matches()) {
			errors.rejectValue("phone", "Account", "Phone number is not in the correct format !");
		}
		
		if (tk.getRole().equalsIgnoreCase("admin")) {
			tk.setRole("ADMIN");
		} else if (tk.getRole().equalsIgnoreCase("employee")) {
			tk.setRole("EMPLOYEE");
		} else
			tk.setRole("CUSTOMER");
		
		if (!errors.hasErrors()) {
			try {
				TaiKhoan tk1 = new TaiKhoan(tk.getSdt());
				session.refresh(tk1);
				tk1.setBiKhoa(tk.isBiKhoa());
				tk1.setRole(tk.getRole());
				session.update(tk1);
				tran.commit();
				return new ResponseEntity<Object>("Updated" + tk1.getSdt(), HttpStatus.CREATED);

			} catch (Exception e) {
				tran.rollback();
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
			} finally {
				session.close();
			}
		} else
			return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
	}
	@RequestMapping(value = "customer/change-password", method = RequestMethod.POST)
	public ResponseEntity<Object> postChangePassword(@Validated @RequestBody TaiKhoan tk, BindingResult errors){
		Session session = factory.openSession();
		Transaction tran = session.beginTransaction();
		if (tk.getSdt().isBlank()) {
			errors.rejectValue("phone", "Account", "Phone number not blank!");
		} else if (!PHONE_PATTERN.matcher(tk.getSdt()).matches()) {
			errors.rejectValue("phone", "Account", "Phone number is not in the correct format !");
		}

		if (!errors.hasErrors()) {
			try {
				TaiKhoan tk1 = new TaiKhoan(tk.getSdt());
				session.refresh(tk1);

				tk1.setMatKhau(BCrypt.hashpw(tk.getMatKhau(), BCrypt.gensalt(12)));

				session.update(tk1);
				tran.commit();
				return new ResponseEntity<Object>("Updated password for " + tk1.getSdt(), HttpStatus.CREATED);

			} catch (Exception e) {
				tran.rollback();
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
			} finally {
				session.close();
			}
		} else
			return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
	}
}
