package com.mycompany.jobapplicationsystem;

public class Validator {

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.trim().length() >= 3;
    }

    public static boolean isValidPhone(String phone) {
        // digits only, minimum 7 digits
        return phone != null && phone.matches("\\d{7,}");
    }

    public static boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty();
    }

    public static boolean hasAtLeastOneSkill(int skillCount) {
        return skillCount > 0;
    }

    public static boolean hasCoverLetter(String coverLetter) {
        return coverLetter != null && !coverLetter.trim().isEmpty();
    }
}