package ie.ul.cs4084project;

public class UserTypeItem {
    private String userType;
    private int userTypeImage;

    public UserTypeItem(String userType, int userTypeImage){
        this.userType = userType;
        this.userTypeImage = userTypeImage;
    }

    public String getUserType(){
        return userType;
    }

    public int getUserTypeImage(){
        return userTypeImage;
    }
}
