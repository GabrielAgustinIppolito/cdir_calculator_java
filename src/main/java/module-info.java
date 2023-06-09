module it.gabriel.cdir_calculator {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
                    requires org.kordamp.bootstrapfx.core;
            
    opens it.gabriel.cdir_calculator to javafx.fxml;
    exports it.gabriel.cdir_calculator;
}