package pkgExcel;

public class SeasonPlayerPositionStat {


	int season_id;
	int player_id;
	int position_id;
	int stat_id;
	int stat_value;
	
	
	public SeasonPlayerPositionStat(int season_id, int player_id, int position_id, int stat_id, int stat_value) {
		super();
		this.season_id = season_id;
		this.player_id = player_id;
		this.position_id = position_id;
		this.stat_id = stat_id;
		this.stat_value = stat_value;
	}
	
	public int getSeason_id() {
		return season_id;
	}
	public void setSeason_id(int season_id) {
		this.season_id = season_id;
	}
	public int getPlayer_id() {
		return player_id;
	}
	public void setPlayer_id(int player_id) {
		this.player_id = player_id;
	}
	public int getPosition_id() {
		return position_id;
	}
	public void setPosition_id(int position_id) {
		this.position_id = position_id;
	}
	public int getStat_id() {
		return stat_id;
	}
	public void setStat_id(int stat_id) {
		this.stat_id = stat_id;
	}
	public int getStat_value() {
		return stat_value;
	}
	public void setStat_value(int stat_value) {
		this.stat_value = stat_value;
	}
	
	
	
}
