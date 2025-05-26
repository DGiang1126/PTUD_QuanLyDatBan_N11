package entity;

import java.util.Objects;

public class NhanVien {
    private String maNV;
    private String tenNV;
    private String sdt;
    private String gioiTinh;
    private String chucVu;
    private int tuoi;
    private double heSoLuong;
    private double luongNV;
    private String linkIMG;
    private String email;
    // No-argument constructor
    public NhanVien() {
    }
    // Parameterized constructor
    public NhanVien(String maNV, String tenNV, String sdt, String gioiTinh, String chucVu, int tuoi, double heSoLuong, double luongNV, String linkIMG, String email) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.sdt = sdt;
        this.gioiTinh = gioiTinh;
        this.chucVu = chucVu;
        this.tuoi = tuoi;
        this.heSoLuong = heSoLuong;
        this.luongNV = luongNV;
        this.linkIMG = linkIMG;
        this.email = email;
    }
    
	public String getLinkIMG() {
		return linkIMG;
	}
	public void setLinkIMG(String linkIMG) {
		this.linkIMG = linkIMG;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMaNV() {
		return maNV;
	}
	public void setMaNV(String maNV) {
		this.maNV = maNV;
	}
	public String getTenNV() {
		return tenNV;
	}
	public void setTenNV(String tenNV) {
		this.tenNV = tenNV;
	}
	public String getSdt() {
		return sdt;
	}
	public void setSdt(String sdt) {
		this.sdt = sdt;
	}
	public String getGioiTinh() {
		return gioiTinh;
	}
	public void setGioiTinh(String gioiTinh) {
		this.gioiTinh = gioiTinh;
	}
	public String getChucVu() {
		return chucVu;
	}
	public void setChucVu(String chucVu) {
		this.chucVu = chucVu;
	}
	public int getTuoi() {
		return tuoi;
	}
	public void setTuoi(int tuoi) {
		this.tuoi = tuoi;
	}
	public double getHeSoLuong() {
		return heSoLuong;
	}
	public void setHeSoLuong(double heSoLuong) {
		this.heSoLuong = heSoLuong;
	}
	public double getLuongNV() {
		return luongNV;
	}
	public void setLuongNV(double luongNV) {
		this.luongNV = luongNV;
	}
	@Override
	public int hashCode() {
		return Objects.hash(chucVu, gioiTinh, heSoLuong, luongNV, maNV, sdt, tenNV, tuoi);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NhanVien other = (NhanVien) obj;
		return Objects.equals(maNV, other.maNV);
	}
	@Override
	public String toString() {
		return "NhanVien [maNV=" + maNV + ", tenNV=" + tenNV + ", sdt=" + sdt + ", gioiTinh=" + gioiTinh + ", chucVu="
				+ chucVu + ", tuoi=" + tuoi + ", heSoLuong=" + heSoLuong + ", luongNV=" + luongNV + "]";
	}
	
  
}