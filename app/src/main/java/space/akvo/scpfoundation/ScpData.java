package space.akvo.scpfoundation;

/**
 * Created by akvo on 2017/9/6.
 */

public class ScpData {
    private String scp_id;
    private String scp_nam;
    private String scp_lev;

    public ScpData(String scp_id,String scp_nam,String scp_lev){
        this.scp_id = scp_id;
        this.scp_nam = scp_nam;
        this.scp_lev = scp_lev;
    }

    public String getScp_id() {
        return scp_id;
    }

    public String getScp_nam() {
        return scp_nam;
    }

    public String getScp_lev() {
        return scp_lev;
    }
}
