package entity;

public class Ban {
	private String maBan;
	private String tenBan;
	private String trangThai;
	private int tang;
	private int soCho;
	private String ghiChu;
	public Ban() {
		super();
	}

	public Ban(String maBan, String trangThai, int tang, int soCho, String ghiChu, String tenBan) {
		this.maBan = maBan;
		this.trangThai = trangThai;
		this.tang = tang;
		this.soCho = soCho;
		this.ghiChu = ghiChu;
		this.tenBan = tenBan;

	}

	public String getTenBan() {
		return tenBan;
	}

	public void setTenBan(String tenBan) {
		this.tenBan = tenBan;
	}

	public String getMaBan() {
		return maBan;
	}

	public void setMaBan(String maBan) {
		this.maBan = maBan;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public int getTang() {
		return tang;
	}

	public void setTang(int tang) {
		this.tang = tang;
	}

	public int getSoCho() {
		return soCho;
	}

	public void setSoCho(int soCho) {
		this.soCho = soCho;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

}