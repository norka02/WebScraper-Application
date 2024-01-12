package pl.hubertmaka.project.webscraper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApartmentInfo {
    private final static Logger logger = LogManager.getLogger(ApartmentInfo.class);
    private String imgSrc = null;
    private String linkToAnnouncement = null;
    private boolean isBoosted = false;
    private String title = null;
    private String location = null;
    private String price = null;
    private String pricePerMeter = null;
    private String rooms = null;
    private String size = null;
    private String additionalInfo = null;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPricePerMeter() {
        return pricePerMeter;
    }

    public void setPricePerMeter(String pricePerMeter) {
        this.pricePerMeter = pricePerMeter;
    }

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAllInfo() {
        StringBuilder info = new StringBuilder();
        return info.append(this.title).append("\n")
                .append(this.location).append("\n")
                .append(this.isBoosted).append("\n")
                .append(this.price).append("\n")
                .append(this.pricePerMeter).append("\n")
                .append(this.rooms).append("\n")
                .append(this.size).append("\n")
                .append(this.imgSrc).append("\n")
                .append(this.linkToAnnouncement).append("\n")
                .append(this.additionalInfo).append("\n")
                .toString();
    }
}
