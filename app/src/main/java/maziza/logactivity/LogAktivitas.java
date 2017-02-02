package maziza.logactivity;

/**
 * Created by Aziz on 01/02/2017.
 */

public class LogAktivitas {

    private String username;
    private String aktivitas;

    LogAktivitas(String username, String aktivitas){
        this.username = username;
        this.aktivitas= aktivitas;
    }

    String getUsername(){
        return username;
    }
    public void setUSername(String username){
        this.username = username;

    }

    String getAktivitas()
    {
        return aktivitas;
    }
    public void setAktivitas(String aktivitas){
        this.aktivitas = aktivitas;

    }
}
