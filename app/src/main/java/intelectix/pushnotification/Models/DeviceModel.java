package intelectix.pushnotification.Models;

import com.orm.SugarRecord;


public class DeviceModel extends SugarRecord<DeviceModel> {

    String token;
    Boolean active;

    public DeviceModel(){}

    public DeviceModel(String token, Boolean active){
        this.token = token;
        this.active = active;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
