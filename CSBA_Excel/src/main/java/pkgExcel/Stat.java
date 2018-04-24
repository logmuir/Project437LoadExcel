package pkgExcel;

public class Stat {

	int stat_id;
	String stat_name;
	String stat_abbr;
	String cast_as;
	
	public Stat(int stat_id, String stat_name, String stat_abbr, String cast_as) {
		this.stat_id = stat_id;
		this.stat_name = stat_name;
		this.stat_abbr = stat_abbr;
		this.cast_as = cast_as;
	}
	
	public int getStat_id() {
		return stat_id;
	}
	public void setStat_id(int stat_id) {
		this.stat_id = stat_id;
	}

	public String getStat_name() {
		return stat_name;
	}
	public void setStat_name(String stat_name) {
		this.stat_name = stat_name;
	}
	public String getStat_abbr() {
		return stat_abbr;
	}
	public void setStat_abbr(String stat_abbr) {
		this.stat_abbr = stat_abbr;
	}
	public String getCast_as() {
		return cast_as;
	}
	public void setCast_as(String cast_as) {
		this.cast_as = cast_as;
	}
	
}
