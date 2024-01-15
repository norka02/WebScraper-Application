package pl.hubertmaka.project.webscraper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApartmentInfo {
    private final static Logger logger = LogManager.getLogger(ApartmentInfo.class);
    private String imgSrc = null;
    private String linkToAnnouncement = null;
    private boolean isBoosted = false;
    private String title = null;
    private String voivodeship = null;
    private String city = null;
    private String district = null;
    private String street = null;
    private Integer price = null;
    private Integer pricePerMeter = null;
    private Integer rooms = null;
    private Double size = null;
    private String additionalInfo = null;

    public String getVoivodeship() {
        return voivodeship;
    }

    public void setVoivodeship(String voivodeship) {
        this.voivodeship = voivodeship;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }



    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getLinkToAnnouncement() {
        return linkToAnnouncement;
    }

    public void setLinkToAnnouncement(String linkToAnnouncement) {
        this.linkToAnnouncement = linkToAnnouncement;
    }

    public boolean isBoosted() {
        return isBoosted;
    }

    public void setBoosted(boolean boosted) {
        isBoosted = boosted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPricePerMeter() {
        return pricePerMeter;
    }

    public void setPricePerMeter(Integer pricePerMeter) {
        this.pricePerMeter = pricePerMeter;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public String getAllInfo() {
        StringBuilder info = new StringBuilder();
        return info.append(this.title).append("\n")
                .append(this.voivodeship).append("\n")
                .append(this.city).append("\n")
                .append(this.district).append("\n")
                .append(this.isBoosted).append("\n")
                .append(this.price.toString()).append("\n")
                .append(this.pricePerMeter.toString()).append("\n")
                .append(this.rooms.toString()).append("\n")
                .append(this.size.toString()).append("\n")
                .append(this.imgSrc).append("\n")
                .append(this.linkToAnnouncement).append("\n")
                .append(this.additionalInfo).append("\n")
                .toString();
    }
}
