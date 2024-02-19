package ca.ulaval.glo2003.entity;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import java.util.regex.Pattern;

public class Reservation {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{1,14}$");
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;
    public int groupSize;
    public String name;
    public String email;
    public String phoneNumber;
    public Hours hours;
    private UUID id;

    public Reservation(LocalDate date, Hours hours, LocalTime startTime, LocalTime endTime, int groupSize, String name, String email, String phoneNumber) {
        this.date = date;
        this.hours = hours;
        this.startTime = adjustStartTime(startTime);
        this.endTime = calculateEndTime();
        this.groupSize = groupSize;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.id = UUID.randomUUID();

        validateGroupSize(groupSize);
        validateName(name);
        validateEmail(email);
        validatePhoneNumber(phoneNumber);
        validateReservationDuration();
    }

    private LocalTime adjustStartTime(LocalTime startTime) {
        int minute = startTime.getMinute();
        if (minute % 15 != 0) {
            int adjustment = (15 - (minute % 15));
            return startTime.plusMinutes(adjustment);
        }
        return startTime;
    }

    private LocalTime calculateEndTime(){
        return this.startTime.plusMinutes(hours.getReservationDuration());
    }

    private void validateName(String name){
        if (name ==null  || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name can't be empty");
        }
    }

    private void validateEmail(String email){
    if (!EMAIL_PATTERN.matcher(email).matches()){
        throw new IllegalArgumentException("Invalid Email.");
    }
    }

    private void validateGroupSize(int groupSize){
        if(groupSize < 1){
            throw new IllegalArgumentException("Group size might at least include 1 person");
        }
    }

    private void validatePhoneNumber(String phoneNumber){
        if (!PHONE_PATTERN.matcher(phoneNumber).matches()){
            throw new IllegalArgumentException("Invalid Phone number");
        }
    }

    private void validateReservationDuration(){
        if (this.endTime.isAfter(hours.getClose())){
            throw new IllegalArgumentException("Reservation end time can't exceeds closing time.");
        }
    }

    @Override
    public String toString() {
        return "{ " +
                "date :" + date+
                ", startTime :" + startTime +
                ", groupSize :" + groupSize +
                ", customer : {" +
                        "name: " + name +
                        ", email: " + email +
                        ", phoneNumber :" + phoneNumber +
                 '}'+
                '}';
    }
}

