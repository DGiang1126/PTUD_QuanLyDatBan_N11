package entity;

import java.time.LocalDateTime;

public class CTPhieuDatBan {
	private String maPDB;
	private LocalDateTime timeNhanBan;
	private LocalDateTime timeTraBan;
	private int soNguoi;

	// Default constructor
	public CTPhieuDatBan() {
	}

	// Parameterized constructor
	public CTPhieuDatBan(String maPDB, LocalDateTime timeNhanBan, LocalDateTime timeTraBan, int soNguoi) {
		this.maPDB = maPDB;
		this.timeNhanBan = timeNhanBan;
		this.timeTraBan = timeTraBan;
		this.soNguoi = soNguoi;
	}


	public String getMaPDB() {
		return maPDB;
	}

	public void setMaPDB(String maPDB) {
		this.maPDB = maPDB;
	}

	public LocalDateTime getTimeNhanBan() {
		return timeNhanBan;
	}

	public void setTimeNhanBan(LocalDateTime timeNhanBan) {
		this.timeNhanBan = timeNhanBan;
	}

	public LocalDateTime getTimeTraBan() {
		return timeTraBan;
	}

	public void setTimeTraBan(LocalDateTime timeTraBan) {
		this.timeTraBan = timeTraBan;
	}

	public int getSoNguoi() {
		return soNguoi;
	}

	public void setSoNguoi(int soNguoi) {
		this.soNguoi = soNguoi;
	}

}