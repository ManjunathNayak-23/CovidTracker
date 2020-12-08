package com.example.covidtracker.Model;

public class FavouritedCountryModel {
    String CountryName;

    public FavouritedCountryModel(String countryName) {
        CountryName = countryName;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }
}
