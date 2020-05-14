package com.example.travel01;

public class ProvincesAndCitys {
    private String provincename;
    private String cityname;

    public String getProvincename() {
        return provincename;
    }

    public void setProvincename(String provincename) {
        this.provincename = provincename;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }


    public ProvincesAndCitys(String provincename, String cityname) {
        this.provincename = provincename;
        this.cityname = cityname;
    }

    public ProvincesAndCitys() {
    }
}
