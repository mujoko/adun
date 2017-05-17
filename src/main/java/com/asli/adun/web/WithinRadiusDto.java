package com.asli.adun.web;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class WithinRadiusDto {
    @NotNull
    private Float latitude;
    @NotNull
    private Float longitude;
    @NotNull
    @Min(1)
    private Integer radius;

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WithinRadiusDto that = (WithinRadiusDto) o;

        if (!latitude.equals(that.latitude)) return false;
        if (!longitude.equals(that.longitude)) return false;
        return radius.equals(that.radius);
    }

    @Override
    public int hashCode() {
        int result = latitude.hashCode();
        result = 31 * result + longitude.hashCode();
        result = 31 * result + radius.hashCode();
        return result;
    }
}
