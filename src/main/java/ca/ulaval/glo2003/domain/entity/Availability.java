package ca.ulaval.glo2003.domain.entity;

import dev.morphia.annotations.Entity;

import java.util.Objects;

@Entity
public class Availability {
   private String start;
    private Number remainingPlaces;


    public Availability(String start, Number remainingPlaces){
        this.remainingPlaces = remainingPlaces;
        this.start=start;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public Number getRemainingPlaces() {
        return remainingPlaces;
    }

    public void setRemainingPlaces(Number remainingPlaces) {
        this.remainingPlaces = remainingPlaces;
    }


    @Override
    public int hashCode() {
        return Objects.hash(start, remainingPlaces);
    }
}
