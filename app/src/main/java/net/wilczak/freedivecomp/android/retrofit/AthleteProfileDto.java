package net.wilczak.freedivecomp.android.retrofit;

import com.google.gson.annotations.SerializedName;

public class AthleteProfileDto {
    @SerializedName("AthleteId")
    private String athleteId;
    @SerializedName("FirstName")
    private String firstName;
    @SerializedName("Surname")
    private String surname;
    @SerializedName("Club")
    private String club;
    @SerializedName("CountryName")
    private String countryName;
    @SerializedName("ProfilePhotoName")
    private String profilePhotoName;
    @SerializedName("Sex")
    private String sex;
    @SerializedName("Category")
    private String category;
    @SerializedName("ModeratorNotes")
    private String moderatorNotes;

    public String getAthleteId() {
        return athleteId;
    }

    public AthleteProfileDto setAthleteId(String athleteId) {
        this.athleteId = athleteId;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public AthleteProfileDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public AthleteProfileDto setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getClub() {
        return club;
    }

    public AthleteProfileDto setClub(String club) {
        this.club = club;
        return this;
    }

    public String getCountryName() {
        return countryName;
    }

    public AthleteProfileDto setCountryName(String countryName) {
        this.countryName = countryName;
        return this;
    }

    public String getProfilePhotoName() {
        return profilePhotoName;
    }

    public AthleteProfileDto setProfilePhotoName(String profilePhotoName) {
        this.profilePhotoName = profilePhotoName;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public AthleteProfileDto setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public AthleteProfileDto setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getModeratorNotes() {
        return moderatorNotes;
    }

    public AthleteProfileDto setModeratorNotes(String moderatorNotes) {
        this.moderatorNotes = moderatorNotes;
        return this;
    }
}
