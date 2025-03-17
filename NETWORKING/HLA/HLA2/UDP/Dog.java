import java.io.Serializable;

public class Dog implements Serializable {

    private String name;
    private String breed;

    public Dog(){
        name = "Clifford";
        breed = "Big Red Dog";
    }

    public Dog(String name, String breed){
        this.name = name;
        this.breed = breed;
    }

    @Override
    public String toString(){
        String result = name + " is a " + breed + ".\n";
        return result;
    }

}
