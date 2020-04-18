package com.example.coronatracker;

public class CovidCountry {

    String mCovidCountry;
    String mCases;
    String mTodayCases;
    String mDeaths;
    String mRecovered;
    String mflags;
    String mCritical;
    private boolean expanded;


    public CovidCountry(String mCovidCountry,String mCases,String mRecovered,String mflags,String mTodayCases,String mCritical,String mDeaths) {
        this.mCovidCountry = mCovidCountry;
        this.mCases = mCases;
        this.mRecovered = mRecovered;
        this.mTodayCases = mTodayCases;
        this.mCritical = mCritical;
        this.mDeaths = mDeaths;
        this.mflags = mflags;
        this.expanded = false;


    }

    public String getmCovidCountry() {
        return mCovidCountry;
    }


    public String getmCases() {
        return mCases;
    }


    public String getmRecovered() {
        return mRecovered;
    }

    public String getmTodayCases() {
        return mTodayCases;
    }

    public String getmDeaths() {
        return mDeaths;
    }

    public String getmCritical() {
        return mCritical;
    }

    public String getMflags() {
        return mflags;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
