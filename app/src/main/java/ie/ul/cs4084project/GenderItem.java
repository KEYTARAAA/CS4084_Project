package ie.ul.cs4084project;

public class GenderItem {
    private String gender;
    private int genderImage;

    public GenderItem(String userType, int userTypeImage){
        this.gender = userType;
        this.genderImage = userTypeImage;
    }

    public String getGender(){
        return gender;
    }

    public int getGenderImage(){
        return genderImage;
    }
}
