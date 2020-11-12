package ie.ul.cs4084project;

public class GoalItem {
    private String goal;
    private int goalImage;

    public GoalItem(String userType, int userTypeImage){
        this.goal = userType;
        this.goalImage = userTypeImage;
    }

    public String getGoal(){
        return goal;
    }

    public int getGoalImage(){
        return goalImage;
    }
}
