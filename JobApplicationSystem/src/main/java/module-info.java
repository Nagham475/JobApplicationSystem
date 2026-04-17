module com.mycompany.jobapplicationsystem {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.jobapplicationsystem to javafx.fxml;
    exports com.mycompany.jobapplicationsystem;
}
