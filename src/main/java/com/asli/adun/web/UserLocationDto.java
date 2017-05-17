package com.asli.adun.web;
public class UserLocationDto {
    private Gender gender;
    private String fullAddress;
    private Float latitude;
    private Float longitude;

    public UserLocationDto(Gender gender, String fullAddress, Float latitude, Float longitude) {
        this.gender = gender;
        this.fullAddress = fullAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Gender getGender() {
        return gender;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserLocationDto that = (UserLocationDto) o;

        if (gender != that.gender) return false;
        if (fullAddress != null ? !fullAddress.equals(that.fullAddress) : that.fullAddress != null) return false;
        if (latitude != null ? !latitude.equals(that.latitude) : that.latitude != null) return false;
        return longitude != null ? longitude.equals(that.longitude) : that.longitude == null;
    }

    @Override
    public int hashCode() {
        int result = gender != null ? gender.hashCode() : 0;
        result = 31 * result + (fullAddress != null ? fullAddress.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        return result;
    }
}
