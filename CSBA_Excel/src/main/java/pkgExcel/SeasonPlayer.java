package pkgExcel;

public class SeasonPlayer {

	int season_id;
	int player_id;
	

	
	public SeasonPlayer(int season_id, int player_id) {
		super();
		this.season_id = season_id;
		this.player_id = player_id;
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
	
	
	
}
