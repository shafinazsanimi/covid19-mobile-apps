package com.example.covid19apps;

public class Country {

    private String flag, country, cases, active, recovered, critical, deaths, newCases, newDeaths;

    public Country() {
    }

    public Country(String flag, String country, String cases, String active, String recovered, String critical, String deaths, String newCases, String newDeaths) {
        this.flag = flag;
        this.country = country;
        this.cases = cases;
        this.active = active;
        this.recovered = recovered;
        this.critical = critical;
        this.deaths = deaths;
        this.newCases = newCases;
        this.newDeaths = newDeaths;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    public String getCritical() {
        return critical;
    }

    public void setCritical(String critical) {
        this.critical = critical;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getNewCases() {
        return newCases;
    }

    public void setNewCases(String newCases) {
        this.newCases = newCases;
    }

    public String getNewDeaths() {
        return newDeaths;
    }

    public void setNewDeaths(String newDeaths) {
        this.newDeaths = newDeaths;
    }

}
