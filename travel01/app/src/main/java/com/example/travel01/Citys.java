package com.example.travel01;

public class Citys {
    public String getCitynm() {
        return citynm;
    }


    public void setCitynm(String citynm) {
        this.citynm = citynm;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    private String citynm;
    private String cityid;

    public Citys(String citynm, String cityid) {
        this.citynm = citynm;
        this.cityid = cityid;
    }

    @Override
    public String toString() {
        return "Citys{" +
                "citynm='" + citynm + '\'' +
                ", cityid='" + cityid + '\'' +
                '}';
    }
}
