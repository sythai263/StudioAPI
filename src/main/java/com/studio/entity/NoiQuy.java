package com.studio.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "NOIQUY")
public class NoiQuy {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MANQ")
	private int maNQ;
	
	@Column(name = "TENNQ")
	@NotBlank(message = "Tên nội quy là bắt buộc.")
	private String tenNQ;
	
	@Column(name = "MOTA")
	private String moTa;
	
	@Column(name = "TIENPHAT")
	@NotBlank(message = "Phải nhập tiền phạt cho nội quy này.")
	@Min(value = 0, message = "Tiền phạt phải lớn hơn 0.")
	private int tienPhat;
	
	@OneToMany(mappedBy = "noiQuy", fetch = FetchType.EAGER)

	public int getMaNQ() {
		return maNQ;
	}

	public void setMaNQ(int maNQ) {
		this.maNQ = maNQ;
	}

	public String getTenNQ() {
		return tenNQ;
	}

	public void setTenNQ(String tenNQ) {
		this.tenNQ = tenNQ;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public int getTienPhat() {
		return tienPhat;
	}

	public void setTienPhat(int tienPhat) {
		this.tienPhat = tienPhat;
	}

	public NoiQuy() {
		super();
	}

	public NoiQuy(int maNQ) {
		this.maNQ = maNQ;
	}
}
