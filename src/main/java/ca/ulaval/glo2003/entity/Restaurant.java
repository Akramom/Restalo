package ca.ulaval.glo2003.entity;

public class Restaurant {

    private String name;
    private int capacity;
    private int noRestaurant;

    private Hours hours;

    private static int cp=1000;

    public Restaurant(String name, int capacity, Hours hours) {
        this.noRestaurant=cp;
        cp++;
        this.name = name;
        this.capacity = capacity;
        this.noRestaurant = noRestaurant;
        this.hours = hours;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getNoRestaurant() {
        return noRestaurant;
    }

    public void setNoRestaurant(int noRestaurant) {
        this.noRestaurant = noRestaurant;
    }

    public Hours getHours() {
        return hours;
    }


    public void setHours(Hours hours) {
        this.hours = hours;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", capacity=" + capacity +
                ", noRestaurant=" + noRestaurant +
                ", hours=" + hours +
                '}';
    }
}
