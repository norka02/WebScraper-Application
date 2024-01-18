package pl.hubertmaka.project.webscraper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApartmentInfo {
    private final static Logger logger = LogManager.getLogger(ApartmentInfo.class);
    private String fromSite = null;

    public String getFromSite() {
        return fromSite;
    }

    public void setFromSite(String fromSite) {
        this.fromSite = fromSite;
    }

    private String purchaseType = "";

    public String getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(String purchaseType) {
        this.purchaseType = purchaseType;
    }

    private String imgSrc = "None";
    private String linkToAnnouncement = null;
    private boolean isBoosted = false;
    private String title = null;
    private String voivodeship = "";
    private String city = null;
    private String district = "";
    private String street = null;
    private Integer price = null;
    private Double pricePerMeter = 0.0;
    private Integer rooms = 0;
    private Double size = null;
    private String additionalInfo = "No additional info";

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

    public Double getPricePerMeter() {
        return pricePerMeter;
    }

    public void setPricePerMeter(Double pricePerMeter) {
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
        return info.append("From site: ").append(this.fromSite).append("\n")
                .append("Title: ").append(this.title).append("\n")
                .append("Voivodeship: ").append(this.voivodeship).append("\n")
                .append("City: ").append(this.city).append("\n")
                .append("District: ").append(this.district).append("\n")
                .append("Is boosted: ").append(this.isBoosted).append("\n")
                .append("Price: ").append(this.price.toString()).append(" zł").append("\n")
                .append("Price per meter: ").append(this.pricePerMeter.toString()).append(" zł/m²").append("\n")
                .append("Rooms: ").append(this.rooms.toString()).append("\n")
                .append("Apartment Size: ").append(this.size.toString()).append(" m²").append("\n")
                .append("Image src: ").append(this.imgSrc).append("\n")
                .append("Link to ann: ").append(this.linkToAnnouncement).append("\n")
                .append("Additional info: ").append(this.additionalInfo).append("\n")
                .toString();
    }
}
